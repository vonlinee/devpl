import { FormInstance, Table, TableProps } from 'antd';
import { AnyObject } from 'antd/es/_util/type';
import EditableRow from './EditableRow';
import EditableCell from './EditableCell';
import { ColumnType, ColumnsType } from 'antd/es/table';

import React from 'react';
import { GetComponentProps } from '@ant-design/pro-components';

type EditableColumnType<T = AnyObject> = ColumnType<T> & {
  editable?: boolean;
};

type EditableTableProps<T = AnyObject> = {
  columns: EditableColumnType<T>[];
  data?: T[];
};

/**
 * 可编辑表格
 * @param props
 * @returns
 */
export default function EditableTable<T extends AnyObject>(props: EditableTableProps<T>) {
  let { columns, data } = props;

  const components = {
    body: {
      row: EditableRow,
      cell: EditableCell,
    },
  };

  const context = React.createContext<FormInstance<T> | null>(null);

  let _columns = columns.map((col) => {
    if (!col.editable) {
      return col;
    }
    return {
      ...col,
      onCell: (record: T) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        editContext: context,
      }),
    };
  }) as ColumnsType<T>;

  const onRow = (record: T) => {
    return {
      context: context,
    };
  };

  return (
    <>
      <Table dataSource={data} onRow={onRow} components={components} columns={_columns}></Table>
    </>
  );
}
