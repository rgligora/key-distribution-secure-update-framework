# Key Distribution and Secure Updates Framework

## Overview
This Key Distribution and Secure Updates Framework is a comprehensive platform designed to manage IoT devices, companies, software, and software packages. The system ensures secure communication and efficient update management between IoT devices and the backend service. It includes a backend service, an IoT device client with device load monitoring, a user-friendly frontend interface, and Vault for secure key management, ensuring that cryptographic keys are securely generated, stored, and distributed.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Components](#components)
- [Setup](#setup)
- [Usage](#usage)

## Features
- Secure IoT device registration
- Device load monitoring and optimal update scheduling
- Secure software package management
- Encrypted communication between devices and backend
- User-friendly dashboard for managing companies, devices, models, and software packages

## Architecture
The system consists of three main components:
1. **Backend Service**: Implemented in Java and Spring Boot, it provides RESTful APIs for managing users, companies, devices, models, and software packages.
2. **IoT Device Client**: A Python script simulating an IoT device with a mock HSM for secure key management and encrypted communication with the backend.
3. **Frontend**: A React-based user interface for managing the system.

## Components

### Backend Service
- **AdminUserServiceImpl**: Manages admin users.
- **CompanyServiceImpl**: Manages companies.
- **DeviceServiceImpl**: Manages devices.
- **ModelServiceImpl**: Manages models.
- **SoftwarePackageServiceImpl**: Manages software packages.
- **SoftwareServiceImpl**: Manages software entries.
- **UpdateHistoryServiceImpl**: Manages update histories.
- **UpdateServiceImpl**: Manages the update process for devices.
- **VaultSecretServiceImpl**: Handles encryption, decryption, and key management using Vault.

### IoT Device Client
- **MockHSM**: A mock Hardware Security Module for encryption, decryption, and key management.
- **Device Registration**: Registers the device with the backend.
- **Update Management**: Checks for updates, downloads them, verifies signatures, and flashes software packages.

### Frontend
- **App.js**: Main React component for routing.
- **MainDashboard.js**: Displays an overview of companies, devices, software, and software packages.
- **Dashboard.js**: Displays detailed information about a specific company.
- **Models.js**: Manages models.
- **Devices.js**: Manages devices.
- **Software.js**: Manages software entries.
- **SoftwarePackages.js**: Manages software packages.

## Setup

### Prerequisites
- Java 11 or higher
- Maven
- Node.js and npm
- Docker and Docker Compose
- Python 3.x
- Virtualenv (optional but recommended)

### Clone
1. Clone the repository:
    ```sh
    git clone https://github.com/rgligora/key-distribution-secure-update-framework.git
    ```

### Vault Setup
1. Navigate to the `backend` directory:
    ```sh
    cd key-distribution-secure-update-framework/backend
    ```

2. Build and run Vault using Docker:
    ```sh
    docker build -t vault-setup .
    docker run -it --name vault-container -p 8200:8200 vault-setup
    ```

### Backend Service
1. Navigate to the `backend` directory:
    ```sh
    cd key-distribution-secure-update-framework/backend
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the Spring Boot application:
    ```sh
    mvn spring-boot:run
    ```

### Frontend
1. Navigate to the frontend directory:
    ```sh
    cd frontend
    ```

2. Install the dependencies:
    ```sh
    npm install
    ```

3. Start the React application:
    ```sh
    npm start
    ```

### IoT Device Client
1. Create and activate a virtual environment:
    ```sh
    cd key-distribution-secure-update-framework/backend
    python -m venv venv
    source venv/bin/activate
    ```

2. Install the dependencies:
    ```sh
    pip install -r requirements.txt
    ```

3. Run the IoT device client:
    ```sh
    python device_client.py
    ```

## Usage
Once all components are running, you can access the frontend at `http://localhost:3000` to interact with the system. The backend service runs on `http://localhost:8080`, and the IoT device client communicates with this backend service for registration and updates.
