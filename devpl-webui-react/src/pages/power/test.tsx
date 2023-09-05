import ReactMonacoEditor from "@/components/editor/ReactMonacoEditor"
import { Button, Modal } from "antd"
import { useState } from "react"

/**
 * 组件测试
 * @returns 
 */
const Test = () => {

  const [text, setText] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (<>
    <Button onClick={showModal}></Button>

    <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
        <p>Some contents...</p>
        <p>Some contents...</p>
        <p>Some contents...</p>
      </Modal>

    <ReactMonacoEditor text={text} ></ReactMonacoEditor>
  </>)
}

Test.route={
  [MENU_TITLE] : "test页面",
  [MENU_KEY] : "test",
  [MENU_PATH]: "/power/test",
}

export default Test





