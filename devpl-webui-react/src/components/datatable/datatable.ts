import { Button } from "antd";
import React from "react";
import { DataTableOptions, RowDataModel } from ".";
import { ColumnsType } from "antd/es/table/interface";

import { EditOutlined, DeleteOutlined } from "@ant-design/icons";

export const useDataTable = (
  options: DataTableOptions
): DataTableOptions => {
  options.columns.push({
    title: "操作",
    key: "operation",
    fixed: "right",
    width: 100,
    render: () => {
      return React.createElement("div", {
        className: 'datatable-operation-container'
      }, [
        React.createElement(Button, {
          key: 'dt_btn_edit',
          content: "编辑",
          type: "text",
          icon: React.createElement(EditOutlined),
          onClick: () => {
            console.log("click");
          },
        }),
        React.createElement(Button, {
          key: 'dt_btn_delete',
          content: "删除",
          type: "text",
          icon: React.createElement(DeleteOutlined),
          onClick: () => {
            console.log("click");
          },
        }),
      ]);
    },
  });
  return options;
};
