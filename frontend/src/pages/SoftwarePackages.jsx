import React, { useEffect, useState } from 'react'
import {GridComponent, ColumnsDirective, ColumnDirective, Selection, Toolbar, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject} from '@syncfusion/ej2-react-grids'
import { Header } from '../components'
import { fetchData, fetchDataWithRequestParams } from '../api.js';




const SoftwarePackages = ({companyId}) => {
  const [softwarePackagesData, setsoftwarePackagesData] = useState([]);

  useEffect(() => {
    const getSoftwarePackages = async () => {
      try {
        const data = await fetchDataWithRequestParams('software-packages', {companyId});
        setsoftwarePackagesData(data);
      } catch (error) {
        console.error('Failed to load softwarePackages:', error);
      }
    };

    getSoftwarePackages();
  }, [companyId]);

  const gridSWPackageStatus = (props) => {
    let statusBg = '';
    const status = props.status;  
    console.log(status)
  
    switch (status) {
      case 'CREATED':
        statusBg = '#03C9D7';
        break;
      case 'DEPRECATED':
        statusBg = '#FF5C8E';
        break;
      case 'AVAILABLE':
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


  const softwarePackagesGrid = [
    { field: 'softwarePackageId', headerText: 'SoftwarePackage ID', width: 'auto', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: '150', textAlign: 'Center' },
    { field: 'creationDate', headerText: 'Creation Date', format: 'd.M.y', textAlign: 'Center', editType: 'datepicker', width: '120' },
    { field: 'description', headerText: 'Description', width: '150', textAlign: 'Center' },
    { field: 'status', headerText: 'Status', template: gridSWPackageStatus, textAlign: 'Center', width: '120' }
   ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Software Packages"/>
      <GridComponent dataSource={softwarePackagesData} allowPaging allowSorting toolbar={['Roll out SW Package']}>
        <ColumnsDirective>
          {softwarePackagesGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit, Selection, Toolbar]} />
      </GridComponent>
    </div>
  );
};

export default SoftwarePackages;
