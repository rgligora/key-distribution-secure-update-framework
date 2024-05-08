import React, {useEffect} from 'react'
import {AiOutlineMenu} from 'react-icons/ai'
import { MdKeyboardArrowDown } from 'react-icons/md'
import { TooltipComponent } from '@syncfusion/ej2-react-popups'
import { useStateContext } from '../contexts/ContextProvider'
import avatar from '../data/avatar.jpg'
import {FiSettings} from 'react-icons/fi';


const NavButton = ({ title, customFunction, icon, color, dotColor }) => (
  <TooltipComponent content={title} position="BottomCenter">
    <button
      type="button"
      onClick={customFunction}
      style={{ color }}
      className="relative text-xl rounded-full p-3 hover:bg-light-gray"
    >
      <span style={{ background: dotColor }} className="absolute inline-flex rounded-full h-2 w-2 right-2 top-2" />
      {icon}
    </button>
  </TooltipComponent>
);

function Navbar() {
  const { activeMenu, setActiveMenu, isClicked, handleClick, screenSize, setScreenSize, adminData } = useStateContext();
  console.log(adminData.username)

  useEffect(() => {
    const handleResize = () => setScreenSize(window.innerWidth);
    window.addEventListener("resize", handleResize);
    handleResize();

    return () => window.removeEventListener("resize", handleResize);
  }, [setScreenSize]);

  useEffect(() => {
    if (screenSize <= 900) {
      setActiveMenu(false);
    } else {
      setActiveMenu(true);
    }
  }, [screenSize, setActiveMenu]);

  return (
    <div className="flex justify-between p-2 md:mx-6 relative">
      <NavButton title="Menu" customFunction={() => setActiveMenu((prev) => !prev)} color="teal" icon={<AiOutlineMenu />} />
      <TooltipComponent content="Profile" position="BottomCenter">
        <div className="flex items-center gap-2 cursor-pointer p-1 rounded-lg">
          <img alt="avatar" className="rounded-full w-10 hover:bg-light-gray" src={avatar} />
          <p>
            <span className="text-gray-400 text-14">Hi, </span>
            <span className="text-gray-400 font-bold ml-1 text-14">{adminData.username}</span>
          </p>
          <MdKeyboardArrowDown className="text-gray-400 text-14" />
          <p>
            <TooltipComponent className="ml-8" content="Settings" position="BottomCenter">
              <button
                type="button"
                className="text-l p-3 hover:drop-shadow-xl hover:bg-orange-400 text-white bg-teal-600"
                style={{ borderRadius: "50%" }}
              >
                <FiSettings />
              </button>
            </TooltipComponent>
          </p>
        </div>
      </TooltipComponent>
    </div>
  );
}

export default Navbar;
