import React, { useEffect, useState } from 'react';
import { fetchData } from '../api.js';
import { FiSmartphone, FiPackage, FiDownload } from 'react-icons/fi';

const Dashboard = ({ companyId }) => {
    const [companyData, setCompanyData] = useState({
        devices: [],
        softwares: [],
        softwarePackages: []
    });

    useEffect(() => {
        const getCompany = async () => {
            try {
                const data = await fetchData(`company/${companyId}`);
                setCompanyData(data);
            } catch (error) {
                console.error('Failed to load company data:', error);
                setCompanyData({ devices: [], softwares: [], softwarePackages: [] });
            }
        };

        getCompany();
    }, [companyId]);

    return (
        <div className='mt-12'>
            <div className='flex flex-wrap lg:flex-nowrap justify-center'>
                <div className='bg-teal-600 dark:text-gray-200 dark:bg-secondary-dark-bg h-44 rounded-xl w-full p-8 pt-9 m-3'>
                    <div className='flex justify-between items-center'>
                        <div>
                            <p className='font-bold text-2xl'>{companyData.name}</p>
                        </div>
                    </div>
                </div>
                <div className='flex mt-3 mb-3 lg:flex-nowrap flex-wrap justify-center gap-2 items-center w-full'>
                    <StatisticsBlock icon={<FiSmartphone />} count={companyData.devices.length} title="Devices" />
                    <StatisticsBlock icon={<FiDownload />} count={companyData.softwares.length} title="Software" />
                    <StatisticsBlock icon={<FiPackage />} count={companyData.softwarePackages.length} title="SW Packages" />
                </div>
            </div>
        </div>
    );
};

const StatisticsBlock = ({ icon, count, title }) => (
    <div className='bg-white dark:text-gray-200 dark:bg-secondary-dark-bg w-1/4 md:w-56 p-4 pt-9 rounded-2xl'>
        <button type='button' className='text-2xl opacity-0.9 p-4 hover:drop-shadow-xl hover:bg-orange-400 text-white bg-teal-600' style={{ borderRadius: "50%" }}>
            {icon}
        </button>
        <p className='mt-3'>
            <span className='text-2xl font-semibold'>{count}</span>
        </p>
        <p className='md:text-s text-xs text-gray-400 mt-1'>{title}</p>
    </div>
);

export default Dashboard;
