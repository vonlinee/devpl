import {
  apiEditDataTypeItem,
  apiListAllDataTypeGroups,
  apiListDataTypes,
  apiSaveDataTypeItems,
} from '@/services/typesystem';
import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { App, Button } from 'antd';
import { useEffect, useRef, useState } from 'react';
import SaveOrUpdateForm, { DataTypeFormValueType } from './SaveOrUpdate';
import TypeGroupManager from './TypeGroupManager';

const TypeSystem = () => {
  const [currentRow, setCurrentRow] = useState<DataTypeListItem>();
  const [selectedRowsState, setSelectedRows] = useState<DataTypeListItem[]>([]);
  /**
   * 新增/更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [typeGroupManagerOpen, setTypeGroupManagerOpen] = useState<boolean>(false);
  const { message } = App.useApp();

  const [typeGroupOptions, setTypeGroupOptions] = useState<SelectOptionType[]>();

  useEffect(() => {
    apiListAllDataTypeGroups().then((res) => {
      setTypeGroupOptions(
        res.data.map((tc) => {
          return {
            label: tc.typeGroupId,
            value: tc.typeGroupId,
          };
        }),
      );
    });
  }, []);

  /**
   * 更新节点
   * @param fields
   */
  const handleSaveOrUpdate = async (fields: DataTypeFormValueType) => {
    const hide = message.loading('更新');
    try {
      await apiEditDataTypeItem({
        ...currentRow,
        ...fields,
      });
      hide();
      message.success('更新成功');
      return true;
    } catch (error) {
      hide();
      message.error('更新失败, 请重试!');
      return false;
    }
  };

  const handleDelete = (row: DataTypeListItem) => {};

  useEffect(() => {}, []);

  // 保存还是更新
  const [saveOrUpdate, setSaveOrUpdate] = useState(false);

  const actionRef = useRef<ActionType>();
  // 列配置
  const columns: ProColumns<DataTypeListItem>[] = [
    {
      title: '分组',
      dataIndex: 'typeGroupId',
      valueType: 'select',
      fieldProps: {
        options: typeGroupOptions,
      },
    },
    {
      title: 'Key',
      dataIndex: 'typeKey',
    },
    {
      title: '名称',
      dataIndex: 'typeName',
    },
    {
      title: '值类型',
      dataIndex: 'valueType',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '范围',
      hideInSearch: true,
      renderText: (val, record) => record.minLength + ' ~ ' + record.maxLength,
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
            setSaveOrUpdate(false);
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
        <ProTable<DataTypeListItem, PageParams>
          tableAlertRender={() => false}
          headerTitle={'数据类型列表'}
          rowKey="id"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              key="primary"
              type="primary"
              onClick={() => {
                setSaveOrUpdate(true);
                handleUpdateModalOpen(true);
              }}
            >
              <PlusOutlined />
              新增
            </Button>,
            <Button
              key="primary"
              type="primary"
              onClick={() => {
                setTypeGroupManagerOpen(true);
              }}
            >
              类型分组管理
            </Button>,
          ]}
          actionRef={actionRef}
          request={apiListDataTypes}
          columns={columns}
          rowSelection={{
            onChange: (_, selectedRows) => {
              setSelectedRows(selectedRows);
            },
          }}
        />
        <SaveOrUpdateForm
          updateModalOpen={updateModalOpen}
          setUpdateModalOpen={handleUpdateModalOpen}
          saveOrUpdate={saveOrUpdate}
          values={currentRow || {}}
          onSubmit={async (value) => {
            const success = await handleSaveOrUpdate(value);
            if (success) {
              handleUpdateModalOpen(false);
              setCurrentRow(undefined);
              if (actionRef.current) {
                // 刷新数据
                actionRef.current.reload();
              }
            }
          }}
          onCancel={(flag, fieldValue) => {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
          }}
        ></SaveOrUpdateForm>

        <TypeGroupManager updateModalOpen={typeGroupManagerOpen} setModalVisiable={setTypeGroupManagerOpen}></TypeGroupManager>
      </PageContainer>
    </>
  );
};

/**
 * 不使用<App>包裹，则下面代码报错
 * const { message } = App.useApp();
 * message.loading('xxx') // 报错 message.loading is not a function
 */
export default () => (
  <App>
    <TypeSystem />
  </App>
);
