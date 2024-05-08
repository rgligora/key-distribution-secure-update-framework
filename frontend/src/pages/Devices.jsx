import React, { useEffect, useState, useRef } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Selection, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button } from '../components';
import { fetchDataWithRequestParams, updateData } from '../api.js';

const Devices = ({ companyId }) => {
  const [confirmedDevices, setConfirmedDevices] = useState([]);
  const [pendingDevices, setPendingDevices] = useState([]);
  const confirmedGridRef = useRef(null);
  const pendingGridRef = useRef(null);

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

  const gridOrderStatus = (props) => {
    let statusBg = '';
    const status = props.status;

    switch (status) {
      case 'ACTIVE':
        statusBg = '#03C9D7';
        break;
      case 'INACTIVE':
        statusBg = '#FF5C8E';
        break;
      case 'UPDATE_PENDING':
        statusBg = 'orange';
        break;
      case 'UPDATING':
        statusBg = 'green';
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
        <button
          onClick={() => confirmDevice(props.deviceId)}
          className="bg-orange-400 text-white p-2 rounded-lg hover:bg-orange-500"
        >
          Confirm
        </button>
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
      <GridComponent
        ref={confirmedGridRef}
        key={`confirmed-devices-${confirmedDevices.length}`}
        dataSource={confirmedDevices}
        allowPaging
        allowSorting
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
