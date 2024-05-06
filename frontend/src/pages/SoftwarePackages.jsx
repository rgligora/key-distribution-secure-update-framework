import React, { useEffect, useState } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Selection, Toolbar, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button, Modal, NewSoftwarePackageForm } from '../components';
import { fetchDataWithRequestParams, createData } from '../api.js';

const SoftwarePackages = ({ companyId }) => {
  const [softwarePackagesData, setsoftwarePackagesData] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const getSoftwarePackages = async () => {
      try {
        const data = await fetchDataWithRequestParams('software-packages', { companyId });
        setsoftwarePackagesData(data);
      } catch (error) {
        console.error('Failed to load softwarePackages:', error);
      }
    };

    getSoftwarePackages();
  }, [companyId]);

  const handleNewSoftwarePackageSubmit = async (formData) => {
    try {
      const createdSoftwarePackage = await createData('software-packages', { ...formData, companyId });
      setsoftwarePackagesData([...softwarePackagesData, createdSoftwarePackage]);
    } catch (error) {
      console.error('Failed to create new software package:', error);
    }
  };

  const gridSWPackageStatus = (props) => {
    let statusBg = '';
    const status = props.status;

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
    { field: 'status', headerText: 'Status', template: gridSWPackageStatus, textAlign: 'Center', width: '120' },
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Software Packages" />
      <Button
        className="mb-5 p-3 bg-teal-600 text-white rounded-lg hover:bg-teal-700"
        onClick={() => setIsModalOpen(true)}
      >
        Create SW Package
      </Button>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Create Software Package">
        <NewSoftwarePackageForm onSubmit={handleNewSoftwarePackageSubmit} onClose={() => setIsModalOpen(false)} companyId={companyId} />
      </Modal>

      <GridComponent dataSource={softwarePackagesData} allowPaging allowSorting>
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
