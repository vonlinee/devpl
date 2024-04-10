import { ModalForm, ProForm, ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { Form } from 'antd';
import React, { useEffect } from 'react';

/**
 * 表单数据类型
 */
export type FormValueType = {
  connName: string;
  driverType?: string;
  host?: string;
  port?: number;
} & Partial<DataSourceListItem>;

/**
 * 更新表单属性
 */
export type UpdateFormProps = {
  /**
   * 保存还是更新，影响表单提交的逻辑
   */
  saveOrUpdate: boolean;
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalOpen: boolean;
  setUpdateModalOpen: (val: boolean) => void;
  /**
   * 支持的驱动类型
   */
  supportedDrivers: SelectOptionType[];
  /**
   * 表单初始数据
   */
  values: Partial<DataSourceListItem>;
};

const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  const [form] = Form.useForm<DataSourceListItem>();

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
        form={form}
      >
        <ProForm.Group>
          <ProFormText
            width="md"
            name="connName"
            label="连接名称"
            tooltip="最长为 24 位"
            placeholder="请输入连接名称"
            rules={[{ required: true, message: '请输入连接名称' }]}
          />
          <ProFormSelect
            width="sm"
            options={props.supportedDrivers}
            name="driverType"
            label="驱动类型"
            rules={[{ required: true, message: '请输入驱动类型' }]}
          />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText width="md" name="host" label="HOST" tooltip="默认127.0.0.1" />
          <ProFormText name="port" label="端口" placeholder={'3306'} />
        </ProForm.Group>
        <ProForm.Group>
          <ProFormText width="md" name="username" label="用户名" tooltip="默认root" />
          <ProFormText.Password
            name="password"
            label="密码"
            placeholder={'请输入密码'}
            tooltip="默认123456"
          />
        </ProForm.Group>
      </ModalForm>
    </>
  );
};

export default UpdateForm;
