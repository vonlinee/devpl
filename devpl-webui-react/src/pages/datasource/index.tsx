import {
  apiDelDataSource,
  apiListDataSource,
  apiListSupportedDbTypes,
  apiSaveOrUpdateDataSource,
} from '@/services/datasource';
import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { App, Button } from 'antd';
import { useEffect, useRef, useState } from 'react';
import UpdateForm, { FormValueType } from './UpdateForm';

/**
 * 数据源列表
 * @returns
 */
const DataSourceList = () => {
  const [currentRow, setCurrentRow] = useState<DataSourceListItem>();

  const [selectedRowsState, setSelectedRows] = useState<DataSourceListItem[]>([]);
  const [showDetail, setShowDetail] = useState<boolean>(false);

  const [driverOptions, setDriverOptions] = useState<SelectOptionType[]>([]);

  useEffect(() => {
    apiListSupportedDbTypes().then((res: any) => {
      const driverTypes = res.data as DriverTypeVO[];
      if (driverTypes != null) {
        const options: SelectOptionType[] = driverTypes.map((driverType) => {
          return {
            label: driverType.name,
            value: driverType.id,
          };
        });
        setDriverOptions(options);
      }
    });
  }, []);

  // 保存还是更新
  const [saveOrUpdate, setSaveOrUpdate] = useState(false);

  const { message } = App.useApp();

  /**
   * 更新节点
   * @param fields
   */
  const handleSaveOrUpdate = async (fields: FormValueType) => {
    const hide = message.loading('更新');
    try {
      await apiSaveOrUpdateDataSource({
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

  const handleDelete = (row: DataSourceListItem) => {
    apiDelDataSource([row.id]).then((res) => {
      if (res.code == 2000) {
        message.info("删除成功")
        if (actionRef.current) {
          // 刷新数据
          actionRef.current.reload();
        }
      }
    })
  };

  /**
   * 新增/更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  // 列配置
  const columns: ProColumns<DataSourceListItem>[] = [
    {
      title: '连接名称',
      dataIndex: 'connName',
      tip: '唯一',
    },
    {
      title: '驱动类型',
      dataIndex: 'driverType',
      valueType: 'select',
      fieldProps: {
        options: driverOptions,
      },
    },
    {
      title: 'HOST/PORT',
      dataIndex: '主机地址/端口号',
      hideInSearch: true,
      renderText: (val, record) => record.host + ':' + record.port,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '密码',
      dataIndex: 'password',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="addDataSource"
          onClick={(event) => {
            setSaveOrUpdate(false);
            setCurrentRow(record);
            handleUpdateModalOpen(true);
          }}
        >
          编辑
        </a>,
        <a key="delDataSource" onClick={(event) => handleDelete(record)}>
          删除
        </a>,
      ],
    },
  ];

  return (
    <>
      <PageContainer>
        <ProTable<DataSourceListItem, PageParams>
          tableAlertRender={() => false}
          headerTitle={'数据源连接配置列表'}
          rowKey="id"
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              key="primary"
              type='primary'
              onClick={() => {
                setSaveOrUpdate(true);
                handleUpdateModalOpen(true);
              }}
            >
              <PlusOutlined />新增
            </Button>,
          ]}
          actionRef={actionRef}
          request={apiListDataSource}
          columns={columns}
          rowSelection={{
            onChange: (_, selectedRows) => {
              setSelectedRows(selectedRows);
            },
          }}
        />
        <UpdateForm
          supportedDrivers={driverOptions}
          saveOrUpdate={saveOrUpdate}
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
            if (!showDetail) {
              setCurrentRow(undefined);
            }
          }}
          updateModalOpen={updateModalOpen}
          setUpdateModalOpen={handleUpdateModalOpen}
          values={currentRow || {}}
        />
      </PageContainer>
    </>
  );
};

/**
 * 使用App包裹，使用message组件
 */
export default () => (
  <App>
    <DataSourceList />
  </App>
);
