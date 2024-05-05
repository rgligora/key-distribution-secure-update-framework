import React, { useState } from 'react';
import { Button } from './';

const NewDeviceForm = ({ onSubmit, onClose }) => {
  const [formData, setFormData] = useState({
    name: '',
    registrationDate: '',
    lastUpdated: '',
    status: 'ACTIVE'
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
    onClose();
    setFormData({
      name: '',
      registrationDate: '',
      lastUpdated: '',
      status: 'ACTIVE'
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
        <label className="block text-sm font-medium mb-1">Registration Date:</label>
        <input
          type="date"
          name="registrationDate"
          value={formData.registrationDate}
          onChange={handleInputChange}
          className="w-full p-2 border rounded-md"
          required
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Last Updated:</label>
        <input
          type="datetime-local"
          name="lastUpdated"
          value={formData.lastUpdated}
          onChange={handleInputChange}
          className="w-full p-2 border rounded-md"
          required
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Status:</label>
        <select
          name="status"
          value={formData.status}
          onChange={handleInputChange}
          className="w-full p-2 border rounded-md"
        >
          <option value="ACTIVE">ACTIVE</option>
          <option value="INACTIVE">INACTIVE</option>
          <option value="UPDATE_PENDING">UPDATE_PENDING</option>
          <option value="UPDATING">UPDATING</option>
        </select>
      </div>
      <Button type="submit">Add Device</Button>
    </form>
  );
};

export default NewDeviceForm;
