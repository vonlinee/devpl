import ReactMonacoEditor from "@/components/editor/ReactMonacoEditor"
import { Button } from "antd"

const Test = () => {
  return (<>
    <Button>111</Button>
    <ReactMonacoEditor></ReactMonacoEditor>
  </>)
}

Test.route={
  [MENU_TITLE] : "test页面",
  [MENU_KEY] : "test",
  [MENU_PATH]: "/power/test",
}

export default Test





