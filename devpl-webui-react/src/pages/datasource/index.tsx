import React, { useRef, useState } from "react";
import { apiListDataSourcePage } from "@/api/datasource";
import { useDataTable } from "@/components/datatable/datatable";
import DataTable, { DataTableRef } from "@/components/datatable";
import DataSourceForm from "./DataSourceForm";
import { Form } from "antd";

interface DataSourceTableItemVO {
  id: number | string;
  key: React.Key;
  connName: string;
  driverType: string;
  host: number;
  port: string;
}

/**
 * 数据源管理
 * @returns
 */
const DataSourceManager = () => {
  const tableRef = useRef<DataTableRef>(null);
  const [form] = Form.useForm()

  const options = useDataTable<DataSourceTableItemVO>({
    columns: [
      {
        title: "连接名称",
        width: 50,
        dataIndex: "connName",
        key: "1",
      },
      {
        title: "驱动类型",
        width: 30,
        dataIndex: "driverType",
        key: "2",
      },
      {
        title: "HOST",
        dataIndex: "host",
        key: "1",
        width: 60,
      },
      {
        title: "PORT",
        dataIndex: "port",
        key: "2",
        width: 30,
      },
      {
        title: "用户名",
        dataIndex: "address",
        key: "3",
        width: 150,
      },
    ],
    formData: form,
    pageable: false,
    tableRef: tableRef,
    modalRender: (param) => {
      return <><DataSourceForm form={form}></DataSourceForm></>;
    },
    api: {
      queryPage: apiListDataSourcePage,
    },
  });

  return (
    <>
      <DataTable ref={tableRef} options={options}></DataTable>
    </>
  );
};

export default DataSourceManager;

DataSourceManager.route = { path: "/datasource" };
