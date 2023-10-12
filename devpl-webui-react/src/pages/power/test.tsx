import { ReactMonacoEditor } from "@/components/editor/ReactMonacoEditor";
import { Button, Modal } from "antd";
import React, { Component } from "react";
import { createRef, useRef, useState } from "react";
import { render } from "react-dom";

/**
 * 测试
 */
const Test = () => {

  let text = ''

  const showModal = () => {
    console.log(text);
  }

  return (
    <>
      <Button onClick={showModal}>Show</Button>
      <ReactMonacoEditor
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
