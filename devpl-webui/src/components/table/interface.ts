import { TableColumnCtx } from "element-plus/es/components/table/src/table-column/defaults";
import { RendererElement, RendererNode, VNode } from "vue";

export interface DataTableOptions {
  /**
   * 是否开启loading
   */
  loading?: boolean;
  "element-loading-text"?: string;
  "element-loading-spinner"?: string;
  "element-loading-svg"?: string;
  "element-loading-background"?: string;
  append?: string;
  /**
   * table 列表的高度
   */
  height?: string | number;
  /**
   * table 列表的最大高度
   */
  "max-height"?: string | number;
  /**
   * 是否开启斑马线
   */
  stripe?: boolean;
  /**
   * 是否开启table的border
   */
  border?: boolean;
  /**
   * table 列表的大小
   */
  size?: "large" | "default" | "small";
  /**
   * table 列是否自动撑开
   */
  fit?: boolean;
  /**
   * table 列表是否展示表头
   */
  "show-header"?: boolean;
  /**
   * 当前行的 key值
   */
  "current-row-key"?: string | number;
  /**
   * 是否高亮当前行
   */
  "highlight-current-row"?: boolean;
  /**
   * 行的className
   */
  "row-class-name"?: string | ((row: any) => string);
  /**
   * 单元格的className
   */
  "cell-class-name"?: string | ((row: any) => string);
  /**
   * 表头行的className
   */
  "header-row-class-name"?: string | ((row: any) => string);
  /**
   * 表头单元格的className
   */
  "header-cell-class-name"?: string | ((row: any) => string);
  /**
   * 行数据的 Key
   */
  "row-key"?: string | ((row: any) => string);
  /**
   * 没有数据时的提示文字
   */
  "empty-text"?: string;
  /**
   * 是否默认展开所有行
   */
  "default-expand-all"?: boolean;
  /**
   *  默认的排序列的 prop 和顺序
   */
  "default-sort"?: {
    prop: string
    order?: "ascending" | "descending"
  };
  /**
   *  tooltip effect 属性
   */
  "tooltip-effect"?: "dark" | "light";
  /**
   * 是否在表尾显示合计行
   */
  "show-summary"?: boolean;
  /**
   * 合计行第一列的文本
   */
  "sum-text"?: string;
  /**
   * 自定义的合计计算方法
   */
  "summary-method"?: (row: any) => {};
  /**
   * 合并行或列的计算方法
   */
  "span-method"?: (row: any) => {};
  /**
   * 在多选表格中，当仅有部分行被选中时，点击表头的多选框时的行为。
   * 若为 true，则选中所有行；若为 false，则取消选择所有行
   */
  "select-on-indeterminate"?: boolean;
  /**
   * 展示树形数据时，树节点的缩进
   */
  indent?: number;
  /**
   *  是否懒加载子节点数据
   */
  lazy?: boolean;
  /***
   * 加载子节点数据的函数
   */
  load?: (row: any, treeNode: any, resolve: any) => {};
  /**
   * 渲染嵌套数据的配置选项
   */
  "tree-props"?: { hasChildren: string; children: string };
}

export interface DataTableColumnProps {
  slot?: string;
  "header-slot"?: string;
  type?: "index" | "selection" | "expand";
  index?: number | ((index: number) => number);
  label?: string;
  "column-key"?: string;
  prop?: string;
  width?: string | number;
  "min-width"?: string | number;
  fixed?: "left" | "right";
  "render-header"?: (data: {
    column: TableColumnCtx<any>
    $index: number
  }) => VNode<RendererNode, RendererElement, { [key: string]: any }>;
  sortable?: true | false | "custom";
  "sort-method"?: (a: any, b: any) => number;
  "sort-by"?: ((row: any, index: any) => string) | string | string[];
  "sort-orders"?: any[];
  resizable?: boolean;
  formatter?: (row: any, column: any, cellValue: any, index: any) => any;
  "show-overflow-tooltip"?: boolean;
  align?: "left" | "center" | "right";
  "header-align"?: "left" | "center" | "right";
  "class-name"?: string;
  "label-class-name"?: string;
  selectable?: (row: any, index: any) => boolean;
  "reserve-selection"?: boolean;
  filters?: any[];
  "filter-placement"?:
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
    | "right-end";
  "filter-multiple"?: boolean;
  "filter-method"?: (value: any, row: any, column: any) => boolean;
  "filtered-value"?: any[];
}
