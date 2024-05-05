import React from 'react';

const Button = ({ type = 'button', className = '', children, ...props }) => (
  <button
    type={type}
    className={`px-4 py-2 bg-teal-600 text-white rounded-lg hover:bg-teal-700 ${className}`}
    {...props}
  >
    {children}
  </button>
);

export default Button;
