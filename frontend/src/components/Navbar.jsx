import React, {useEffect} from 'react'
import {AiOutlineMenu} from 'react-icons/ai'
import { MdKeyboardArrowDown } from 'react-icons/md'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'
import { useStateContext } from '../contexts/ContextProvider'
import {UserProfile} from '.'
import avatar from '../data/avatar.jpg'

const NavButton = ({title, customFunction, icon, color, dotColor}) => (
  <TooltipComponent content={title} position='BottomCenter'>
    <button type='button' onClick={customFunction} style={{color}} className='relative text-xl rounded-full p-3 hover:bg-light-gray'>
      <span style={{background: dotColor}} className="absolute inline-flex rounded-full h-2 w-2 right-2 top-2">
        {icon}
      </span>
    </button>
  </TooltipComponent>
)


function Navbar() {

  const {activeMenu, setActiveMenu, isClicked, setisClicked, handleClick} = useStateContext();

  return (
    <div className='flex justify-between p-2 md:mx-6 relative'>
      <NavButton title="Menu" customFunction={() => setActiveMenu((prevActiveMenu) => !prevActiveMenu)} color="teal" icon={<AiOutlineMenu/>}/>
      <TooltipComponent content="Profile" position='BottomCnter'>
        <div onClick={() => handleClick('userProfile')} className='flex items-center gap-2 cursor-pointer p-1 rounded-lg'>
          <img alt='avatar' className="rounded-full w-8 hover:bg-light-gray" src={avatar}/>
          <p>
            <span className='text-gray-400 text-14'>Hi, </span>
            <span className='text-gray-400 font-bold ml-1 text-14'>Admin</span>
          </p>
          <MdKeyboardArrowDown className='text-gray-400 text-14'/>
        </div>
    </TooltipComponent>
    {isClicked.userProfile && <UserProfile />}
    </div>
    
  )
}

export default Navbar