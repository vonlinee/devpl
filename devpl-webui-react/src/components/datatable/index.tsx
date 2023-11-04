import React, { RefObject, useEffect, useState } from "react";
import { Modal, Table } from "antd";
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
 * 表单数据模型
 */
export declare type DataTableFormObject = AnyObject | RowDataModel;

/**
 * 组件ref
 */
export interface DataTableRef<R = AnyObject, F = DataTableFormObject> {
  // 打开弹窗
  showModal: (row: R) => void;
}

/**
 * 表格参数
 */
interface TableParams {
  /**
   * antd表格组件
   */
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
 * 表单配置
 */
export interface FormConfig {
  /**
   * 表单数据绑定对象
   */
  mode: AnyObject
}

/**
 * 配置选项
 * @param R 行数据类型
 * @param F 表单数据类型
 */
export declare interface DataTableOptions<
  R = RowDataModel,
  F = AnyObject | RowDataModel
> {
  // 列定义
  columns: ColumnsType<R>;
  // 是否可分页
  pageable: boolean;
  // 表单配置
  formConfig?: FormConfig
  // 表单数据
  formData?: F;
  // 数据
  data?: TableProps<R>[];
  // 组件的ref引用, ref在DataTable组件上
  tableRef?: RefObject<DataTableRef<R, F>>;
  // 后端接口配置
  api?: ApiConfig;
  // 修改弹窗
  modalRender: (param: AnyObject) => JSX.Element;
}

/**
 * DataTable 属性
 */
export declare interface DataTableProps<
  R = RowDataModel,
  F = DataTableFormObject
> {
  // 配置项
  options: DataTableOptions<R, F>;
}

/**
 * 组件定义
 * @param props
 * @returns
 */
const DataTable = React.forwardRef<
  DataTableRef<AnyObject, AnyObject>,
  DataTableProps
>((props: DataTableProps, ref) => {
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
    if (queryByPage != null && queryByPage instanceof Function) {
      useEffect(() => {
        setLoading(true);
        // @ts-ignore
        queryByPage(currentPage, currentPageSize, {}).then((res: any) => {
          if (res.code == 2000) {
            res.data = res.data.map((i: any) => {
              i.key = i.id;
              return i;
            });

            setData(res.data);
            setLoading(false);
            setTableParams({
              ...tableParams,
              pagination: {
                ...tableParams.pagination,
                total: res.total,
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
    console.log("表单数据", options.formData?.getFieldsValue());

    options.formData?.setFieldsValue({
      connName: "111111112",
      driverType: "Oracle",
      host: "12012012"
    })
    // if (options.api?.updateById) {
    //   options.api?.updateById(1).then((res) => {
    //     console.log("更新结果", res);
    //   });
    // }
    // setOpen(false);
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

  // 弹窗表单组件
  const ModalContent: JSX.Element = props.options.modalRender({});

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
        okText="确定"
        cancelText="取消"
        onOk={handleOk}
        onCancel={handleCancel}
      >
        {ModalContent}
      </Modal>
    </>
  );
});
export default DataTable;
