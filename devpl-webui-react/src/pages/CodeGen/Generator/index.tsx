import React, { useContext, useEffect, useState } from 'react';

import { AnyObject } from 'antd/es/_util/type';
import { Button, FormInstance, Modal, Table } from 'antd';
import TemplateParamTable from './TemplateParamTable';

type EditableTableProps = Parameters<typeof Table>[0];

interface DataType {
  key: React.Key;
  filename: string;
  templateId?: number;
  templateName?: string;
  // 填充策略
  fillStrtegy?: string;
}

type ColumnTypes = Exclude<EditableTableProps['columns'], undefined>;

/**
 * GeneratorDashboard
 * @returns
 */
const GeneratorDashboard: React.FC = () => {
  const [dataSource, setDataSource] = useState<DataType[]>([
    {
      key: '0',
      filename: 'Edward King 0',
      templateId: 32,
      templateName: 'London, Park Lane no. 0',
    },
    {
      key: '1',
      filename: 'Edward King 1',
      templateId: 32,
      templateName: 'London, Park Lane no. 1',
    },
    {
      key: '2',
      filename: 'Edward King 1',
      templateId: 32,
      templateName: 'London, Park Lane no. 1',
    },
    {
      key: '3',
      filename: 'Edward King 1',
      templateId: 32,
      templateName: 'London, Park Lane no. 1',
    },
    {
      key: '4',
      filename: 'Edward King 1',
      templateId: 32,
      templateName: 'London, Park Lane no. 1',
    },
  ]);

  const [count, setCount] = useState(2);

  const handleDelete = (key: React.Key) => {
    const newData = dataSource.filter((item) => item.key !== key);
    setDataSource(newData);
  };

  const [paramTableModalOpen, setParamTableModalOpen] = useState(false);

  function handParamTableModalOpen(key: React.Key, open: boolean) {
    setParamTableModalOpen(open);
  }

  const columns: (ColumnTypes[number] & { editable?: boolean; dataIndex: string })[] = [
    {
      title: '文件名',
      dataIndex: 'filename',
      width: '30%',
      editable: true,
    },
    {
      title: '模板',
      dataIndex: 'templateName',
      editable: true,
    },
    {
      title: '数据填充策略',
      dataIndex: 'fillStrtegy',
      editable: true,
    },
    {
      title: '操作',
      dataIndex: 'operation',
      align: 'center',
      render: (_: any, record: AnyObject, index: number) =>
        dataSource.length >= 1 ? (
          <>
            <a
              onClick={() => handParamTableModalOpen(record.key, true)}
              style={{
                marginRight: 10,
              }}
            >
              填充参数
            </a>
            <a
              onClick={() => handParamTableModalOpen(record.key, true)}
              style={{
                marginRight: 10,
              }}
            >
              参数表
            </a>
            <a
              onClick={() => handParamTableModalOpen(record.key, true)}
              style={{
                marginRight: 10,
              }}
            >
              预览
            </a>
            <a onClick={() => handleDelete(record.key)}>删除</a>
          </>
        ) : null,
    },
  ];

  const handleAdd = () => {
    const newData: DataType = {
      key: count,
      filename: 'New File',
    };
    setDataSource([...dataSource, newData]);
    setCount(count + 1);
  };

  return (
    <div>
      <Button onClick={handleAdd} type="primary" style={{ marginBottom: 16 }}>
        新增
      </Button>
      <Table
        size="small"
        rowClassName={() => 'editable-row'}
        bordered
        dataSource={dataSource}
        columns={columns}
      />
      <Modal
        title="模板参数表"
        width={'50%'}
        style={
          {
            height: '70%'
          }
        }
        open={paramTableModalOpen}
        onCancel={() => handParamTableModalOpen('', false)}
      >
        <TemplateParamTable />
      </Modal>
    </div>
  );
};

export default GeneratorDashboard;
