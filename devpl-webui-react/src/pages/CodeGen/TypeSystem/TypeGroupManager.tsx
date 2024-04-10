import { ModalForm, ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea } from "@ant-design/pro-components";
import { Form } from "antd";

/**
 * 表单数据类型
 */
export type DataTypeGroupFormData = {
  typeGroupId?: string;
  typeGroupName?: string;
} & Partial<DataTypeGroupItem>;

/**
 * 类型分组表单属性
 */
export type TypeGroupFormProps = {
  updateModalOpen: boolean;
  setModalVisiable: (val: boolean) => void
};

/**
 * 类型分组管理
 * @param props 
 * @returns 
 */
const TypeGroupManager: React.FC<TypeGroupFormProps> = (props) => {
  const [form] = Form.useForm<DataTypeGroupFormData>();

  return (
    <>
      <ModalForm
        name="typeGroupModal"
        title="类型分组管理"
        open={props.updateModalOpen}
        layout="horizontal"
        onOpenChange={props.setModalVisiable}
        form={form}
      >
        <ProFormText
          width="sm"
          name="typeGroupKey"
          label="类型分组Key"
          rules={[{ required: true, message: '请输入类型Key' }]}
        />
        <ProFormText
          width="md"
          name="typeName"
          label="类型分组名称"
          placeholder="请输入类型分组名称"
        />
      </ModalForm>
    </>
  );
}

export default TypeGroupManager