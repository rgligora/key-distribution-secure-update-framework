import base64
import os
import json
import random
import time
import psutil
import requests
from collections import deque
from Crypto.PublicKey import RSA
from Crypto.Cipher import AES, PKCS1_OAEP
from Crypto.Random import get_random_bytes
from Crypto.Hash import SHA256
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

backend = "http://127.0.0.1:8080"

SERIAL_NO = "a966d08b-7f3e-4ece-9d31-b4e08251d2e1"

HSM_DEVICE_ID_FILE = 'hsm_deviceId.json'
HSM_BACKEND_PUBKEY_FILE = 'hsm_backendPubKey.json'
HSM_KEY_FILE = 'hsm_key.key'

LOAD_HISTORY_WINDOW = 20
PREDICT_WINDOW = 10
SNAPSHOT_INTERVAL = 0.2
CPU_THRESHOLD = 40.0
MEMORY_THRESHOLD = 90.0

class MockHSM:
    def __init__(self):
        self.key = self.startUpHSM()
        self.deviceId = self.deviceIdHSM()
        self.backendPubKey = self.backendPublicKey()
        self.privKey = RSA.generate(2048)
        self.public_key = self.privKey.publickey()
        self.session_key = None

    def startUpHSM(self):
        if os.path.exists(HSM_KEY_FILE):
            with open(HSM_KEY_FILE, 'rb') as keyFile:
                return keyFile.read()
        else:
            key = get_random_bytes(32)
            with open(HSM_KEY_FILE, 'wb') as keyFile:
                keyFile.write(key)
            return key

    def getDevicePubKeyBytes(self):
        return self.public_key.export_key(format='DER')

    def encryptHSM(self, plaintext):
        iv = get_random_bytes(12)
        cipher = AES.new(self.key, AES.MODE_GCM, iv)
        encrypted, tag = cipher.encrypt_and_digest(plaintext.encode())
        return base64.urlsafe_b64encode(iv + tag + encrypted).decode()

    def decryptHSM(self, ciphertext):
        raw = base64.urlsafe_b64decode(ciphertext)
        iv = raw[:12]
        tag = raw[12:28]
        encrypted_data = raw[28:]
        cipher = AES.new(self.key, AES.MODE_GCM, iv)
        decrypted = cipher.decrypt_and_verify(encrypted_data, tag)
        return decrypted.decode()

    def deviceIdHSM(self):
        if os.path.exists(HSM_DEVICE_ID_FILE):
            with open(HSM_DEVICE_ID_FILE, 'r') as f:
                data = json.load(f)
                encrypted_device_id = data.get('deviceId')
                if encrypted_device_id:
                    return encrypted_device_id
        return None

    def storeDeviceId(self, deviceId):
        self.deviceId = self.encryptHSM(deviceId)
        with open(HSM_DEVICE_ID_FILE, 'w') as f:
            json.dump({'deviceId': self.deviceId}, f)
    
    def retrieveDeviceId(self):
        if self.deviceId:
            return self.decryptHSM(self.deviceId)
        return None

    def storeBackendPubKey(self, backendPubKey):
        self.backendPubKey = backendPubKey
        with open(HSM_BACKEND_PUBKEY_FILE, 'w') as f:
            json.dump({'backendPubKey': self.encryptHSM(backendPubKey)}, f)
    
    def retrieveBackendPubKey(self):
        if self.backendPubKey:
            return self.backendPubKey
        return None

    def backendPublicKey(self):
        if os.path.exists(HSM_BACKEND_PUBKEY_FILE):
            with open(HSM_BACKEND_PUBKEY_FILE, 'r') as f:
                data = json.load(f)
                encrypted_backend_pub_key = data.get('backendPubKey')
                if encrypted_backend_pub_key:
                    return self.decryptHSM(encrypted_backend_pub_key)
        url = f"{backend}/api/keys/backend"
        response = requests.get(url)
        if response.status_code == 200:
            backendPubKey = response.text
            self.storeBackendPubKey(backendPubKey)
            return backendPubKey
        else:
            print("Backend public key retrieval failed: " + response.text)
            exit()

    def establishSession(self):
        url = f"{backend}/api/keys/sessionKey"
        devicePublicKeyBytes = self.getDevicePubKeyBytes()
        devicePublicKeyBase64 = base64.b64encode(devicePublicKeyBytes).decode('utf-8')
        response = requests.post(url, json={"devicePublicKey": devicePublicKeyBase64})
        if response.status_code == 200:
            response_data = response.text
            self.session_key = self.decryptSessionKey(response_data)
            return self.session_key
        else:
            print("Session key creation failed: " + response.text)
            exit()

    def decryptSessionKey(self, encrypted_key_base64):
        try:
            encrypted_key = base64.b64decode(encrypted_key_base64)
            cipher_rsa = PKCS1_OAEP.new(self.privKey)
            decrypted_key = cipher_rsa.decrypt(encrypted_key)
            return decrypted_key
        except Exception as e:
            print(f"Failed to decrypt session key: {str(e)}")
            raise

    def encryptWithSessionKey(self, plaintext):
        nonce = os.urandom(12)
        aesgcm = AESGCM(self.session_key)
        ciphertext = aesgcm.encrypt(nonce, plaintext.encode(), None)
       
        encrypted_data = nonce + ciphertext
        return f"vault:v1:{base64.b64encode(encrypted_data).decode()}"
    
    def decryptWithSessionKey(self, ciphertext):
        encodedData = ciphertext[len("vault:v1:"):]
        data = base64.b64decode(encodedData)
        nonce, ciphertext = data[:12], data[12:]
        aesgcm = AESGCM(self.session_key)
        return aesgcm.decrypt(nonce, ciphertext, None).decode()

cpuLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)
memoryLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)

def deviceLoadSnapshot():
    cpuUsage = psutil.cpu_percent(interval=1)
    memoryUsage = psutil.virtual_memory().percent
    print(f"CPU usage: {cpuUsage}, MEMORY usage: {memoryUsage}")
    cpuLoadHistory.append(cpuUsage)
    memoryLoadHistory.append(memoryUsage)

def isOptimalDeviceLoad(cpuThreshold=CPU_THRESHOLD, memoryThreshold=MEMORY_THRESHOLD):
    predictedCpuUsage, predictedMemoryUsage = predictDeviceLoad()
    print(f"Predicted CPU Usage: {predictedCpuUsage}%, Predicted Memory Usage: {predictedMemoryUsage}%")
    return predictedCpuUsage < cpuThreshold and predictedMemoryUsage < memoryThreshold

def predictDeviceLoad(predictWindow=PREDICT_WINDOW):
    cpuLoadList = list(cpuLoadHistory)
    memoryLoadList = list(memoryLoadHistory)
    predictedCpuUsage = sum(cpuLoadList[-predictWindow:]) / len(cpuLoadList[-predictWindow:])
    predictedMemoryUsage = sum(memoryLoadList[-predictWindow:]) / len(memoryLoadList[-predictWindow:])
    return predictedCpuUsage, predictedMemoryUsage

def registerDevice():
    url = f"{backend}/api/devices/register"
    payload = {
        "serialNo": SERIAL_NO,
        "publicKey": base64.b64encode(hsm.getDevicePubKeyBytes()).decode('utf-8')
    }
    response = requests.post(url, json={"devicePublicKey": base64.b64encode(hsm.getDevicePubKeyBytes()).decode('utf-8'), "encryptedData": hsm.encryptWithSessionKey(json.dumps(payload))})
    if response.status_code == 200:
        EncryptedDto = response.json()
        decryptedDeviceInfo = hsm.decryptWithSessionKey(EncryptedDto["encryptedData"])
        return json.loads(decryptedDeviceInfo)
    else:
        print("Device registration failed: " + response.text)
        exit()

def checkForUpdates(deviceId):
    url = f"{backend}/api/updates/check"
    payload = {
        "deviceId": deviceId
    }
    response = requests.post(url, json={"devicePublicKey": base64.b64encode(hsm.getDevicePubKeyBytes()).decode('utf-8'), "encryptedData": hsm.encryptWithSessionKey(json.dumps(payload))})
    if response.status_code == 200:
        EncryptedDto = response.json()
        decryptedUpdateInfo = hsm.decryptWithSessionKey(EncryptedDto["encryptedData"])
        return json.loads(decryptedUpdateInfo)
    else:
        print("Update check failed: " + response.text)
        exit()

