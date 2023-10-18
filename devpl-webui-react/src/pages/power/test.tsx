import { useDataSourceTestApi } from "@/api/backend";
import { apiListDataTypes } from "@/api/datatype";
import DataTable, {
  DataTableOptions,
  RowDataModel,
} from "@/components/datatable";
import { useDataTable } from "@/components/datatable/datatable";
import { ReactMonacoEditor } from "@/components/editor/ReactMonacoEditor";
import { Button, Checkbox, Form, Input } from "antd";
import React from "react";

/**
 * 测试
 */
const Test = () => {
  let text = "";

  let ref = React.createRef<ReactMonacoEditor>();

  const getText = () => {
    console.log(ref.current?.getText());
  };

  const setText = () => {
    ref.current?.setText("hello world");
  };

  interface DataType {
    key: React.Key;
    name: string;
    age: number;
    address: string;
  }

  const data: RowDataModel[] = [];
  for (let i = 0; i < 100; i++) {
    data.push({
      key: i,
      name: `Edward ${i}`,
      age: 32,
      address: `London Park no. ${i}`,
    });
  }

  const onFinish = (values: any) => {
    console.log("Success:", values);
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  type FieldType = {
    typeKey?: "";
    typeName?: "";
    valueType?: "";
  };

  const TableForm = ({ data }) => {
    return (
      <>
        <Form
          name="basic"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          style={{ maxWidth: 600 }}
          initialValues={data}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item<FieldType>
            label="Username"
            name="typeKey"
            rules={[{ required: true, message: "Please input your username!" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item<FieldType>
            label="Password"
            name="typeName"
            rules={[{ required: true, message: "Please input your password!" }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item<FieldType>
            name="valueType"
            valuePropName="checked"
            wrapperCol={{ offset: 8, span: 16 }}
          >
            <Checkbox>Remember me</Checkbox>
          </Form.Item>

          <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </Form.Item>
        </Form>
      </>
    );
  };

  let formData = {
    typeKey: "",
    typeName: "",
    valueType: "",
  };

  const options: DataTableOptions = useDataTable({
    columns: [
      {
        title: "数据类型Key",
        width: 100,
        dataIndex: "typeKey",
        key: "typeKey",
        fixed: "left",
      },
      {
        title: "类型名称",
        width: 100,
        dataIndex: "typeName",
        key: "typeName",
        fixed: "left",
      },
      {
        title: "值类型",
        width: 100,
        dataIndex: "valueType",
        key: "valueType",
        fixed: "left",
      },
      {
        title: "最小长度",
        width: 100,
        dataIndex: "minLength",
        key: "minLength",
        fixed: "left",
      },
      {
        title: "最大长度",
        width: 100,
        dataIndex: "maxLength",
        key: "maxLength",
        fixed: "left",
      },
      {
        title: "类型默认值",
        width: 100,
        dataIndex: "defaultValue",
        key: "defaultValue",
        fixed: "left",
      },
    ],
    pageable: true,
    api: {
      queryPage: apiListDataTypes,
    },
    formData: formData,
    modalContent: (param) => {
      console.log(param);
      return (<TableForm data={param}></TableForm>) 
    }
  });

  return (
    <>
      <DataTable ref={options.tableRef} options={options}></DataTable>
    </>
  );
};

Test.route = {
  [MENU_TITLE]: "test页面",
  [MENU_KEY]: "test",
  [MENU_PATH]: "/power/test",
};

export default Test;
