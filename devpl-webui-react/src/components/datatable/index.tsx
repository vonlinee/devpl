import React, { useEffect, useState } from "react";
import { Button, FormInstance, Modal, Table } from "antd";
import type {
  ColumnsType,
  TablePaginationConfig,
  TableProps,
} from "antd/es/table";
import { AnyObject } from "antd/es/_util/type";
import { FilterValue } from "antd/es/table/interface";
import { EditOutlined, DeleteOutlined } from "@ant-design/icons";

/**
 * 表格行数据类型
 */
export declare type RowDataType = AnyObject;

/**
 * 新增/编辑表单数据模型
 */
export declare type DataTableFormType = AnyObject | RowDataType;

/**
 * 查询参数数据类型
 */
export declare type DataTableQueryParamType = AnyObject;

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
export interface ApiConfig<R = RowDataType, P = DataTableQueryParamType> {
  // 分页查询
  queryPage?: (page: number, limit: number, params: P) => Promise<any>;
  // 根据ID删除
  deleteById?: (id: React.Key) => Promise<any>;
  // 根据ID更新
  updateById?: (id: React.Key) => Promise<any>;
  // 单条更新
  update?: (row: R) => Promise<any>;
}

/**
 * 表单配置
 */
export interface FormConfig<R = RowDataType, F = DataTableFormType> {
  // 表单数据绑定对象
  instance: FormInstance<F>;
  // 编辑的当前行
  currentRow?: R;
  // 编辑时填充表单
  rowToForm?: (row: R) => F;
  // 编辑表单提交时，表单数据转换为行数据类型
  formToRow?: (row: R | undefined, form: F) => R;
}

/**
 * 配置选项
 * @param R 行数据类型
 * @param F 表单数据类型
 */
export declare interface DataTableOptions<
  R = RowDataType,
  F = DataTableFormType
> {
  // 列定义
  columns: ColumnsType<R>;
  // 是否可分页
  pageable: boolean;
  // 表单配置
  formConfig: FormConfig<R, F>;
  // 数据
  data?: TableProps<R>[];
  // 后端接口配置
  api?: ApiConfig;
  // 修改弹窗
  modalRender: (formInstance: FormInstance<F>) => JSX.Element;
}

/**
 * DataTable 属性
 */
export declare interface DataTableProps<
  R = RowDataType,
  F = DataTableFormType
> {
  // 配置项
  options: DataTableOptions<R, F>;
}

/**
 * 组件定义
 * @param props
 * @returns
 */
const DataTable = <R extends RowDataType, F extends Object = DataTableFormType>(
  props: DataTableProps<R, F>
) => {
  const { api, formConfig, columns } = props.options;

  // 弹窗状态
  const [open, setOpen] = useState(false);
  // 分页属性
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageSize, setCurrentPageSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [data, setData] = useState<RowDataType[]>();
  const [loading, setLoading] = useState(false);
  const [tableParams, setTableParams] = useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 10,
    },
  });

  /**
   * 显示编辑或新增弹窗
   * @param record
   */
  const showModal = (record: R) => {
    setOpen(true);
    // 填充表单值
    formConfig.currentRow = record;
    if (!formConfig.rowToForm) {
      formConfig.rowToForm = (row) => {
        const formValues = {} as F;
        Object.assign(formValues, row);
        return formValues;
      };
    }
    if (record) {
      const values = formConfig.rowToForm(record) as any;
      formConfig.instance.setFieldsValue(values);
    }
  };

  // 添加操作列
  useEffect(() => {
    if (columns.find((col) => col.key === "operation") == null) {
      columns.push({
        title: "操作",
        key: "operation",
        fixed: "right",
        align: "center",
        width: 20,
        render: (value: any, record: R, index: number) => {
          return (
            <div className="datatable-operation-container">
              <Button
                key="dt_btn_edit"
                type="text"
                icon={<EditOutlined></EditOutlined>}
                onClick={(e) => showModal(record)}
              />
              <Button
                key="dt_btn_del"
                type="text"
                icon={<DeleteOutlined></DeleteOutlined>}
              />
            </div>
          );
        },
      });
    }
  }, [columns]);

  if (api != undefined) {
    let queryByPage = api.queryPage;
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

  /**
   * 新增或编辑弹窗提交
   * @param e
   */
  const handleEditSubmit = (e: React.MouseEvent<HTMLElement>) => {
    const fieldValue = formConfig.instance.getFieldsValue();
    if (api?.update) {
      if (formConfig.currentRow) {
        if (!formConfig.formToRow) {
          formConfig.formToRow = (row, form) => {
            if (!row) {
              row = {} as R;
            }
            Object.assign(row, form);
            return row;
          };
        }
        // 调用接口
        api
          ?.update(formConfig.formToRow(formConfig.currentRow, fieldValue))
          .then((res) => {
            if (res.code == 2000) {
              setOpen(false);
            }
          });
      }
    }
  };

  /**
   * 新增或编辑弹窗取消或者关闭
   * @param e
   */
  const handleCancel = (e: React.MouseEvent<HTMLElement>) => {
    setOpen(false);
  };

  const pagination: TablePaginationConfig = {
    total: total,
    onShowSizeChange(current, size) {},
    onChange(page, pageSize) {
      if (api?.queryPage) {
        api
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
  const ModalContent: JSX.Element = props.options.modalRender(
    formConfig?.instance
  );

  return (
    <>
      <Button>新增</Button>
      <Table<R>
        bordered={true}
        columns={props.options.columns}
        dataSource={data}
        pagination={tableParams.pagination}
        loading={loading}
      />
      <Modal
        forceRender={true}
        title="修改"
        destroyOnClose={true}
        centered={true}
        open={open}
        okText="确定"
        cancelText="取消"
        onOk={handleEditSubmit}
        onCancel={handleCancel}
      >
        {ModalContent}
      </Modal>
    </>
  );
};
export default DataTable;
