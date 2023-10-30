import { IHooksOptions } from "@/hooks/interface"
import http from "@/utils/http"
import { onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"

/**
 * CRUD Hooks
 * @param options
 */
export const useCrud = (options: IHooksOptions) => {
  /**
   * 默认选项
   */
  const defaultOptions: IHooksOptions = {
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
    pageSizes: [10, 20, 50, 100],
    dataListLoading: false,
    dataListSelections: [],
  }

  /**
   * 合并配置选项与默认选项
   * @param options
   * @param props
   */
  const mergeDefaultOptions = (options: any, props: any): IHooksOptions => {
    for (const key in options) {
      if (!Object.getOwnPropertyDescriptor(props, key)) {
        props[key] = options[key]
      }
    }
    return props
  }

  // 覆盖默认值
  const state: IHooksOptions = mergeDefaultOptions(defaultOptions, options)

  onMounted(() => {
    if (state.createdIsNeed) {
      query()
    }
  })

  // 查询
  const query = () => {
    if (!state.dataListUrl) {
      return
    }
    state.dataListLoading = true
    if (state.query) {
      state.query(
          state.page ? state.page : 1,
          state.limit ? state.limit : 10,
          state.queryForm
        )
        .then((res) => {
          // 分页
          state.dataList = res.data
          state.total = res.total == undefined ? -1 : res.total
        })
    } else {
      http.get(state.dataListUrl, {
          order: state.order,
          asc: state.asc,
          page: state.isPage ? state.page : null,
          limit: state.isPage ? state.limit : null,
          ...state.queryForm,
        })
        .then((res) => {
          state.dataList = res.data
          state.total = res.total == undefined ? -1 : res.total
        })
    }

    state.dataListLoading = false
  }

  /**
   * 默认查询回调
   */
  const getDataList = (): void => {
    state.page = 1
    query()
  }

  /**
   * 每页大小变化回调，默认查第一页
   * @param val
   */
  const sizeChangeHandle = (val: number): void => {
    state.page = 1
    state.limit = val
    query()
  }

  /**
   * 当前页变化回调
   * @param val
   */
  const currentChangeHandle = (val: number): void => {
    state.page = val
    query()
  }

  /**
   * 多选回调
   * @param selections
   */
  const selectionChangeHandle = (selections: any[]): void => {
    state.dataListSelections = selections.map(
      (item: any) => state.primaryKey && item[state.primaryKey]
    )
  }

  /**
   * 排序回调
   * @param data
   */
  const sortChangeHandle = (data: any): void => {
    const { prop, order } = data
    if (prop && order) {
      state.order = prop
      state.asc = order === "ascending"
    } else {
      state.order = ""
    }
    query()
  }

  /**
   * 删除回调
   * @param key
   */
  const deleteHandle = (key: number | string): void => {
    if (!state.deleteUrl) {
      return
    }
    ElMessageBox.confirm("确定进行删除操作?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        http.delete(state.deleteUrl + "/" + key).then(() => {
          ElMessage.success("删除成功")
          query()
        })
      })
      .catch((err) => {
        console.error("删除错误", err)
      })
  }

  /**
   * 批量删除回调
   * @param key
   */
  const deleteBatchHandle = (key?: number | string) => {
    let data: any[] = []
    if (key) {
      data = [key]
    } else {
      data = state.dataListSelections ? state.dataListSelections : []
      if (data.length === 0) {
        ElMessage.warning("请选择删除记录")
        return
      }
    }
    ElMessageBox.confirm("确定进行删除操作?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
        if (state.deleteUrl) {
          http.delete(state.deleteUrl, { data }).then(() => {
            ElMessage.success("删除成功")
            query()
          })
        }
      })
      .catch((err) => {
        console.error("删除错误", err)
      })
  }

  return {
    getDataList,
    sizeChangeHandle,
    currentChangeHandle,
    selectionChangeHandle,
    sortChangeHandle,
    deleteHandle,
    deleteBatchHandle,
  }
}
