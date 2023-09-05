import ReactMonacoEditor from "@/components/editor/ReactMonacoEditor"
import { Button } from "antd"
import { useState } from "react"

const Test = () => {

  const [text, setText] = useState("");

  return (<>
    <Button onClick={() => console.log(text)}></Button>
    <ReactMonacoEditor text={text} ></ReactMonacoEditor>
  </>)
}

Test.route={
  [MENU_TITLE] : "test页面",
  [MENU_KEY] : "test",
  [MENU_PATH]: "/power/test",
}

export default Test





