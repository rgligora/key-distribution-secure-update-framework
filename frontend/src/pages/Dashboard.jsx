import React from 'react'
import {GoPrimitiveDot} from 'react-icons/go'
import {LineChart, PieChart} from '../components'
import { useStateContext } from '../contexts/ContextProvider'
import {FiSmartphone, FiPackage, FiDownload} from 'react-icons/fi';


function Dashboard() {
  return (
    <div className='mt-12'>
      <div className='flex flex-wrap lg:flex-nowrap justify-center'>
        <div className='bg-teal-600 dark:text-gray-200 dark:bg-secondary-dark-bg h-44 rounded-xl w-full p-8 pt-9 m-3'>
          <div className='flex justify-between items-center'>
            <div>
              <p className='font-bold text-2xl'>Company</p>
            </div>
          </div>
        </div>
        <div className='flex mt-3 mb-3 lg:flex-nowrap flex-wrap justify-center gap-2 items-center w-full'>
          <div className='bg-white dark:text-gray-200 dark:bg-secondary-dark-bg w-1/4 md:w-56 p-4 pt-9 rounded-2xl'>
            <button type='button' className='text-2xl opacity-0.9
                p-4
                hover:drop-shadow-xl
                hover:bg-orange-400
                text-white
                bg-teal-600'
                style={{borderRadius:"50%"}}>
                <FiSmartphone />
            </button>
            <p className='mt-3'>
              <span className='text-2xl font-semibold'>
                1000
              </span>
            </p>
            <p className='md:text-s text-xs text-gray-400 mt-1'>Devices</p>
          </div>
          <div className='bg-white dark:text-gray-200 dark:bg-secondary-dark-bg w-1/4 md:w-56 p-4 pt-9 rounded-2xl'>
            <button type='button' className='text-2xl opacity-0.9
                p-4
                hover:drop-shadow-xl
                hover:bg-orange-400
                text-white
                bg-teal-600'
                style={{borderRadius:"50%"}}>
                <FiDownload />
            </button>
            <p className='mt-3'>
              <span className='text-2xl font-semibold'>
                1000
              </span>
            </p>
            <p className='md:text-s text-xs text-gray-400 mt-1'>Software</p>
          </div>
          <div className='bg-white dark:text-gray-200 dark:bg-secondary-dark-bg w-1/4 md:w-56 p-4 pt-9 rounded-2xl'>
            <button type='button' className='text-2xl opacity-0.9
                p-4
                hover:drop-shadow-xl
                hover:bg-orange-400
                text-white
                bg-teal-600'
                style={{borderRadius:"50%"}}>
                <FiPackage />
            </button>
            <p className='mt-3'>
              <span className='text-2xl font-semibold'>
                1000
              </span>
            </p>
            <p className='md:text-s w-20 text-xs text-gray-400 mt-1'>SW Packages</p>
          </div>
        </div>
        
      </div>
    </div>
  )
}

export default Dashboard