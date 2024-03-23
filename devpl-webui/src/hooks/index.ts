import { DataTableOption } from "@/hooks/interface";
import http from "@/utils/http";
import { isReactive, onMounted, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Message } from "./message";

/**
 * CRUD Hooks
 * 
 * TODO 解决类型声明问题
 * @param options
 */
export const useCrud = (options: DataTableOption) => {

  if (!isReactive(options)) {
    console.error("option of crud hook should be reactive")
  }

  /**
   * 默认选项
   */
  const defaultOptions: DataTableOption = {
    createdIsNeed: true,
    dataListUrl: "",
    isPage: true,
    deleteUrl: "",
    primaryKey: "id",
    exportUrl: "",
    queryForm: {},
    dataList: [],
    order: "",
    asc: false,
    page: 1,
    limit: 10,
    total: 0,
    confirmBeforeDelete: false,
    pageSizes: [10, 20, 50, 100],
    dataListLoading: false,
    dataListSelections: []
  };

  /**
   * 合并配置选项与默认选项
   * @param options
   * @param props
   */
  const mergeDefaultOptions = (options: any, props: any): DataTableOption => {
    for (const key in options) {
      if (!Object.getOwnPropertyDescriptor(props, key)) {
        props[key] = options[key];
      }
    }
    return props;
  };

  // 覆盖默认值
  let option: DataTableOption = mergeDefaultOptions(defaultOptions, options);

  /**
   * 需要在组件内部调用
   */
  onMounted(() => {
    if (option.createdIsNeed) {
      query();
    }
  });

  // 查询
  const query = () => {
    if (!option.dataListUrl && !option.queryPage) {
      return;
    }
    option.dataListLoading = true;
    if (option.queryPage) {
      option.queryPage(
        option.page ? option.page : 1,
        option.limit ? option.limit : 10,
        option.queryForm
      ).then((res) => {
        // 分页
        option.dataList = res.data;
        option.total = res.total == undefined ? -1 : res.total;
      });
    } else if (option.dataListUrl) {
      http.get(option.dataListUrl, {
        order: option.order,
        asc: option.asc,
        page: option.isPage ? option.page : null,
        limit: option.isPage ? option.limit : null,
        ...option.queryForm
      }).then((res) => {
        option.dataList = res.data;
        option.total = res.total == undefined ? -1 : res.total;
      });
    }
    option.dataListLoading = false;
  };

  /**
   * 默认查询回调
   */
  const getDataList = (): void => {
    option.page = 1;
    query();
  };

  /**
   * 每页大小变化回调，默认查第一页
   * @param val
   */
  const sizeChangeHandle = (val: number): void => {
    option.page = 1;
    option.limit = val;
    query();
  };

  /**
   * 当前页变化回调
   * @param val
   */
  const currentChangeHandle = (val: number): void => {
    option.page = val;
    query();
  };

  /**
   * 多选回调
   * @param selections
   */
  const selectionChangeHandle = (selections: any[]): void => {
    option.dataListSelections = selections.map(
      (item: any) => option.primaryKey && item[option.primaryKey]
    );
  };

  /**
   * 排序回调
   * @param data
   */
  const sortChangeHandle = (data: any): void => {
    const { prop, order } = data;
    if (prop && order) {
      option.order = prop;
      option.asc = order === "ascending";
    } else {
      option.order = "";
    }
    query();
  };

  /**
   * 单行删除回调
   * @param key
   */
  const deleteHandle = (key: number | string): void => {
    if (!option.deleteUrl) {
      if (!option.removeByIds) {
        return;
      }
    }
    if (option.confirmBeforeDelete) {
      ElMessageBox.confirm("确定进行删除操作?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          if (option.removeByIds) {
            option.removeByIds([key]).then((res) => {
              ElMessage.success("删除成功");
              query();
            });
          } else {
            http.delete(option.deleteUrl + "/" + key).then(() => {
              ElMessage.success("删除成功");
              query();
            });
          }
        })
        .catch((err) => {
          // console.error("删除错误", err);
        });
    } else {
      if (option.removeByIds) {
        option.removeByIds([key]).then((res) => {
          ElMessage.success("删除成功");
          query();
        });
      } else {
        http.delete(option.deleteUrl + "/" + key).then(() => {
          ElMessage.success("删除成功");
          query();
        });
      }
    }
  };

  /**
   * 批量删除回调
   * @param key
   */
  const deleteBatchHandle = (key?: number | string) => {
    let data: any[] = [];
    if (key) {
      data = [key];
    } else {
      data = option.dataListSelections ? option.dataListSelections : [];
      if (data.length === 0) {
        Message.warn("请选择删除记录");
        return;
      }
    }
    ElMessageBox.confirm("确定进行删除操作?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
      .then(() => {
        if (!option.deleteUrl) {
          if (option.removeByIds) {
            option.removeByIds(data).then((res) => {
              Message.info("删除成功")
              query();
            });
          }
        } else {
          http.delete(option.deleteUrl, data).then(() => {
            Message.info("删除成功")
            query();
          });
        }
      })
      .catch((err) => {
        console.error("删除错误", err);
      });
  };

  return {
    getDataList,
    sizeChangeHandle,
    currentChangeHandle,
    selectionChangeHandle,
    sortChangeHandle,
    deleteHandle,
    deleteBatchHandle
  };
};
