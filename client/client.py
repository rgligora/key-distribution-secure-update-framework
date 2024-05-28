import requests
import time
import psutil
from collections import deque
import os
import json
import random
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.backends import default_backend
import base64

backend = "http://localhost:8080"
client_cert = ("path/to/client.crt", "path/to/client.key")
ca_cert = "path/to/CA_cert.pem"

SERIAL_NO = "a966d08b-7f3e-4ece-9d31-b4e08251d2e1"

HSM_DEVICE_ID_FILE = 'hsm_deviceId.json'
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

    def startUpHSM(self):
        if os.path.exists(HSM_KEY_FILE):
            with open(HSM_KEY_FILE, 'rb') as keyFile:
                return keyFile.read()
        else:
            key = os.urandom(32)
            with open(HSM_KEY_FILE, 'wb') as keyFile:
                keyFile.write(key)
            return key

    def encrypt(self, plaintext):
        iv = os.urandom(12)
        cipher = Cipher(algorithms.AES(self.key), modes.GCM(iv), backend=default_backend())
        encryptor = cipher.encryptor()
        encrypted = encryptor.update(plaintext.encode()) + encryptor.finalize()
        return base64.urlsafe_b64encode(iv + encryptor.tag + encrypted).decode()

    def decrypt(self, ciphertext):
        raw = base64.urlsafe_b64decode(ciphertext)
        iv = raw[:12]
        tag = raw[12:28]
        encrypted_data = raw[28:]
        cipher = Cipher(algorithms.AES(self.key), modes.GCM(iv, tag), backend=default_backend())
        decryptor = cipher.decryptor()
        decrypted = decryptor.update(encrypted_data) + decryptor.finalize()
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
        self.deviceId = self.encrypt(deviceId)
        with open(HSM_DEVICE_ID_FILE, 'w') as f:
            json.dump({'deviceId': self.deviceId}, f)
    
    def retrieveDeviceId(self):
        if self.deviceId:
            return self.decrypt(self.deviceId)
        return None


cpuLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)
memoryLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)

def deviceLoadSnaphot():
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
        "serialNo": SERIAL_NO
    }
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        return response.json()
    else:
        print("Device registration failed: " + response.text)
        exit()


def checkForUpdates(deviceId):
    url = f"{backend}/api/updates/check/{deviceId}"
    response = requests.get(url)
    if response.status_code == 200:
        updateInfo = response.json()
        return updateInfo
    else:
        print("Update check failed: " + response.text)
        exit()


def downloadUpdate(deviceId, softwarePackageId):
    url = f"{backend}/api/updates/download"
    payload = {
        "deviceId": deviceId,
        "softwarePackageId": softwarePackageId
    }
    response = requests.post(url, json=payload)
    if response.status_code == 200:
        softwarePackageDto = response.json()
        return softwarePackageDto
    else:
        print("Software Package download failed: " + response.text)
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

def saveDeviceIdHSM(device_id):
    hsm.storeDeviceId(device_id)

def main():

    deviceId = loadDeviceIdHSM()
    
    if deviceId is None:
        deviceInfo = registerDevice()
        deviceId = deviceInfo["deviceId"]
        saveDeviceIdHSM(deviceId)
        print(f'Registered new device with deviceId: {deviceId}')
    else:
        print(f'Device already registered with deviceId: {deviceId}')


    for snapshot in range(LOAD_HISTORY_WINDOW):
            deviceLoadSnaphot()
            time.sleep(SNAPSHOT_INTERVAL)

    while True:
        deviceLoadSnaphot()

        if isOptimalDeviceLoad():
            updateInfo = checkForUpdates(deviceId)
            if updateInfo["available"]:
                flashingSoftwarePackages = []
                for softwarePackageId in updateInfo["softwarePackageIds"]:
                    softwarePackage = downloadUpdate(deviceId, softwarePackageId)
                    flashingSoftwarePackages.append(softwarePackage)
                
                success = flashSoftwarePackages(deviceId, flashingSoftwarePackages, updateInfo["softwarePackageIds"])
                while not success:
                    success = flashSoftwarePackages(deviceId, flashingSoftwarePackages, updateInfo["softwarePackageIds"])
                
            else:
                print(f"No updates found for device: {deviceId}...")
        else:
            print("Waiting for optimal sytem load")
        time.sleep(SNAPSHOT_INTERVAL)


if __name__ == "__main__":
    main()


