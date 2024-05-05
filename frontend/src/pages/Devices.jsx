import React, { useEffect, useState } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button, Modal, NewDeviceForm } from '../components';
import { fetchData, createData, fetchDataWithRequestParams } from '../api.js';


const Devices = ({ companyId }) => {
  const [deviceData, setDeviceData] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchDevices = async () => {
    try {
      const data = await fetchDataWithRequestParams('devices', { companyId });
      setDeviceData(data);
    } catch (error) {
      console.error('Failed to load devices:', error);
    }
  };

  useEffect(() => {
    fetchDevices();
  }, [companyId]);

  const handleNewDeviceSubmit = async (formData) => {
    try {
      const createdDevice = await createData('devices', { ...formData, companyId });
      setDeviceData([...deviceData, createdDevice]);
    } catch (error) {
      console.error('Failed to create new device:', error);
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

  const devicesGrid = [
    { field: 'deviceId', headerText: 'Device ID', width: 'auto', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: 'auto', textAlign: 'Center' },
    { field: 'registrationDate', headerText: 'Registration Date', format: 'd.M.y', textAlign: 'Center', editType: 'datepicker', width: 'auto' },
    { field: 'lastUpdated', headerText: 'Last Updated', format: 'dd.MM.yyyy HH:mm', textAlign: 'Center', editType: 'datetimepicker', width: 'auto' },
    { field: 'status', headerText: 'Status', template: gridOrderStatus, textAlign: 'Center', width: '120' }
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Devices" />
      <Button
        className="mb-5 p-3 bg-teal-600 text-white rounded-lg hover:bg-teal-700"
        onClick={() => setIsModalOpen(true)}
      >
        Add New Device
      </Button>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Add New Device">
        <NewDeviceForm onSubmit={handleNewDeviceSubmit} onClose={() => setIsModalOpen(false)} />
      </Modal>

      <GridComponent id='gridcomp' dataSource={deviceData} allowPaging allowSorting>
        <ColumnsDirective>
          {devicesGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit]} />
      </GridComponent>
    </div>
  );
};

export default Devices;
