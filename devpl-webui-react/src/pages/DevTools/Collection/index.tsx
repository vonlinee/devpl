import {Tabs} from "antd";
import {useState} from "react";
import SqlConvert from "@/pages/DevTools/Collection/SqlConvert";
import ModelConvert from "@/pages/DevTools/Collection/ModelConvert";

const panes = [{
  key: "sqlconvert",
  label: "SQL转换",
  closable: false,
  children: <SqlConvert/>
}, {
  key: "modelconvert",
  label: "实体类转换SQL",
  closable: false,
  children: <ModelConvert/>
}]

/**
 * 工具箱
 * @constructor
 */
export default function DevToolCollection() {
  const [activeKey, setActiveKey] = useState(panes[0].key);
  const [items, setItems] = useState(panes);

  const onChange = (key: string) => {
    setActiveKey(key);
  };

  return <>
    <Tabs
      onChange={onChange}
      activeKey={activeKey}
      type="editable-card"
      items={items}
    />
  </>
}