def downloadUpdate(deviceId, softwarePackageId):
    url = f"{backend}/api/updates/download"
    payload = {
        "deviceId": deviceId,
        "softwarePackageId": softwarePackageId
    }
    response = requests.post(url, json={"devicePublicKey": base64.b64encode(hsm.getDevicePubKeyBytes()).decode('utf-8'), "encryptedData": hsm.encryptWithSessionKey(json.dumps(payload))})
    if response.status_code == 200:
        EncryptedDto = response.json()
        decryptedSoftwarePackageDto = hsm.decryptWithSessionKey(EncryptedDto["encryptedData"])
        return json.loads(decryptedSoftwarePackageDto)
    else:
        print("Software Package download failed: " + response.text)
        exit()

def verifySignature(deviceId, softwarePackage, signature):
    url = f"{backend}/api/updates/verify"
    payload = {
        "deviceId" : deviceId,
        "softwarePackageDto": softwarePackage,
        "signature": signature
    }
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        return response.json()["valid"]
    else:
        print("Signature verification failed: " + response.text)
        exit()

def flashSoftwarePackages(deviceId, flashingSoftwarePackages, softwarePackageIds):
    print(f"Flashing device: {deviceId}...")
    for softwarePackage in flashingSoftwarePackages:
        print(f"Software package ID: {softwarePackage['softwarePackageId']}")
        for sw in softwarePackage['includedSoftware']:
            print(f"Included software: {sw}")

    time.sleep(15)
    success = random.choice([True, False])
    if success:
        print("Flashing successful!")
    else:
        print("Flashing failed!")

    url = f"{backend}/api/updates/flashing"
    payload = {
        "deviceId": deviceId,
        "softwarePackageIds": softwarePackageIds,
        "success": success
    }
    response = requests.post(url, json=payload)
    
    return success

hsm = MockHSM()

def loadDeviceIdHSM():
    return hsm.retrieveDeviceId()

def saveDeviceIdHSM(deviceId):
    hsm.storeDeviceId(deviceId)

def loadBackendPubKeyHSM():
    hsm.retrieveBackendPubKey()

def establishSessionHSM():
    hsm.establishSession()

def main():
    deviceId = loadDeviceIdHSM()
    loadBackendPubKeyHSM()
    establishSessionHSM()
    
    if deviceId is None:
        deviceInfo = registerDevice()
        deviceId = deviceInfo["deviceId"]
        saveDeviceIdHSM(deviceId)
        print(f'Registered new device with deviceId: {deviceId}')
    else:
        print(f'Device already registered with deviceId: {deviceId}')

    for snapshot in range(LOAD_HISTORY_WINDOW):
            deviceLoadSnapshot()
            time.sleep(SNAPSHOT_INTERVAL)

    while True:
        deviceLoadSnapshot()

        if isOptimalDeviceLoad():
            updateInfo = checkForUpdates(deviceId)
            if updateInfo["available"]:
                flashingSoftwarePackages = []
                for softwarePackageId in updateInfo["softwarePackageIds"]:
                    softwarePackage = downloadUpdate(deviceId, softwarePackageId)
                    if not verifySignature(deviceId, softwarePackage, softwarePackage["signature"]):
                        print(f"Signature verification failed for package: {softwarePackageId}")
                        exit()

                    flashingSoftwarePackages.append(softwarePackage)
                
                success = flashSoftwarePackages(deviceId, flashingSoftwarePackages, updateInfo["softwarePackageIds"])
                while not success:
                    success = flashSoftwarePackages(deviceId, flashingSoftwarePackages, updateInfo["softwarePackageIds"])
                
            else:
                print(f"No updates found for device: {deviceId}...")
        else:
            print("Waiting for optimal system load")
        time.sleep(SNAPSHOT_INTERVAL)

if __name__ == "__main__":
    main()
