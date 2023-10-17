import React, { useEffect, useRef, useState } from "react";
import { Button, Modal, Table } from "antd";
import type { ColumnsType, TableProps } from "antd/es/table";
import { AnyObject } from "antd/es/_util/type";
import { Draggable } from "react-beautiful-dnd";

/**
 * 行数据模型
 */
export declare type RowDataModel = AnyObject;

/**
 * 选项
 */
export declare interface DataTableOptions {
    // 列定义
    columns: ColumnsType<RowDataModel>;
    // 是否可分页
    pageable: boolean;
    // 数据
    data: TableProps<RowDataModel>[];
}

/**
 * DataTable 属性
 */
export declare interface DataTableProps {
  options: DataTableOptions
}

/**
 * 组件定义
 * @param props
 * @returns
 */
const DataTable: React.FC<DataTableProps> = (props: DataTableProps) => {
  const [open, setOpen] = useState(false);
  const [disabled, setDisabled] = useState(true);
  const draggleRef = useRef<HTMLDivElement>(null);

  const showModal = () => {
    setOpen(true);
  };

  const handleOk = (e: React.MouseEvent<HTMLElement>) => {
    setOpen(false);
  };

  const handleCancel = (e: React.MouseEvent<HTMLElement>) => {
    setOpen(false);
  };

  return (
    <>
      <Table
        onRow={(record) => {
          return {
            onClick: (event) => {}, // 点击行
            onDoubleClick: (event) => {},
            onContextMenu: (event) => {},
            onMouseEnter: (event) => {}, // 鼠标移入行
            onMouseLeave: (event) => {},
          };
        }}
        columns={props.options.columns}
        dataSource={props.options.data}
        scroll={{ x: 1500 }}
        sticky={{ offsetHeader: 64 }}
      />
      <Modal
        title={
          <div
            style={{
              width: "100%",
              cursor: "move",
            }}
            onMouseOver={() => {
              if (disabled) {
                setDisabled(false);
              }
            }}
            onMouseOut={() => {
              setDisabled(true);
            }}
            // fix eslintjsx-a11y/mouse-events-have-key-events
            // https://github.com/jsx-eslint/eslint-plugin-jsx-a11y/blob/master/docs/rules/mouse-events-have-key-events.md
            onFocus={() => {}}
            onBlur={() => {}}
            // end
          >
            Draggable Modal
          </div>
        }
        open={open}
        onOk={handleOk}
        onCancel={handleCancel}
        modalRender={(modal) => (
          <>
            <div ref={draggleRef}>{modal}</div>
          </>
        )}
      >
        <p>
          Just don&apos;t learn physics at school and your life will be full of
          magic and miracles.
        </p>
        <br />
        <p>
          Day before yesterday I saw a rabbit, and yesterday a deer, and today,
          you.
        </p>
      </Modal>
    </>
  );
};

export default DataTable;
