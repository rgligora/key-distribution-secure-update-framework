import requests
import time
import psutil
from collections import deque

client_cert = ("path/to/client.crt", "path/to/client.key")
ca_cert = "path/to/CA_cert.pem"
SERIAL_NO = "7c3ef80b-9ea0-4d67-b494-67a18bb22a27"

LOAD_HISTORY_WINDOW = 30
SNAPSHOT_INTERVAL = 1

cpuLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)
memoryLoadHistory = deque(maxlen=LOAD_HISTORY_WINDOW)

def systemLoadSnaphot():
    cpuUsage = psutil.cpu_percent(interval=1)
    memoryUsage = psutil.virtual_memory().percent
    print(f"CPU Usage: {cpuUsage}%, Memory Usage: {memoryUsage}%")
    cpuLoadHistory.append(cpuUsage)
    memoryLoadHistory.append(memoryUsage)


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
            systemLoadSnaphot()
            time.sleep(SNAPSHOT_INTERVAL)

    while True:
        systemLoadSnaphot()
        print(cpuLoadHistory)
        print(memoryLoadHistory)
        time.sleep(SNAPSHOT_INTERVAL)


        

if __name__ == "__main__":
    main()


