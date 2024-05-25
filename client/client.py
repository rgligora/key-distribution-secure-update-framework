import requests
import time
import psutil
from collections import deque

client_cert = ("path/to/client.crt", "path/to/client.key")
ca_cert = "path/to/CA_cert.pem"
SERIAL_NO = "7c3ef80b-9ea0-4d67-b494-67a18bb22a27"

LOAD_HISTORY_WINDOW = 30
PREDICT_WINDOW = 10
SNAPSHOT_INTERVAL = 0.2
CPU_THRESHOLD = 30.0
MEMORY_THRESHOLD = 80.0

cpuLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)
memoryLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)

def deviceLoadSnaphot():
    cpuUsage = psutil.cpu_percent(interval=1)
    memoryUsage = psutil.virtual_memory().percent
    print(f"CPU usage: {cpuUsage}, MEMORY usage: {memoryUsage}")
    cpuLoadHistory.append(cpuUsage)
    memoryLoadHistory.append(memoryUsage)

def isOptimaldeviceLoad(cpuThreshold=CPU_THRESHOLD, memoryThreshold=MEMORY_THRESHOLD):
    predictedCpuUsage, predictedMemoryUsage = predictdeviceLoad()
    print(f"Predicted CPU Usage: {predictedCpuUsage}%, Predicted Memory Usage: {predictedMemoryUsage}%")
    return predictedCpuUsage < cpuThreshold and predictedMemoryUsage < memoryThreshold

def predictdeviceLoad(predictWindow=PREDICT_WINDOW):
    cpuLoadList = list(cpuLoadHistory)
    memoryLoadList = list(memoryLoadHistory)
    predictedCpuUsage = sum(cpuLoadList[-predictWindow:]) / len(cpuLoadList[-predictWindow:])
    predictedMemoryUsage = sum(memoryLoadList[-predictWindow:]) / len(memoryLoadList[-predictWindow:])
    return predictedCpuUsage, predictedMemoryUsage



def register_device():
    url = "/api/register"
    payload = {
        "serialNo": SERIAL_NO
    }
    response = requests.post(url, json=payload, cert=client_cert, verify=ca_cert)
    if response.status_code == 200:
        return response.json()
    else:
        raise Exception("Device registration failed: " + response.text)


def checkForUpdates(deviceId):
    url = f"https://api/updates/check/{deviceId}"

def downloadUpdate(deviceId):
    url = f"https://api/updates/check/{deviceId}"

def main():
    for snapshot in range(LOAD_HISTORY_WINDOW):
            deviceLoadSnaphot()
            time.sleep(SNAPSHOT_INTERVAL)

    while True:
        deviceLoadSnaphot()

        if isOptimaldeviceLoad():
            print("INIT")
        else:
            print("Waiting for optimal sytem load")
        time.sleep(SNAPSHOT_INTERVAL)


if __name__ == "__main__":
    main()


