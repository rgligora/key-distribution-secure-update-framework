import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const fetchData = async (endpoint) => {
  try {
    const response = await axios.get(`${API_URL}/${endpoint}`);
    return response.data;
  } catch (error) {
    console.error('Fetching data error:', error);
    throw error;
  }
};

export const fetchDataWithRequestParams = async (endpoint, params = {}) => {
  try {
    const response = await axios.get(`${API_URL}/${endpoint}`, { params });
    return response.data;
  } catch (error) {
    console.error('Fetching data error:', error);
    throw error;
  }
};


export const createData = async (endpoint, data) => {
  try {
    const response = await axios.post(`${API_URL}/${endpoint}`, data);
    return response.data;
  } catch (error) {
    console.error('Creating data error:', error);
    throw error;
  }
};

export const updateData = async (endpoint, data) => {
  try {
    const response = await axios.put(`${API_URL}/${endpoint}`, data);
    return response.data;
  } catch (error) {
    console.error('Updating data error:', error);
    throw error;
  }
};

export const deleteData = async (endpoint) => {
  try {
    const response = await fetch(`${API_URL}/${endpoint}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      }
    });
    const data = await response.text();
    if (!response.ok) {
      throw new Error(data || "Unknown error occurred");
    }
    return data;
  } catch (error) {
    console.error('Error during API request:', error);
    throw error;
  }
};
