import { TableColumnCtx } from "element-plus/es/components/table/src/table-column/defaults"
import { RendererElement, RendererNode, VNode } from "vue"

export interface DataTableOptions {
  /**
   * 是否开启loading
   */
  loading?: boolean
  elementLoadingText?: string
  elementLoadingSpinner?: string
  elementLoadingSvg?: string
  elementLoadingBackground?: string
  append?: string
  /**
   * table 列表的高度
   */
  height?: string | number
  /**
   * table 列表的最大高度
   */
  maxHeight?: string | number
  /**
   * 是否开启斑马线
   */
  stripe?: boolean
  /**
   * 是否开启table的border
   */
  border?: boolean
  /**
   * table 列表的大小
   */
  size?: "large" | "default" | "small"
  /**
   * table 列是否自动撑开
   */
  fit?: boolean
  /**
   * table 列表是否展示表头
   */
  showHeader?: boolean
  /**
   * 当前行的 key值
   */
  currentRowKey?: string | number
  /**
   * 是否高亮当前行
   */
  highlightCurrentRow?: boolean
  /**
   * 行的className
   */
  rowClassName?: string | ((row: any) => string)
  /**
   * 单元格的className
   */
  cellClassName?: string | ((row: any) => string)
  /**
   * 表头行的className
   */
  headerRowClassName?: string | ((row: any) => string)
  /**
   * 表头单元格的className
   */
  headerCellClassName?: string | ((row: any) => string)
  /**
   * 行数据的 Key
   */
  rowKey?: string | ((row: any) => string)
  /**
   * 没有数据时的提示文字
   */
  emptyText?: string
  /**
   * 是否默认展开所有行
   */
  defaultExpandAll?: boolean
  /**
   *  默认的排序列的 prop 和顺序
   */
  defaultSort?: {
    prop: string
    order?: "ascending" | "descending"
  }
  /**
   *  tooltip effect 属性
   */
  tooltipEffect?: "dark" | "light"
  /**
   * 是否在表尾显示合计行
   */
  showSummary?: boolean
  /**
   * 合计行第一列的文本
   */
  sumText?: string
  /**
   * 自定义的合计计算方法
   */
  summaryMethod?: (row: any) => {}
  /**
   * 合并行或列的计算方法
   */
  spanMethod?: (row: any) => {}
  /**
   * 在多选表格中，当仅有部分行被选中时，点击表头的多选框时的行为。
   * 若为 true，则选中所有行；若为 false，则取消选择所有行
   */
  selectOnIndeterminate?: boolean
  /**
   * 展示树形数据时，树节点的缩进
   */
  indent?: number
  /**
   *  是否懒加载子节点数据
   */
  lazy?: boolean
  /***
   * 加载子节点数据的函数
   */
  load?: (row: any, treeNode: any, resolve: any) => {}
  /**
   * 渲染嵌套数据的配置选项
   */
  treeProps?: { hasChildren: string; children: string }
}

/**
 * 列配置项
 */
export interface DataTableColumnProps {
  /**
   * 列自定义内容插槽名称
   */
  slot?: string
  /**
   * 列自定义标题插槽名称
   */
  headerSlot?: string
  type?: "index" | "selection" | "expand"
  index?: number | ((index: number) => number)
  label?: string
  columnKey?: string
  // 属性字段
  prop?: string
  width?: string | number
  minWidth?: string | number
  fixed?: "left" | "right"
  renderHeader?: (data: {
    column: TableColumnCtx<any>
    $index: number
  }) => VNode<RendererNode, RendererElement, { [key: string]: any }>
  sortable?: true | false | "custom"
  sortMethod?: (a: any, b: any) => number
  sortBy?: ((row: any, index: any) => string) | string | string[]
  sortOrders?: any[]
  resizable?: boolean
  formatter?: (row: any, column: any, cellValue: any, index: any) => any
  showOverflowTooltip?: boolean
  align?: "left" | "center" | "right"
  headerAlign?: "left" | "center" | "right"
  className?: string
  labelClassName?: string
  selectable?: (row: any, index: any) => boolean
  reserveSelection?: boolean
  filters?: any[]
  filterPlacement?:
  | "top"
  | "top-start"
  | "top-end"
  | "bottom"
  | "bottom-start"
  | "bottom-end"
  | "left"
  | "left-start"
  | "left-end"
  | "right"
  | "right-start"
  | "right-end"
  filterMultiple?: boolean
  filterMethod?: (value: any, row: any, column: any) => boolean
  filteredValue?: any[]
}
