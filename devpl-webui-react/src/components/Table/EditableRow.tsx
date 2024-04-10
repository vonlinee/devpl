import { Form, FormInstance } from 'antd';
import { AnyObject } from 'antd/es/_util/type';
import React, { Context } from 'react';

interface EditableRowProps<T = AnyObject> {
  index: number;
  context: Context<T>;
}

const EditableRow: React.FC<EditableRowProps> = (props) => {
  const { index, context } = props;

  const EditableContext = context;

  const [form] = Form.useForm();
  return (
    <Form form={form} component={false}>
      {context == null ? (
        <tr {...props}></tr>
      ) : (
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      )}
    </Form>
  );
};

export default EditableRow;
