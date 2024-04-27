import React from 'react'
import { Link, NavLink } from 'react-router-dom'
import { MdApi } from 'react-icons/md'
import { MdOutlineCancel } from 'react-icons/md'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'
import { useStateContext } from '../contexts/ContextProvider'

function Sidebar() {
  const activeLink = 'flex items-center gap-5 pl-4 pt-3 pb-2.5 rounded-lg text-white text-md m-2';
  const normalLink = 'flex items-center gap-5 pl-4 pt-3 pb-2.5 rounded-lg text-md text-gray-700 dark:text-gray-200 dark:hover:text-black hover:bg-light-gray m-2';

  const {activeMenu, setActiveMenu, screeSize} = useStateContext();

  const handleCloseSideBar = () => {
    if(activeMenu && screeSize <= 900){
      setActiveMenu(false)
    }
  }

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
            <div className='pb-10' key="company1">
              <p className='text-gray-400 m-3 mt-4 uppercase'>Company 1</p>
              <NavLink 
                to="/dashboard"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>dashboard</span>
              </NavLink>
              <NavLink 
                to="/devices"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>devices</span>
              </NavLink>
              <NavLink 
                to="/software"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software</span>
              </NavLink>
              <NavLink 
                to="/softwarePackages"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software Packages</span>
              </NavLink>
            </div>
            <div className='pb-10' key="company2">
              <p className='text-gray-400 m-3 mt-4 uppercase'>Company 2</p>
              <NavLink 
                to="/dashboard"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>dashboard</span>
              </NavLink>
              <NavLink 
                to="/devices"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>devices</span>
              </NavLink>
              <NavLink 
                to="/software"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software</span>
              </NavLink>
              <NavLink 
                to="/softwarePackages"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software Packages</span>
              </NavLink>
            </div>
            <div className='pb-10' key="company3">
              <p className='text-gray-400 m-3 mt-4 uppercase'>Company 3</p>
              <NavLink 
                to="/dashboard"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>dashboard</span>
              </NavLink>
              <NavLink 
                to="/devices"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>devices</span>
              </NavLink>
              <NavLink 
                to="/software"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software</span>
              </NavLink>
              <NavLink 
                to="/softwarePackages"
                onClick={handleCloseSideBar}
                className={({ isActive }) => isActive ? activeLink : normalLink}>
                  <span className='capitalize'>software Packages</span>
              </NavLink>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Sidebar
