import { Button, Modal } from "antd";
import React, { useRef } from "react";
import { DataTableOptions, DataTableRef } from ".";
import { AnyObject } from "antd/es/_util/type";
import {
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleFilled,
} from "@ant-design/icons";

/**

export function createElement(ExclamationCircleFilled: React.ForwardRefExoticComponent<any>) {
  throw new Error("Function not implemented.");
}
 * 表格重复的配置项
 * @param options
 * @returns
 */
export const useDataTable = <R, F = AnyObject>(options: DataTableOptions<R, F>): DataTableOptions<R, F> => {
  if (options.tableRef == undefined) {
    options.tableRef = useRef<DataTableRef<R, F>>(null);
  }

  const showConfirm = () => {
    Modal.confirm({
      title: "是否删除?",
      icon: React.createElement(ExclamationCircleFilled),
      content: "描述信息",
      onOk() {
        if (options.api?.deleteById) {
          options.api.deleteById(1).then((res) => {
            console.log("删除结果", res);
          });
        }
      },
      onCancel() {
        console.log("点击取消按钮");
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
              // antd form
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
