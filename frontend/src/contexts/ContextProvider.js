import React, { createContext, useContext, useState, useEffect } from "react";
import { fetchData } from "../api.js";

const StateContext = createContext();

const initialState = {
  userProfile: false,
};

export const ContextProvider = ({ children }) => {
  const [activeMenu, setActiveMenu] = useState(true);
  const [isClicked, setIsClicked] = useState(initialState);
  const [screenSize, setScreenSize] = useState(undefined);

  const handleClick = (clicked) => setIsClicked({ ...initialState, [clicked]: true });
  const [adminData, setAdminData] = useState({ companies: [] });

  useEffect(() => {
    const getAdminData = async () => {
      const adminId = "b668dead-1c36-44df-852d-a6f0ae4986eb"; // Update as needed
      try {
        const data = await fetchData(`admin-users/${adminId}`);
        const companyDetails = await Promise.all(
          data.companies.map((companyId) => fetchData(`company/${companyId}`))
        );
        
        setAdminData({
          ...data,
          companies: companyDetails,
        });
      } catch (error) {
        console.error("Failed to load admin data:", error);
      }
    };
    getAdminData();
  }, []);

  return (
    <StateContext.Provider
      value={{
        activeMenu,
        setActiveMenu,
        isClicked,
        setIsClicked,
        handleClick,
        screenSize,
        setScreenSize,
        adminData,
        setAdminData,
      }}
    >
      {children}
    </StateContext.Provider>
  );
};

export const useStateContext = () => useContext(StateContext);
