import { Button, Modal } from "antd";
import React, { useRef } from "react";
import { DataTableOptions } from ".";

import {
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
} from "@ant-design/icons";

/**
 * 表格重复的配置项
 * @param options
 * @returns
 */
export const useDataTable = (options: DataTableOptions): DataTableOptions => {
  if (options.tableRef == undefined) {
    options.tableRef = useRef(null);
  }

  const showConfirm = () => {
    Modal.confirm({
      title: "Do you Want to delete these items?",
      icon: React.createElement(ExclamationCircleFilled),
      content: "Some descriptions",
      onOk() {
        if (options.api?.deleteById) {
          options.api.deleteById(1).then((res) => {
            console.log("删除结果", res);
          });
        }
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  // 添加操作列
  options.columns.push({
    title: "操作",
    key: "operation",
    fixed: "right",
    align: 'center',
    width: 50,
    render: (value: any, record: any, index: number) => {
      return React.createElement(
        "div",
        {
          className: "datatable-operation-container",
        },
        [
          React.createElement(Button, {
            key: "dt_btn_edit",
            content: "编辑",
            type: "text",
            icon: React.createElement(EditOutlined),
            onClick: () => {
              // 将选中行的数据填充到表单上
              if (options.formData) {
                Object.assign(options.formData, record)
              }
              options.tableRef?.current?.showModal(record);
            },
          }),
          React.createElement(Button, {
            key: "dt_btn_delete",
            content: "删除",
            type: "text",
            icon: React.createElement(DeleteOutlined),
            onClick: () => {
              showConfirm();
            },
          }),
        ]
      );
    },
  });
  return options;
};
