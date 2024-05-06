import React, { useEffect, useState } from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Selection, Inject } from '@syncfusion/ej2-react-grids';
import Button from './Button.jsx';
import { fetchDataWithRequestParams } from '../api.js';

const NewSoftwarePackageForm = ({ onSubmit, onClose, companyId }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    status: 'CREATED',
    includedSoftwareIds: [],
  });

  const [softwareData, setSoftwareData] = useState([]);
  const [selectedSoftwareIds, setSelectedSoftwareIds] = useState([]);

  useEffect(() => {
    const getSoftware = async () => {
      try {
        const data = await fetchDataWithRequestParams('software', { companyId });
        setSoftwareData(data);
      } catch (error) {
        console.error('Failed to load software:', error);
      }
    };

    getSoftware();
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
    onSubmit({ ...formData, includedSoftwareIds: selectedSoftwareIds });
    onClose();
    setFormData({
      name: '',
      description: '',
      status: 'CREATED',
      includedSoftwareIds: [],
    });
  };

  const handleSelectionChange = ({ selectedRecords }) => {
    const ids = selectedRecords.map((record) => record.softwareId);
    setSelectedSoftwareIds(ids);
  };

  const softwareGrid = [
    { type: 'checkbox', width: '50' },
    { field: 'softwareId', headerText: 'Software ID', width: '250', textAlign: 'Center' },
    { field: 'name', headerText: 'Name', width: '150', textAlign: 'Center' },
    { field: 'version', headerText: 'Version', width: '150', textAlign: 'Center' },
    { field: 'uploadDate', headerText: 'Upload Date', format: 'd.M.y', textAlign: 'Center', width: '120' },
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
          change={handleSelectionChange}
        >
          <ColumnsDirective>
            {softwareGrid.map((item, index) => (
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
