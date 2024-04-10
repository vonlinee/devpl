import { EditorInstance } from '@/components/Editor';
import {
  ModalForm,
  ProForm,
  ProFormRadio,
  ProFormText,
} from '@ant-design/pro-components';
import { Editor, Monaco } from '@monaco-editor/react';
import { Button, Form } from 'antd';
import { RadioChangeEvent } from 'antd/lib';
import React, { useRef, useState } from 'react';

/**
 * 模板表单
 */
export type TemplateForm = {
  templateId: string | number;
  templateName: string;
  type?: string;
  content?: string;
  remark: string;
} & Partial<TemplateListItem>;

/**
 * 模板表单属性
 */
export type TemplateFormProps = {
  onCancel: (flag?: boolean, formVals?: TemplateForm) => void;
  onSubmit: (values: TemplateForm) => Promise<void>;
  updateModalOpen: boolean;
  currentRow?: TemplateListItem;
};

const NewTemplate: React.FC<TemplateFormProps> = (props) => {
  const { currentRow, onSubmit, onCancel, updateModalOpen } = props;

  const [form] = Form.useForm<TemplateForm>();

  const [currentTemplateType, setCurrentTemplateType] = useState(currentRow?.type)

  const openUpdateModal = (val: boolean) => {
    if (val) {
      if (currentRow) {
        form.setFieldsValue(currentRow);

        if (currentRow.content) {
          editorRef.current?.getModel()?.setValue(currentRow.content || '');
        }
      }
    } else {
      onCancel(true, form.getFieldsValue());
      // 重置表单
      form.resetFields();
    }
  };

  const editorRef = useRef<EditorInstance>();

  function handleEditorDidMount(editor: EditorInstance, _monaco: Monaco) {
    editorRef.current = editor;
  }
  const fileChooserRef = React.useRef<HTMLInputElement>(null);

  function onFileChooseChange() {
    let choosenFiles = fileChooserRef.current?.files;
    if (choosenFiles) {
      const file = choosenFiles.item(0);
      if (file) {
        const reader = new FileReader();
        reader.onload = function fileReadCompleted() {
          // 当读取完成时，内容只在`reader.result`中
          editorRef.current?.getModel()?.setValue((reader.result as string) || '');
          form.setFieldValue('templateName', file.name);
        };
        reader.readAsText(file);
      }
    }
  }

  return (
    <>
      <ModalForm
        name={currentRow ? 'editTemplate' : 'newTemplate'}
        title={currentRow ? '修改模板' : '新增模板'}
        open={updateModalOpen}
        onOpenChange={openUpdateModal}
        onFinish={(formData) => {
          formData.content = editorRef.current?.getModel()?.getValue()
          return onSubmit(formData)
        }}
        layout="horizontal"
        form={form}
      >
        <ProForm.Group>
          <ProFormText
            width="sm"
            name="templateName"
            label="模板名称"
            colProps={{ md: 8, xl: 8 }}
            rules={[{ required: true, message: '输入模板名称' }]}
          />
          <ProFormRadio.Group
            name="type"
            label="模板类型"
            valueEnum={{
              1: '字符串模板',
              2: '文件模板',
            }}
            fieldProps={{
              size: 'small',
              onChange: (event: RadioChangeEvent) => {
                setCurrentTemplateType(form.getFieldValue('type'));
              },
            }}
            options={[
              {
                label: '字符串模板',
                value: 1,
              },
              {
                label: '文件模板',
                value: 2,
              },
            ]}
          />
          {currentTemplateType == 2 ? <Button onClick={(event) => fileChooserRef.current?.click()}>选择</Button> : null}
        </ProForm.Group>

        <input
          ref={fileChooserRef}
          type="file"
          style={{
            display: 'none',
          }}
          accept=".ftl,.vm"
          onChange={onFileChooseChange}
        ></input>

        <Editor
          width={'100%'}
          height={'400px'}
          defaultLanguage="xml"
          onMount={handleEditorDidMount}
        />

        <ProFormText colProps={{ span: 24 }} width="md" name="remark" label="备注" />
      </ModalForm>
    </>
  );
};

export default NewTemplate;
