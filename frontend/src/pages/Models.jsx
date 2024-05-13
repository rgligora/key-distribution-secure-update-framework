import React, { useEffect, useState } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button, Modal } from '../components';
import { fetchDataWithRequestParams } from '../api.js';

const Models = ({ companyId }) => {
  const [ModelData, setModelData] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedModel, setSelectedModel] = useState(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  const fetchModels = async () => {
    try {
      const data = await fetchDataWithRequestParams('models', { companyId });
      console.log(data)
      setModelData(data);
    } catch (error) {
      console.error('Failed to load Models:', error);
    }
  };

  useEffect(() => {
    fetchModels();
  }, [companyId]);

  const handleRowSelected = (args) => {
    const selectedData = args.data;
    const serialNumbers = selectedData.serialNos.map(serialNo => ({ serialNo }));
    setSelectedModel({ ...selectedData, serialNos: serialNumbers });
    setIsDetailModalOpen(true);

  };

  const ModelsGrid = [
    { field: 'modelId', headerText: 'Model ID', width: 'auto', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: 'auto', textAlign: 'Center' }
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Models" />
      <Button className="mb-5 p-3 bg-teal-600 text-white rounded-lg hover:bg-teal-700" onClick={() => setIsModalOpen(true)}>
        Add New Model
      </Button>
      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Add New Model">
      </Modal>
      <Modal isOpen={isDetailModalOpen} onClose={() => setIsDetailModalOpen(false)} title={`Serial Numbers for: ${selectedModel?.name}`}>
        <GridComponent dataSource={selectedModel?.serialNos} allowPaging={true} pageSettings={{ pageSize: 10 }}>
          <ColumnsDirective>
            <ColumnDirective field="serialNo" headerText="Serial Number" textAlign="Center" />
          </ColumnsDirective>
          <Inject services={[Page]} />
        </GridComponent>
      </Modal>
      <GridComponent id='gridcomp' dataSource={ModelData} allowPaging allowSorting rowSelected={handleRowSelected}>
        <ColumnsDirective>
          {ModelsGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Resize, Sort, ContextMenu, Filter, Page, Edit]} />
      </GridComponent>
    </div>
  );
};

export default Models;