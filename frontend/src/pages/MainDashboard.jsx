import React from 'react'
import {GoPrimitiveDot} from 'react-icons/go'
import {LineChart, PieChart} from '../components'
import { useStateContext } from '../contexts/ContextProvider'


function MainDashboard() {
  return (
    <div className='mt-12 bg-blue-500'>
      <div className='flex flex-wrap lg:flex-nowrap justify-center'>
        <div className='dark:text-gray-200 dark:bg-secondary-dark-bg h-44 rounded-xl w-full lg:w-80 p-8 pt-9 m-3 bg-hero-pattern bg-no-repeat bg-cover bg-center'>

        </div>
      </div>
    </div>
  )
}

export default MainDashboard