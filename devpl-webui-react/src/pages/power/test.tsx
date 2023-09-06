import ReactMonacoEditor from "@/components/editor/ReactMonacoEditor"
import {Button, Modal} from "antd"
import {useRef, useState} from "react"

/**
 * 组件测试
 * @returns
 */
const Test = () => {

  const [text, setText] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const divRef = useRef<HTMLDivElement>(null)

  const showModal = () => {
    // setIsModalOpen(true);
    console.log("divRef", divRef.current)
  };

  return (<>
    <Button onClick={showModal}></Button>

    <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
      <div style={{
        backgroundColor: "gray"
      }}>

      </div>
    </Modal>

    <ReactMonacoEditor text={text} ref={divRef}></ReactMonacoEditor>
  </>)
}

Test.route = {
  [MENU_TITLE]: "test页面",
  [MENU_KEY]: "test",
  [MENU_PATH]: "/power/test",
}

export default Test





