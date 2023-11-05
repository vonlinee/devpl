import React from "react";
import { apiListDataSourcePage, apiSaveOrUpdateDataSource } from "@/api/datasource";
import DataTable, {
  DataTableOptions,
} from "@/components/datatable";
import DataSourceForm from "./DataSourceForm";
import { Form } from "antd";

/**
 * 数据源
 */
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
  const [form] = Form.useForm();

  const options: DataTableOptions<
    DataSourceTableItemVO,
    DataSourceTableItemVO
  > = {
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
        dataIndex: "username",
        key: "3",
        width: 150,
      },
    ],
    formConfig: {
      instance: Form.useForm()[0],
    },
    pageable: false,
    modalRender: (formInstance) => {
      return <DataSourceForm form={formInstance}></DataSourceForm>;
    },
    api: {
      queryPage: apiListDataSourcePage,
      update: apiSaveOrUpdateDataSource
    },
  };

  return <DataTable options={options}></DataTable>;
};

export default DataSourceManager;

DataSourceManager.route = { path: "/datasource" };
