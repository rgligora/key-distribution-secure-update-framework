import React, {useEffect} from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom'; 
import {FiSettings} from 'react-icons/fi';
import { TooltipComponent } from '@syncfusion/ej2-react-popups';

import {Button, ChartsHeader, Footer, Header, Navbar, Sidebar, ThemeSettings, UserProfile, LineChart, PieChart} from './components';
import {MainDashboard, Dashboard, Devices, Software, SoftwarePackages, Line, Pie} from './pages'
import './App.css';

import { useStateContext } from './contexts/ContextProvider';

const App = () => {
    const {activeMenu} = useStateContext();

    return (
    <div>
        <BrowserRouter>
        <div className="flex realtive dark:bg-main-dark-bg">
            <div className="fixed right-4 bottom-4" style={{zIndex: '1000'}}>
                <TooltipComponent content="Settings" position="Top">
                    <button type='button' className='text-3xl p-3 
                        hover:drop-shadow-xl
                        hover:bg-orange-400
                        text-white
                        bg-teal-600'
                        style={{borderRadius:"50%"}}>
                        <FiSettings />
                    </button>
                </TooltipComponent>
            </div>
            {activeMenu ? 
                (<div className="w-72 fixed sidebar dark:bg-secondary-dark-bg bg-white">
                    <Sidebar />
                </div>) : 
                (<div className='w-0 dark:bg-secondary-dark-bg'>
                    <Sidebar />
                </div>)}
            <div className={activeMenu ? 'dark:bg-main-bg bg-main-bg min-h-screen w-full md:ml-72' : 'dark:bg-main-bg bg-main-bg min-h-screen w-full flex-2'}>
                <div className='fixed md:static bg-main-bg dark:bg-main-dark-bg navbar w-full'>
                    <Navbar />
                </div>
                <div>
                    <Routes>
                        {/*Dashboards*/}
                        <Route path='/' element={<MainDashboard />} />
                        <Route path='company1/dashboard' element={<Dashboard />} />
                        <Route path='company2/dashboard' element={<Dashboard />} />
                        <Route path='company3/dashboard' element={<Dashboard />} />
                        
                        {/* TODO: add routes for all companies for all apges like in Dashboards */}
                        {/*Pages*/}
                        <Route path='/devices' element={<Devices />} />
                        <Route path='/software' element={<Software />} />
                        <Route path='/software-packages' element={<SoftwarePackages />} />

                        {/*Charts*/}
                        <Route path='/line' element={<Line />} />
                        <Route path='/pie' element={<Pie />} />
                        
                    </Routes>
                </div>
            </div>
        </div>
        </BrowserRouter>
    </div>
  )
}

export default App