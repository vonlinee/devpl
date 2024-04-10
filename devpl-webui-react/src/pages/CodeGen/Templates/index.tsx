import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { App, Button } from 'antd';
import { useEffect, useRef, useState } from 'react';
import NewTemplate from './NewTemplate';
import { apiListTemplatesByPage, apiUpdateTemplate } from '@/services/templates';

/**
 * 模板列表
 * @returns
 */
const TempalateList = () => {
  const [currentRow, setCurrentRow] = useState<TemplateListItem>();
  const [selectedRowsState, setSelectedRows] = useState<TemplateListItem[]>([]);
  /**
   * 新增/更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const { message } = App.useApp();

  const handleDelete = (row: TemplateListItem) => {};

  const actionRef = useRef<ActionType>();
  // 列配置
  const columns: ProColumns<TemplateListItem>[] = [
    {
      title: '模板名称',
      dataIndex: 'templateName',
    },
    {
      title: '模板类型',
      dataIndex: 'templateType',
    },
    {
      title: '提供方',
      dataIndex: 'provider',
    },
    {
      title: '备注',
      dataIndex: 'remark',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="editDataType"
          onClick={(event) => {
            setCurrentRow(record);
            handleUpdateModalOpen(true);
          }}
        >
          编辑
        </a>,
        <a key="delDataType" onClick={(event) => handleDelete(record)}>
          删除
        </a>,
      ],
    },
  ];

  return (
    <>
      <PageContainer>
        <ProTable<TemplateListItem, PageParams>
          tableAlertRender={() => false}
          rowKey="templateId"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              key="primary"
              type="primary"
              onClick={() => {
                setCurrentRow(undefined);
                handleUpdateModalOpen(true);
              }}
            >
              <PlusOutlined />
              新增模板
            </Button>,
          ]}
          pagination={{
            pageSize: 10,
          }}
          actionRef={actionRef}
          request={apiListTemplatesByPage}
          columns={columns}
          rowSelection={{
            onChange: (_, selectedRows) => {
              setSelectedRows(selectedRows);
            },
          }}
        />

        <NewTemplate
          updateModalOpen={updateModalOpen}
          onSubmit={(formValues) => {
            return apiUpdateTemplate(Object.assign(currentRow || {}, formValues));
          }}
          onCancel={(f, val) => {
            handleUpdateModalOpen(false);
          }}
          currentRow={currentRow}
        />
      </PageContainer>
    </>
  );
};

export default () => (
  <App>
    <TempalateList />
  </App>
);
