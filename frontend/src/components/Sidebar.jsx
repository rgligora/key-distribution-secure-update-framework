import React, { useEffect, useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { MdApi, MdOutlineCancel } from 'react-icons/md';
import { TooltipComponent } from '@syncfusion/ej2-react-popups';
import { useStateContext } from '../contexts/ContextProvider';
import { fetchData } from '../api.js';

function Sidebar() {
    const { activeMenu, setActiveMenu, screenSize } = useStateContext();
    const activeLink = 'flex items-center gap-5 pl-4 pt-3 pb-2.5 rounded-lg text-black text-md m-2 bg-teal-600';
    const normalLink = 'flex items-center gap-5 pl-4 pt-3 pb-2.5 rounded-lg text-md text-gray-700 dark:text-gray-200 dark:hover:text-black hover:bg-light-gray m-2';

    const handleCloseSideBar = () => {
        if (activeMenu && screenSize <= 900) {
            setActiveMenu(false);
        }
    };

    const [companiesData, setCompaniesData] = useState([]);

    const adminId = '60a8cbbc-d38f-4ce2-a791-5f4dbd6ad400';

    useEffect(() => {
        const getAdminsCompanies = async () => {
            try {
                const adminData = await fetchData(`admin-users/${adminId}`);
                const companyDetails = await Promise.all(
                    adminData.companies.map(companyId => fetchData(`company/${companyId}`))
                );
                setCompaniesData(companyDetails);
            } catch (error) {
                console.error('Failed to load companies:', error);
            }
        };
        getAdminsCompanies();
    }, []);

    return (
        <div className='ml-3 h-screen md:overflow-hidden overflow-auto md:hover:overflow-auto pb-10'>
            {activeMenu && (
                <div>
                    <div className='flex justify-between items-center'>
                        <Link to="/" onClick={handleCloseSideBar} className='items-center gap-3 ml-3 mt-4 flex text-xl font-extrabold tracking-tight dark:text-white text-slate-900'>
                            <MdApi /> <span>KDSUF</span>
                        </Link>
                        <TooltipComponent content="Menu" position='BottomCenter'>
                            <button type='button' onClick={() => setActiveMenu((prevActiveMenu) => !prevActiveMenu)} className='text-xl rounded-full p-3 hover:bg-light-gray mt-4 mr-2 block'>
                                <MdOutlineCancel />
                            </button>
                        </TooltipComponent>
                    </div>
                    <div className='mt-10'>
                        {companiesData.map(company => (
                            <div className='pb-10' key={company.companyId}>
                                <p className='text-gray-400 m-3 mt-4 uppercase'>{company.name}</p>
                                <NavLink to={`/${company.companyId}/dashboard`} onClick={handleCloseSideBar} className={({ isActive }) => isActive ? activeLink : normalLink}>
                                    <span className='capitalize'>dashboard</span>
                                </NavLink>
                                <NavLink to={`/${company.companyId}/devices`} onClick={handleCloseSideBar} className={({ isActive }) => isActive ? activeLink : normalLink}>
                                    <span className='capitalize'>devices</span>
                                </NavLink>
                                <NavLink to={`/${company.companyId}/software`} onClick={handleCloseSideBar} className={({ isActive }) => isActive ? activeLink : normalLink}>
                                    <span className='capitalize'>software</span>
                                </NavLink>
                                <NavLink to={`/${company.companyId}/software-packages`} onClick={handleCloseSideBar} className={({ isActive }) => isActive ? activeLink : normalLink}>
                                    <span className='capitalize'>software packages</span>
                                </NavLink>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Sidebar;
