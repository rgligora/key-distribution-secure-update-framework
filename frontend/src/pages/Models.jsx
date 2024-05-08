import React, { useEffect, useState } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Resize, Sort, ContextMenu, Filter, Page, Edit, Inject } from '@syncfusion/ej2-react-grids';
import { Header, Button, Modal, NewModelForm } from '../components';
import { fetchData, createData, fetchDataWithRequestParams } from '../api.js';


const Models = ({ companyId }) => {
  const [ModelData, setModelData] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchModels = async () => {
    try {
      const data = await fetchDataWithRequestParams('models', { companyId });
      setModelData(data);
    } catch (error) {
      console.error('Failed to load Models:', error);
    }
  };

  useEffect(() => {
    fetchModels();
  }, [companyId]);

  const handleNewModelsubmit = async (formData) => {
    try {
      const createdModel = await createData('models', { ...formData, companyId });
      setModelData([...ModelData, createdModel]);
    } catch (error) {
      console.error('Failed to create new Model:', error);
    }
  };


  const ModelsGrid = [
    { field: 'modelId', headerText: 'Model ID', width: 'auto', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: 'auto', textAlign: 'Center' }
  ];

  return (
    <div className='m-2 md:m-10 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category="Page" title="Models" />
      <Button
        className="mb-5 p-3 bg-teal-600 text-white rounded-lg hover:bg-teal-700"
        onClick={() => setIsModalOpen(true)}
      >
        Add New Model
      </Button>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Add New Model">
        <NewModelForm onSubmit={handleNewModelsubmit} onClose={() => setIsModalOpen(false)} />
      </Modal>

      <GridComponent id='gridcomp' dataSource={ModelData} allowPaging allowSorting>
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