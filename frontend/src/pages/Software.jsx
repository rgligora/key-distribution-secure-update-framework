import React, { useEffect, useState } from 'react'
import {GridComponent, ColumnsDirective, ColumnDirective, Selection, Toolbar, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject} from '@syncfusion/ej2-react-grids'
import { Header } from '../components'
import { fetchData, fetchDataWithRequestParams } from '../api.js';




const Software = ({companyId}) => {
  const [softwareData, setsoftwareData] = useState([]);

  useEffect(() => {
    const getSoftware = async () => {
      try {
        const data = await fetchDataWithRequestParams('software', {companyId});
        setsoftwareData(data);
      } catch (error) {
        console.error('Failed to load software:', error);
      }
    };

    getSoftware();
  }, []);


  const ordersGrid = [
    { type: 'checkbox', width: '50' },
    { field: 'softwareId', headerText: 'Software ID', width: '250', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: '150', textAlign: 'Center' },
    { field: 'version', headerText: 'Version', width: '150', textAlign: 'Center' },
    { field: 'uploadDate', headerText: 'Upload Date', format: 'd.M.y', textAlign: 'Center', editType: 'datepicker', width: '120' }
   ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Software"/>
      <GridComponent dataSource={softwareData} allowPaging allowSorting toolbar={['Create SW Package']}>
        <ColumnsDirective>
          {ordersGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit, Selection, Toolbar]} />
      </GridComponent>
    </div>
  );
};

export default Software;
