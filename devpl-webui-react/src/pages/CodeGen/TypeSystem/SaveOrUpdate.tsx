import { ModalForm, ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea } from "@ant-design/pro-components";
import { Form } from "antd";


/**
 * 表单数据类型
 */
export type DataTypeFormValueType = {
  typeKey: string;
  typeName: string;
  typeGroupId?: string;
  valueType: string
  minLength?: number;
  maxLength?: number;
  remark: string;
} & Partial<DataTypeListItem>;

/**
 * 更新表单属性
 */
export type DataTypeFormProps = {
  /**
   * 保存还是更新，影响表单提交的逻辑
   */
  saveOrUpdate: boolean;
  onCancel: (flag?: boolean, formVals?: DataTypeFormValueType) => void;
  onSubmit: (values: DataTypeFormValueType) => Promise<void>;
  updateModalOpen: boolean;
  setUpdateModalOpen: (val: boolean) => void;
  /**
   * 表单初始数据
   */
  values: Partial<DataTypeListItem>;
};

const SaveOrUpdateForm: React.FC<DataTypeFormProps> = (props) => {
  const [form] = Form.useForm<DataTypeFormValueType>();

  const openUpdateModal = (val: boolean) => {
    if (val && props.values) {
      form.setFieldsValue(props.values);
    } else {
      props.onCancel(true, form.getFieldsValue());
      // 重置表单
      form.resetFields();
    }
  };

  return (
    <>
      <ModalForm
        name={props.saveOrUpdate ? 'saveDataSource' : 'updateDataSource'}
        title={props.saveOrUpdate ? '新增' : '修改'}
        open={props.updateModalOpen}
        onOpenChange={openUpdateModal}
        onFinish={props.onSubmit}
        grid={true}
        layout="vertical"
        form={form}
      >
        <ProForm.Group>
          <ProFormSelect
            width="sm"
            name="typeGroupId"
            label="分组"
            colProps={{ md: 8, xl: 8 }}
            rules={[{ required: true, message: '请选择类型分组' }]}
          />
          <ProFormText
            width="sm"
            name="typeKey"
            label="类型Key"
            colProps={{ md: 8, xl: 8 }}
            rules={[{ required: true, message: '请输入类型Key' }]}
          />
          <ProFormText
            width="md"
            name="typeName"
            colProps={{ md: 12, xl: 8 }}
            label="类型名称"
            placeholder="请输入类型名称"
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText name="valueType" colProps={{ md: 8, xl: 8 }} label="值类型" />
          <ProFormDigit name="minLength" colProps={{ md: 8, xl: 8 }} label="最小长度" />
          <ProFormDigit name="maxLength" colProps={{ md: 12, xl: 8 }} label="最大长度" />
        </ProForm.Group>
        <ProFormTextArea colProps={{ span: 24 }} fieldProps={{
          autoSize: { minRows: 3, maxRows: 5 }
        }} width="md" name="remark" label="备注" />
      </ModalForm>
    </>
  );
}

export default SaveOrUpdateForm