import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Button, ChartsHeader, Footer, Header, Navbar, Sidebar, ThemeSettings, UserProfile, LineChart, PieChart } from './components';
import { MainDashboard, Dashboard, Devices, Software, SoftwarePackages, Line, Pie } from './pages';
import { useStateContext } from "./contexts/ContextProvider";
import './App.css';

const App = () => {
  const { activeMenu, adminData } = useStateContext();

  return (
    <div>
      <BrowserRouter>
        <div className="flex relative dark:bg-main-dark-bg">
          {activeMenu ? (
            <div className="w-72 fixed sidebar dark:bg-secondary-dark-bg bg-white">
              <Sidebar />
            </div>
          ) : (
            <div className="w-0 dark:bg-secondary-dark-bg">
              <Sidebar />
            </div>
          )}
          <div className={activeMenu ? "dark:bg-main-bg bg-main-bg min-h-screen w-full md:ml-72" : "dark:bg-main-bg bg-main-bg min-h-screen w-full flex-2"}>
            <div className="fixed md:static bg-main-bg dark:bg-main-dark-bg navbar w-full">
              <Navbar />
            </div>
            <div>
              <Routes>
                <Route path="/" element={<MainDashboard />} />
                {adminData.companies.map((company) => (
                  <React.Fragment key={company.companyId}>
                    <Route path={`${company.companyId}/dashboard`} element={<Dashboard companyId={company.companyId} />} />
                    <Route path={`${company.companyId}/devices`} element={<Devices companyId={company.companyId} />} />
                    <Route path={`${company.companyId}/software`} element={<Software companyId={company.companyId} />} />
                    <Route path={`${company.companyId}/software-packages`} element={<SoftwarePackages companyId={company.companyId} />} />
                  </React.Fragment>
                ))}
                <Route path="/line" element={<Line />} />
                <Route path="/pie" element={<Pie />} />
              </Routes>
            </div>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
