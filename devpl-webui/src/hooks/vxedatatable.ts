import { UnwrapNestedRefs, reactive } from "vue"
import { VxeGridProps } from "vxe-table"
import { VxeTableDefines, VxeTablePropTypes } from "vxe-table/types/table"

/**
 * 行数据模型: 任意普通对象
 */
export declare type RowModel = Record<string, any>

/**
 * vxe-grid 选项
 */
export interface VDTOptions {
  // 是否边框
  border?: boolean
  // 高度
  height?: VxeTablePropTypes.Height
  // 是否启用分页
  pageEnabled?: boolean
  // 列配置项
  columns: VxeTableDefines.ColumnOptions<RowModel>[],
  // 查询分页数据
  queryPage?: (currentPage: number, pageSize: number) => any
}

/**
 * vxe-table配置项
 * https://vxetable.cn/v4/#/table/grid/basic
 */
export const useVxeGridTable = (
  options: VDTOptions
): UnwrapNestedRefs<VxeGridProps<RowModel>> => {
  
  const _options: VxeGridProps<RowModel> = {
    border: options.border ? options.border : true,
    keepSource: true,
    height: options.height ? options.height : 500,
    printConfig: {},
    importConfig: {},
    exportConfig: {},
    columnConfig: {
      resizable: true,
    },
    pagerConfig: {
      perfect: true,
      pageSize: 15,
      enabled: options.pageEnabled == undefined ? true : options.pageEnabled
    },
    editConfig: {
      trigger: "click",
      mode: "row",
      showStatus: true,
      // 是否显示列头编辑图标
      showIcon: true
    },
    toolbarConfig: {
      buttons: [
        {
          code: "insert_actived",
          name: "新增",
          status: "perfect",
          icon: "vxe-icon-add",
        },
        {
          code: "mark_cancel",
          name: "标记/取消",
          status: "perfect",
          icon: "vxe-icon-delete",
        },
        {
          code: "save",
          name: "保存",
          status: "perfect",
          icon: "vxe-icon-save",
        },
      ],
      perfect: true,
      refresh: {
        icon: "vxe-icon-refresh",
        iconLoading: "vxe-icon-refresh roll",
      },
      zoom: {
        iconIn: "vxe-icon-fullscreen",
        iconOut: "vxe-icon-minimize",
      },
      custom: {
        icon: "vxe-icon-menu",
      },
    },
    proxyConfig: {
      autoLoad: true,
      props: {
        result: "result",
        total: "page.total",
      },
      ajax: {
        // 接收 Promise
        query: ({ page }) => {
          console.log("queryPage");
          if (options && options.queryPage) {
            return options.queryPage(page.currentPage, page.pageSize)
          }
          return new Promise(() => undefined)
        },
        // body 对象： { removeRecords }
        delete: ({ body }) => {
          console.log("delete");
          return new Promise(() => 1);
        },
        // body 对象： { insertRecords, updateRecords, removeRecords, pendingRecords }
        save: ({ body }) => {
          console.log("save");
          
          return new Promise(() => 1);
        },
      },
    },
    columns: options.columns,
  }

  return reactive<VxeGridProps<RowModel>>(_options)
}
