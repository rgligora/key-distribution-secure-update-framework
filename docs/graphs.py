# Import necessary libraries
import pandas as pd
import matplotlib.pyplot as plt

# Load gathered during initialization and load prediction
def plot_initialization_load():
    # Load data
    init_load_data = pd.read_csv('initial_device_load_data.csv')
    predicted_load_data = pd.read_csv('predicted_device_load_data.csv')
    
    # Shift the initial load time to start from zero
    init_load_start_time = init_load_data['Time'].min()
    init_load_data['Time'] = init_load_data['Time'] - init_load_start_time
    
    # Shift the predicted load time to start from zero
    predicted_load_start_time = predicted_load_data['Time'].min()
    predicted_load_data['Time'] = predicted_load_data['Time'] - predicted_load_start_time

    # Find the end of initialization time in the shifted time frame
    end_of_initialization_time = predicted_load_data[predicted_load_data['Predicted Memory Usage'] > 0]['Time'].min()
    
    # Create the first subplot for initial load data
    fig, ax1 = plt.subplots(figsize=(10, 6))
    
    ax1.plot(init_load_data['Time'], init_load_data['CPU Usage'], label='CPU Usage (Initial)', color='b')
    ax1.plot(init_load_data['Time'], init_load_data['Memory Usage'], label='Memory Usage (Initial)', color='g')
    ax1.set_xlabel('Time')
    ax1.set_ylabel('Usage (%)')
    
    # Create the twin axes for predicted load data
    ax2 = ax1.twiny()
    ax2.plot(predicted_load_data['Time'], predicted_load_data['Predicted CPU Usage'], label='Predicted CPU Usage', linestyle='--', color='r')
    ax2.plot(predicted_load_data['Time'], predicted_load_data['Predicted Memory Usage'], label='Predicted Memory Usage', linestyle='--', color='m')
    ax2.set_xlabel('Time (Predicted)')
    
    ax1.axvline(x=end_of_initialization_time, color='black', linestyle='--', linewidth=1)
    y_min, y_max = ax1.get_ylim()
    y_mid = (y_min + y_max) / 2
    ax1.text(end_of_initialization_time, y_mid, 'End of initialization', rotation=90, verticalalignment='center', horizontalalignment='right', color='black', fontsize=10)
    
    # Adding legends
    lines_1, labels_1 = ax1.get_legend_handles_labels()
    lines_2, labels_2 = ax2.get_legend_handles_labels()
    ax1.legend(lines_1 + lines_2, labels_1 + labels_2, loc='best')
    
    plt.title('CPU and Memory Load During Initialization and Predicted Load')
    plt.show()


# RSA key generation impact on performance
def plot_rsa_load():
    rsa_load_data = pd.read_csv('rsa_load_data.csv')
    
    time = rsa_load_data['Time'].min()
    
    plt.figure(figsize=(10, 6))
    plt.plot(rsa_load_data['Time']-time, rsa_load_data['CPU Usage'], label='CPU Usage')
    plt.plot(rsa_load_data['Time']-time, rsa_load_data['Memory Usage'], label='Memory Usage')
    
    plt.xlabel('Time')
    plt.ylabel('Usage')
    plt.title('CPU and Memory Load During RSA Key Generation')
    plt.legend()
    plt.show()


# Registration process impact on performance
def plot_registration_load():
    registration_load_data = pd.read_csv('registration_load_data.csv')
    
    plt.figure(figsize=(10, 6))
    plt.plot(registration_load_data['Time'], registration_load_data['CPU Usage'], label='CPU Usage')
    plt.plot(registration_load_data['Time'], registration_load_data['Memory Usage'], label='Memory Usage')
    plt.xlabel('Time')
    plt.ylabel('Usage (%)')
    plt.title('CPU and Memory Load During Device Registration')
    plt.legend()
    plt.show()

# Generate the plots
plot_initialization_load()
plot_rsa_load()
plot_registration_load()
