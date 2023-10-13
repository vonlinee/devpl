import { ReactMonacoEditor } from "@/components/editor/ReactMonacoEditor";
import { Button, Modal } from "antd";
import React, { Component } from "react";
import { createRef, useRef, useState } from "react";
import { render } from "react-dom";


interface IRefProps {
  childMethod: () => void;
}

/**
 * 测试
 */
const Test = () => {

  let text = ''

  let ref = useRef<ReactMonacoEditor>()

  const showModal = () => {
    console.log(ref.current?.getText());
  }

  const setText = () => {
    ref.current?.setText("hello world")
  }

  return (
    <>
      <Button onClick={showModal}>getText</Button>
      <Button onClick={setText}>setText</Button>
      <ReactMonacoEditor
        ref={ref}
        value={text}
        width={"100%"}
        height={"100%"}
      ></ReactMonacoEditor>
    </>
  );
}

Test.route = {
  [MENU_TITLE]: "test页面",
  [MENU_KEY]: "test",
  [MENU_PATH]: "/power/test",
};

export default Test;
