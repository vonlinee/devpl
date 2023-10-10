import ReactMonacoEditor from "@/components/editor/ReactMonacoEditor";
import { Button, Modal } from "antd";
import React, { Component } from "react";
import { createRef, useRef, useState } from "react";
import { render } from "react-dom";

class Test extends React.Component {
  isModalOpen: boolean;
  static route: { title: string; key: string; path: string; };

  childRef = createRef();
  

  constructor(props: {} | Readonly<{}>) {
    super(props);
    this.isModalOpen = false
    this.state = {
      text: null
    }
  }

  componentDidMount(): void {
      console.log(this);
  }

  showModal() {
    console.log(this);
  }

  handleOk() {

  }

  handleCancel() {

  }

  render() {
    return (
      <>
        <Button onClick={this.showModal}>Show</Button>
        <ReactMonacoEditor
          ref={this.state.text}
          value={this.text}
          width={"100%"}
          height={"100%"}
        ></ReactMonacoEditor>
      </>
    );
  }
}

Test.route = {
  [MENU_TITLE]: "test页面",
  [MENU_KEY]: "test",
  [MENU_PATH]: "/power/test",
};

export default Test;
