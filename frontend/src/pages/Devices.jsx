import React from 'react'
import {GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject} from '@syncfusion/ej2-react-grids'
import { Header } from '../components'

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