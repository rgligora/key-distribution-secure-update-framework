import React, { useEffect, useState, useRef } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Selection, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button, Modal} from '../components';
import { fetchDataWithRequestParams, updateData } from '../api.js';

const Devices = ({ companyId }) => {
  const [confirmedDevices, setConfirmedDevices] = useState([]);
  const [pendingDevices, setPendingDevices] = useState([]);
  const confirmedGridRef = useRef(null);
  const pendingGridRef = useRef(null);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [updateHistory, setUpdateHistory] = useState([]);
  const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);


  useEffect(() => {
    const getDevices = async () => {
      try {
        const data = await fetchDataWithRequestParams('devices', { companyId });
        const confirmed = data.filter((device) => device.status !== 'REGISTERED');
        const pending = data.filter((device) => device.status === 'REGISTERED');
        setConfirmedDevices(confirmed);
        setPendingDevices(pending);

        if (confirmedGridRef.current) {
          confirmedGridRef.current.dataSource = confirmed;
        }
        if (pendingGridRef.current) {
          pendingGridRef.current.dataSource = pending;
        }
      } catch (error) {
        console.error('Failed to load devices:', error);
      }
    };

    getDevices();
  }, [companyId]);

  const confirmDevice = async (deviceId) => {
    try {
      await updateData(`devices/${deviceId}/confirm`, {});
      const updatedPendingDevices = pendingDevices.filter((device) => device.deviceId !== deviceId);
      const updatedConfirmedDevices = pendingDevices.filter((device) => device.deviceId === deviceId).map((device) => ({
        ...device,
        status: 'ACTIVE',
      }));

      setPendingDevices(updatedPendingDevices);
      setConfirmedDevices((prevConfirmed) => [...prevConfirmed, ...updatedConfirmedDevices]);

      if (pendingGridRef.current) {
        pendingGridRef.current.refresh();
      }
      if (confirmedGridRef.current) {
        confirmedGridRef.current.refresh();
      }
    } catch (error) {
      console.error('Failed to confirm device:', error);
    }
  };

  const fetchUpdateHistory = async (deviceId) => {
    try {
      const response = await fetchDataWithRequestParams(`update-history`, { deviceId });
      setUpdateHistory(response);
      setIsHistoryModalOpen(true);
    } catch (error) {
      console.error('Failed to load update history:', error);
    }
  };

  const handleDeviceRowSelected = (args) => {
    const selectedData = args.data;
    setSelectedDevice(selectedData);
    fetchUpdateHistory(selectedData.deviceId);
  };
    
  const gridOrderStatus = (props) => {
    let statusBg = '';
    const status = props.status;

    switch (status) {
      case 'ACTIVE':
        statusBg = '#b3e87d';
        break;
      case 'INACTIVE':
        statusBg = '#ff6b6b';
        break;
      case 'UPDATE_PENDING':
        statusBg = '#f0ad4e';
        break;
      case 'UPDATING':
        statusBg = '#5bc0de';
        break;
      case 'SUCCESS':
        statusBg = '#80de21';
        break;
      case 'FAILED':
        statusBg = '#ff2414';
        break;
      default:
        statusBg = 'gray';
    }

    return (
      <button
        type="button"
        style={{ background: statusBg }}
        className="text-white py-1 px-2 capitalize rounded-2xl text-md"
      >
        {props.status}
      </button>
    );
  };

  const deviceColumns = [
    { field: 'deviceId', headerText: 'Device ID', width: '150', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: '120', textAlign: 'Center' },
    { field: 'registrationDate', headerText: 'Registration Date', width: '120', textAlign: 'Center', format: 'd.M.y', editType: 'datepicker' },
    { field: 'lastUpdated', headerText: 'Last Updated', width: '150', textAlign: 'Center', format: 'dd.MM.yyyy HH:mm', editType: 'datetimepicker' },
    { field: 'status', headerText: 'Status', template: gridOrderStatus, textAlign: 'Center', width: '120' }
  ];

  const pendingDeviceColumns = [
    ...deviceColumns,
    {
      headerText: 'Actions',
      template: (props) => (
        <Button onClick={() => confirmDevice(props.deviceId)} className="bg-teal-600 text-white p-2 rounded-lg hover:bg-teal-700">
          Confirm
        </Button>
      ),
      width: '150',
      textAlign: 'Center',
    },
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Devices" />

      <h2 className="text-2xl font-semibold mb-4">Devices Waiting for Confirmation</h2>
      <GridComponent
        ref={pendingGridRef}
        key={`pending-devices-${pendingDevices.length}`}
        dataSource={pendingDevices}
        allowPaging
        allowSorting
      >
        <ColumnsDirective>
          {pendingDeviceColumns.map((col, index) => (
            <ColumnDirective key={index} {...col} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit, Selection]} />
      </GridComponent>

      <h2 className="text-2xl font-semibold mt-8 mb-4">Confirmed Devices</h2>
      <Modal isOpen={isHistoryModalOpen} onClose={() => setIsHistoryModalOpen(false)} title={`Update History for device: ${selectedDevice?.deviceId}`}>
        <GridComponent dataSource={updateHistory} allowPaging={true} pageSettings={{ pageSize: 10 }}>
          <ColumnsDirective>
            <ColumnDirective field="updateDate" headerText="Update Date" textAlign="Center" format="dd.MM.yyyy HH:mm" />
            <ColumnDirective 
              field="softwarePackageId" 
              headerText="Software Package" 
              width="200" 
              textAlign="Center" 
              template={props => <span title={props.softwarePackageId}>{props.softwarePackageId}</span>} 
            />
            <ColumnDirective field="status" headerText="Status" textAlign="Center" template={gridOrderStatus} />
          </ColumnsDirective>
          <Inject services={[Page]} />
        </GridComponent>
      </Modal>
      <GridComponent
        ref={confirmedGridRef}
        key={`confirmed-devices-${confirmedDevices.length}`}
        dataSource={confirmedDevices}
        allowPaging
        allowSorting
        rowSelected={handleDeviceRowSelected}
      >
        <ColumnsDirective>
          {deviceColumns.map((col, index) => (
            <ColumnDirective key={index} {...col} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit, Selection]} />
      </GridComponent>
    </div>
  );
};

export default Devices;
