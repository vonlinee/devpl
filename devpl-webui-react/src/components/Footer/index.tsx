import React, {useEffect} from 'react';

const Footer: React.FC = () => {

  useEffect(() => {
    let elements = document.getElementsByClassName("ant-layout-footer")
    console.log("Footer => ", elements)
  }, [])

  return (
    <></>
  );
};

export default Footer;
