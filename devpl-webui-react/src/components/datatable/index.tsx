import React, { RefObject, useEffect, useMemo, useRef, useState } from "react";
import { Button, Modal, Table } from "antd";
import type {
  ColumnsType,
  TablePaginationConfig,
  TableProps,
} from "antd/es/table";
import { AnyObject } from "antd/es/_util/type";
import { FilterValue } from "antd/es/table/interface";

/**
 * 行数据模型
 */
export declare type RowDataModel = AnyObject;

/**
 * 组件ref
 */
interface DataTableRef {
  // 打开弹窗
  showModal: (row: RowDataModel) => void;
}

interface TableParams {
  pagination?: TablePaginationConfig;
  sortField?: string;
  sortOrder?: string;
  filters?: Record<string, FilterValue>;
}

/**
 * 后端接口配置项
 */
export interface ApiConfig {
  // 分页查询
  queryPage?: (page: number, limit: number, params: AnyObject) => Promise<any>;
  // 根据ID删除
  deleteById?: (id: React.Key) => Promise<any>;
  // 根据ID更新
  updateById?: (id: React.Key) => Promise<any>;
}

/**
 * 选项
 */
export declare interface DataTableOptions {
  // 列定义
  columns: ColumnsType<RowDataModel>;
  // 是否可分页
  pageable: boolean;
  // 表单数据
  formData?: AnyObject | RowDataModel,
  // 数据
  data?: TableProps<RowDataModel>[];
  // 组件的ref引用
  tableRef?: RefObject<DataTableRef>;
  // 后端接口配置
  api?: ApiConfig;
  // 修改弹窗
  modalContent: (param: AnyObject) => JSX.Element;
}

/**
 * DataTable 属性
 */
export declare interface DataTableProps {
  options: DataTableOptions;
}

/**
 * 组件定义
 * @param props
 * @returns
 */
const DataTable = React.forwardRef<DataTableRef, DataTableProps>(
  (props: DataTableProps, ref) => {
    const { options } = props;

    // 弹窗状态
    const [open, setOpen] = useState(false);
    // 分页属性
    const [currentPage, setCurrentPage] = useState(1);
    const [currentPageSize, setCurrentPageSize] = useState(10);
    const [total, setTotal] = useState(0);

    const [data, setData] = useState<RowDataModel[]>();
    const [loading, setLoading] = useState(false);
    const [tableParams, setTableParams] = useState<TableParams>({
      pagination: {
        current: 1,
        pageSize: 10,
      },
    });

    if (options.api != undefined) {
      let queryByPage = options.api.queryPage;
      if (queryByPage != null) {
        useEffect(() => {
          setLoading(true);
          // @ts-ignore
          queryByPage(currentPage, currentPageSize, {}).then((res: any) => {
            if (res.code == 200) {

              res.data.list = res.data.list.map((i: any) => {
                i.key = i.id
                return i
              })

              setData(res.data.list);
              setLoading(false);
              setTableParams({
                ...tableParams,
                pagination: {
                  ...tableParams.pagination,
                  total: res.data.total,
                },
              });
            }
          });
        }, []);
      }
    }

    // 向父组件暴露子组件的数据和方法
    React.useImperativeHandle(ref, () => ({
      showModal: (row: RowDataModel) => {
        setOpen(true);
      },
    }));

    const handleOk = (e: React.MouseEvent<HTMLElement>) => {
      if (options.api?.updateById) {
        options.api?.updateById(1).then((res) => {
          console.log("更新结果", res);
        });
      }
      setOpen(false);
    };

    const handleCancel = (e: React.MouseEvent<HTMLElement>) => {
      setOpen(false);
    };

    const pagination: TablePaginationConfig = {
      total: total,
      onShowSizeChange(current, size) {},
      onChange(page, pageSize) {
        if (options.api?.queryPage) {
          options.api
            ?.queryPage(page, pageSize, {
              name: "zs",
            })
            .then((res) => {
              console.log("查询结果", res);
            });
        }
      },
    };

    const ModalContent : JSX.Element  = props.options.modalContent({})

    return (
      <>
        <Table
          bordered={true}
          columns={props.options.columns}
          dataSource={data}
          pagination={tableParams.pagination}
          loading={loading}
        />
        <Modal
          title={<div>修改</div>}
          destroyOnClose={true}
          centered={true}
          open={open}
          onOk={handleOk}
          onCancel={handleCancel}
        >
          {ModalContent}
        </Modal>
      </>
    );
  }
);
export default DataTable;
