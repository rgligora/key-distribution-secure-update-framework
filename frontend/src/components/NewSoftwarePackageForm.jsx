import React, { useEffect, useState, useRef } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Selection, Inject } from '@syncfusion/ej2-react-grids';
import Button from './Button.jsx';
import { fetchDataWithRequestParams } from '../api.js';

const NewSoftwarePackageForm = ({ onSubmit, onClose, companyId }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    status: 'CREATED',
    includedSoftwareIds: [],
    modelIds: []
  });

  const [softwareData, setSoftwareData] = useState([]);
  const [modelData, setModelData] = useState([]);
  const softwareGridRef = useRef(null);
  const modelGridRef = useRef(null);

  useEffect(() => {
    const getSoftware = async () => {
      try {
        const data = await fetchDataWithRequestParams('software', { companyId });
        setSoftwareData(data);
      } catch (error) {
        console.error('Failed to load software:', error);
      }
    };

    const getModels = async () => {
      try {
        const data = await fetchDataWithRequestParams('models', { companyId });
        setModelData(data);
      } catch (error) {
        console.error('Failed to load models:', error);
      }
    };

    getSoftware();
    getModels();
  }, [companyId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    const selectedSoftwareRecords = softwareGridRef.current?.getSelectedRecords() || [];
    const selectedModelRecords = modelGridRef.current?.getSelectedRecords() || [];

    const selectedSoftwareIds = selectedSoftwareRecords.map((record) => record.softwareId);
    const selectedModelIds = selectedModelRecords.map((record) => record.modelId);

    onSubmit({
      ...formData,
      includedSoftwareIds: selectedSoftwareIds,
      modelIds: selectedModelIds,
    });
    onClose();
    setFormData({
      name: '',
      description: '',
      status: 'CREATED',
      includedSoftwareIds: [],
      modelIds: [],
    });
  };

  const softwareGrid = [
    { type: 'checkbox', width: '50' },
    { field: 'softwareId', headerText: 'Software ID', width: '250', textAlign: 'Center', isPrimaryKey: true },
    { field: 'name', headerText: 'Name', width: '150', textAlign: 'Center' },
    { field: 'version', headerText: 'Version', width: '150', textAlign: 'Center' },
    { field: 'uploadDate', headerText: 'Upload Date', format: 'd.M.y', textAlign: 'Center', width: '120' },
  ];

  const modelGrid = [
    { type: 'checkbox', width: '50' },
    { field: 'modelId', headerText: 'Model ID', width: '250', textAlign: 'Center', isPrimaryKey: true },
    { field: 'name', headerText: 'Name', width: '150', textAlign: 'Center' },
  ];

  return (
    <form onSubmit={handleFormSubmit}>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Package Name:</label>
        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleInputChange}
          className="w-full p-2 border rounded-md"
          required
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Description:</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleInputChange}
          className="w-full p-2 border rounded-md"
          required
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Select Software:</label>
        <GridComponent
          dataSource={softwareData}
          selectionSettings={{ type: 'Multiple', persistSelection: true }}
          ref={softwareGridRef}
          allowSelection={true}
        >
          <ColumnsDirective>
            {softwareGrid.map((item, index) => (
              <ColumnDirective key={index} {...item} />
            ))}
          </ColumnsDirective>
          <Inject services={[Selection]} />
        </GridComponent>
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Select Models:</label>
        <GridComponent
          dataSource={modelData}
          selectionSettings={{ type: 'Multiple', persistSelection: true }}
          ref={modelGridRef}
          allowSelection={true}
        >
          <ColumnsDirective>
            {modelGrid.map((item, index) => (
              <ColumnDirective key={index} {...item} />
            ))}
          </ColumnsDirective>
          <Inject services={[Selection]} />
        </GridComponent>
      </div>
      <Button type="submit">Create Software Package</Button>
    </form>
  );
};

export default NewSoftwarePackageForm;
