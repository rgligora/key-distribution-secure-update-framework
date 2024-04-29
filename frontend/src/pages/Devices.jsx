import React, { useEffect, useState } from 'react'
import {GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject} from '@syncfusion/ej2-react-grids'
import { Header } from '../components'
import { fetchData, fetchDataWithRequestParams } from '../api.js';



const Devices = ({companyId}) => {
  const [deviceData, setDeviceData] = useState([]);

  useEffect(() => {
    const getDevices = async () => {
      try {
        const data = await fetchDataWithRequestParams('devices', { companyId });
        setDeviceData(data);
      } catch (error) {
        console.error('Failed to load devices:', error);
      }
    };

    getDevices();
  }, []);

  const gridDeviceStatus = (props) => {
  let statusBg = '';
  const status = props.status;  
  console.log(status)

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

  const ordersGrid = [
    { field: 'deviceId', headerText: 'Device ID', width: 'auto', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: 'auto', textAlign: 'Center' },
    { field: 'registrationDate', headerText: 'Registration Date', format: 'd.M.y', textAlign: 'Center', editType: 'datepicker', width: 'auto' },
    { field: 'lastUpdated', headerText: 'Last Updated', format: 'dd.MM.yyyy HH:mm', textAlign: 'Center', editType: 'datetimepicker', width: 'auto' },
    { field: 'status', headerText: 'Status', template: gridDeviceStatus, textAlign: 'Center', width: '120' }
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Devices"/>
      <GridComponent id='gridcomp' dataSource={deviceData} allowPaging allowSorting>
        <ColumnsDirective>
          {ordersGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit]} />
      </GridComponent>
    </div>
  );
};

export default Devices;
