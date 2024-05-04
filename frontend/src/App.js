import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom'; 
import { fetchData } from './api.js';

import {Button, ChartsHeader, Footer, Header, Navbar, Sidebar, ThemeSettings, UserProfile, LineChart, PieChart} from './components';
import {MainDashboard, Dashboard, Devices, Software, SoftwarePackages, Line, Pie} from './pages';
import './App.css';

import { useStateContext } from './contexts/ContextProvider';

const App = () => {
    const { activeMenu } = useStateContext();
    const [AdminData, setAdminData] = useState({ companies: [] });

    const adminId = '81541d35-b894-4f6f-8d90-333912fba62a';

    useEffect(() => {
        const getAdmins = async () => {
          try {
            const data = await fetchData(`admin-users/${adminId}`);
            setAdminData(data);
          } catch (error) {
            console.error('Failed to load admins:', error);
          }
        };
    
        getAdmins();
    }, []);

    return (
        <div>
            <BrowserRouter>
                <div className="flex relative dark:bg-main-dark-bg">
                    {activeMenu ? (
                        <div className="w-72 fixed sidebar dark:bg-secondary-dark-bg bg-white">
                            <Sidebar />
                        </div>
                    ) : (
                        <div className='w-0 dark:bg-secondary-dark-bg'>
                            <Sidebar />
                        </div>
                    )}
                    <div className={activeMenu ? 'dark:bg-main-bg bg-main-bg min-h-screen w-full md:ml-72' : 'dark:bg-main-bg bg-main-bg min-h-screen w-full flex-2'}>
                        <div className='fixed md:static bg-main-bg dark:bg-main-dark-bg navbar w-full'>
                            <Navbar />
                        </div>
                        <div>
                            <Routes>
                                <Route path='/' element={<MainDashboard />} />

                                {AdminData.companies.map((companyId) => (
                                    <React.Fragment key={companyId}>
                                        <Route path={`${companyId}/dashboard`} element={<Dashboard companyId={companyId}/>} />
                                        <Route path={`${companyId}/devices`} element={<Devices companyId={companyId} />} />
                                        <Route path={`${companyId}/software`} element={<Software companyId={companyId} />} />
                                        <Route path={`${companyId}/software-packages`} element={<SoftwarePackages companyId={companyId} />} />
                                    </React.Fragment>
                                ))}

                                {/* Charts */}
                                <Route path='/line' element={<Line />} />
                                <Route path='/pie' element={<Pie />} />
                            </Routes>
                        </div>
                    </div>
                </div>
            </BrowserRouter>
        </div>
    );
}

export default App;
