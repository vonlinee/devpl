import React from "react";
import { Col, Form, FormInstance, Input, Row, Select } from "antd";

const onFinish = (values: any) => {
  console.log("Success:", values);
};

const onFinishFailed = (errorInfo: any) => {
  console.log("Failed:", errorInfo);
};

type FieldType = {
  connName?: string;
  driverType?: string;
  host?: string;
  port: number;
  username: string;
  password: string;
};

interface DataSourceFormProps {
  form : FormInstance<any>
}

const DataSourceForm: React.FC<DataSourceFormProps> = ({form}) => {
  // 表单数据对象
  return (
    <>
      <Form
        form={form}
        name="basic"
        labelAlign="left"
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 20 }}
        style={{ maxWidth: 600 }}
        initialValues={{ port: 3306 }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        <Form.Item<FieldType>
          label="连接名称"
          name="connName"
          rules={[{ required: true, message: "连接名称" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item<FieldType>
          label="驱动类型"
          name="driverType"
          rules={[{ required: true, message: "驱动类型" }]}
        >
          <Select
            style={{ width: 120 }}
            options={[
              { value: "MySQL", label: "MySQL" },
              { value: "Oracle", label: "Oracle" },
            ]}
          />
        </Form.Item>

        <Row>
          <Col>
            <Form.Item<FieldType>
              label="IP"
              name="host"
              rules={[{ required: true, message: "输入IP地址" }]}
            >
              <Input />
            </Form.Item>
          </Col>
          <Col>
            <Form.Item<FieldType>
              label="端口号"
              name="port"
              rules={[{ required: true, message: "输入端口号" }]}
            >
              <Input />
            </Form.Item>
          </Col>
        </Row>

        <Form.Item<FieldType>
          label="用户名"
          name="username"
          rules={[{ required: true, message: "连接用户名" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item<FieldType>
          label="密码"
          name="password"
          rules={[{ required: true, message: "密码" }]}
        >
          <Input.Password />
        </Form.Item>
      </Form>
    </>
  );
};

export default DataSourceForm;
