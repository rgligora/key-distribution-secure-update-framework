import React from 'react'
import {GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject} from '@syncfusion/ej2-react-grids'
import { Header } from '../components'

const gridOrderStatus = (props) => {
  let statusBg = '';
  switch (props.Status.toLowerCase()) {
    case 'active':
      statusBg = '#03C9D7';
      break;
    case 'inactive':
      statusBg = '#FF5C8E';
      break;
    case 'pending':
      statusBg = 'orange';
      break;
    case 'updating':
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
      {props.Status}
    </button>
  );
};

const deviceData = [
  {
    DeviceId: 10248,
    DeviceName: 'Nest Hub',

    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'pending',
    StatusBg: '#FB9678'
  },
  {
    DeviceId: 345653,
    DeviceName: 'Home Max',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'updating',
    StatusBg: '#8BE78B'
  },
  {
    DeviceId: 390457,
    DeviceName: 'Nest Wifi point',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'active',
    StatusBg: '#03C9D7'
  },
  {
    DeviceId: 893486,
    DeviceName: 'Nest Audio',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: '#FF5C8E'
  },
  {
    DeviceId: 748975,
    DeviceName: 'Chromecast 4K',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: 'red'
  },
  {
    DeviceId: 10248,
    DeviceName: 'Nest Hub',

    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'pending',
    StatusBg: '#FB9678'
  },
  {
    DeviceId: 345653,
    DeviceName: 'Home Max',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'updating',
    StatusBg: '#8BE78B'
  },
  {
    DeviceId: 390457,
    DeviceName: 'Nest Wifi point',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'active',
    StatusBg: '#03C9D7'
  },
  {
    DeviceId: 893486,
    DeviceName: 'Nest Audio',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: '#FF5C8E'
  },
  {
    DeviceId: 748975,
    DeviceName: 'Chromecast 4K',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: 'red'
  },
  {
    DeviceId: 10248,
    DeviceName: 'Nest Hub',

    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'pending',
    StatusBg: '#FB9678'
  },
  {
    DeviceId: 345653,
    DeviceName: 'Home Max',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'updating',
    StatusBg: '#8BE78B'
  },
  {
    DeviceId: 390457,
    DeviceName: 'Nest Wifi point',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'active',
    StatusBg: '#03C9D7'
  },
  {
    DeviceId: 893486,
    DeviceName: 'Nest Audio',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: '#FF5C8E'
  },
  {
    DeviceId: 748975,
    DeviceName: 'Chromecast 4K',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: 'red'
  },
  {
    DeviceId: 10248,
    DeviceName: 'Nest Hub',

    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'pending',
    StatusBg: '#FB9678'
  },
  {
    DeviceId: 345653,
    DeviceName: 'Home Max',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'updating',
    StatusBg: '#8BE78B'
  },
  {
    DeviceId: 390457,
    DeviceName: 'Nest Wifi point',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'active',
    StatusBg: '#03C9D7'
  },
  {
    DeviceId: 893486,
    DeviceName: 'Nest Audio',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: '#FF5C8E'
  },
  {
    DeviceId: 748975,
    DeviceName: 'Chromecast 4K',
    RegistrationDate: new Date(2020, 5, 30),
    LastUpdated: new Date(Date.now()),
    FirmwareVersion: '1.0',
    Status: 'inactive',
    StatusBg: 'red'
  }
]

const ordersGrid = [
  {
    field: 'DeviceId', // Correct field names should match case sensitivity
    headerText: 'Device ID',
    width: '120',
    textAlign: 'Center',
  },
  {
    field: 'DeviceName',
    headerText: 'Name',
    width: '150',
    textAlign: 'Center',
  },
  {
    field: 'RegistrationDate',
    headerText: 'Registration Date',
    format: 'd.M.y',
    textAlign: 'Center',
    editType: 'datepicker',
    width: '150',
  },
  {
    field: 'LastUpdated',
    headerText: 'Last Updated',
    format: 'dd.MM.yyyy HH:mm',
    textAlign: 'Center',
    editType: 'datetimepicker',
    width: '150',
  },
  {
    field: 'FirmwareVersion',
    headerText: 'Firmware Version',
    textAlign: 'Center',
    width: '150',
  },
  {
    headerText: 'Status',
    template: gridOrderStatus,
    field: 'Status',
    textAlign: 'Center',
    width: '120',
  }
];


function Devices() {
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
}


export default Devices