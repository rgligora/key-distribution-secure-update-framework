import React, { useState } from 'react';
import { Button } from './';

const NewModelForm = ({ onSubmit, onClose, companyId }) => {
  const [formData, setFormData] = useState({
    name: '',
    companyId: companyId,
    deviceIds: []
  });
  const [deviceIdInput, setDeviceIdInput] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleDeviceIdInputChange = (e) => {
    setDeviceIdInput(e.target.value);
  };

  const handleAddDeviceId = () => {
    if (deviceIdInput.trim()) {
      setFormData({
        ...formData,
        deviceIds: [...formData.deviceIds, deviceIdInput.trim()]
      });
      setDeviceIdInput('');
    }
  };

  const handleRemoveDeviceId = (index) => {
    setFormData({
      ...formData,
      deviceIds: formData.deviceIds.filter((_, i) => i !== index)
    });
  };

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (file && file.type === 'text/plain') {
      const reader = new FileReader();
      reader.onload = (event) => {
        const text = event.target.result;
        const deviceIdsFromFile = text.split(',').map(id => id.trim()).filter(Boolean);
        setFormData({
          ...formData,
          deviceIds: [...formData.deviceIds, ...deviceIdsFromFile]
        });
      };
      reader.readAsText(file);
    } else {
      alert('Please upload a valid .txt file containing comma-separated device IDs.');
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
    onClose();
    setFormData({
      name: '',
      companyId: companyId,
      deviceIds: []
    });
  };

  return (
    <form onSubmit={handleFormSubmit}>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Name:</label>
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
        <label className="block text-sm font-medium mb-1">Device IDs (manually):</label>
        <div className="flex items-center mb-2">
          <input
            type="text"
            value={deviceIdInput}
            onChange={handleDeviceIdInputChange}
            className="flex-grow p-2 border rounded-md"
          />
          <Button type="button" onClick={handleAddDeviceId} className="ml-2">
            Add
          </Button>
        </div>
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Upload Device IDs (.txt):</label>
        <input
          type="file"
          accept=".txt"
          onChange={handleFileUpload}
          className="w-full p-2 border rounded-md"
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Current Device IDs:</label>
        <div
          className="w-full p-2 border rounded-md"
          style={{ maxHeight: '150px', overflowY: 'auto' }}
        >
          <ul className="list-disc pl-5">
            {formData.deviceIds.map((deviceId, index) => (
              <li key={index} className="flex items-center mb-1">
                <span className="mr-2">{deviceId}</span>
                <Button
                  type="button"
                  onClick={() => handleRemoveDeviceId(index)}
                  className="text-white-500 bg-red-600 p-1"
                >
                  Remove
                </Button>
              </li>
            ))}
          </ul>
        </div>
      </div>
      <Button type="submit">Add Model</Button>
    </form>
  );
};

export default NewModelForm;
