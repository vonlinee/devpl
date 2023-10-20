<template>
  <div>
    <el-table v-loading="config.loading"
              :element-loading-text="config['element-loading-text']"
              :element-loading-spinner="config['element-loading-spinner']"
              :element-loading-svg="config['element-loading-svg']"
              :element-loading-background="config['element-loading-background']"
              ref="tableRef"
              class="el-table"
              :data="tableData"
              :height="config.height"
              :max-height="`${config['max-height']}px`"
              :stripe="config.stripe"
              :border="config.border"
              :size="config.size"
              :fit="config.fit" :show-header="config['show-header']"
              :current-row-key="config['current-row-key']"
              :highlight-current-row="config['highlight-current-row']"
              :empty-text="config['empty-text']"
              :row-key="config['row-key']"
              :row-class-name="config['row-class-name']"
              :cell-class-name="config['cell-class-name']"
              :default-sort="config['default-sort']"
              :tooltip-effect="config['tooltip-effect']"
              :show-summary="config['show-summary']"
              :sum-text="config['sum-text']"
              :summary-method="config['summary-method']"
              :span-method="config['span-method']"
              :select-on-indeterminate="config['select-on-indeterminate']"
              :indent="config.indent"
              :lazy="config.lazy"
              :load="config.load"
              :tree-props="config['tree-props']"
              @select="select"
              @select-all="selectAll"
              @selection-change="selectionChange"
              @cell-mouse-enter="cellMouseEnter"
              @cell-mouse-leave="cellMouseLeave"
              @cell-click="cellClick"
              @cell-dblclick="cellDblclick"
              @cell-contextmenu="cellContextmenu"
              @row-click="rowClick"
              @row-contextmenu="rowContextmenu"
              @row-dblclick="rowDblclick"
              @header-click="headerClick"
              @header-contextmenu="headerContextmenu"
              @sort-change="sortChange"
              @filter-change="filterChange"
              @current-change="currentChange"
              @header-dragend="headerDragend"
              @expand-change="expandChange">
      <template v-for="item in columns">
        <a-table-column :column="item">
          <!-- 当slot-header有值时使用插槽类型 -->
          <template v-if="item['header-slot']" #header="scope">
            <slot :name="item['header-slot']" :column="scope.column" :index="scope.index"></slot>
          </template>
          <!-- 当slot有值时使用插槽类型 -->
          <template v-if="item.slot" #default="scope">
            <slot :name="item.slot" :row="scope.row" :column="scope.column" :index="scope.index"></slot>
          </template>
          <!-- (END) 当slot有值时使用插槽类型 -->
        </a-table-column>
      </template>
      <template #append>
        <slot name="append">

        </slot>
      </template>

      <el-table-column label="操作" fixed="right" width="140px" :sortable="false" :resizable="false"
                       header-align="center"
                       align="center">
        <template #default="scope">
          <el-button type="primary" @click="handleEditOperation(scope.$index, scope.row)" :icon="Edit" />
          <el-button type="danger" @click="handleDelete(scope.$index, scope.row)" :icon="Delete" />
        </template>
      </el-table-column>
    </el-table>

    <vxe-modal v-model="modalVisiable" :title="modalTitle">
      <slot name="modal" :form="currentRow">

      </slot>
      <template #footer>
        <el-button type="primary" @click="setModalVisiable(false)">取消</el-button>
        <el-button type="primary" @click="setModalVisiable(false)">确定</el-button>
      </template>
    </vxe-modal>

    <el-pagination background layout="prev, pager, next, jumper, total" :total="1000" />
  </div>
</template>

<script lang="ts" setup>
import { PropType, ref, toRaw } from "vue";
import { ElTable } from "element-plus";
import { DataTableColumnProps, DataTableOptions } from "./interface";
import ATableColumn from "./DataTableColumn.vue";
import { Delete, Edit } from "@element-plus/icons-vue";

// 窗口显示状态
const modalVisiable = ref(false);
// 当前操作的行数据
const currentRow = ref();

const modalTitle = ref("新增");

const setModalVisiable = (val: boolean) => {
  modalVisiable.value = val;
};

// 表格数据
const tableData = ref<any[]>([]);

/**
 * 属性定义
 */
const props = defineProps({
  // table上的相关配置信息
  config: {
    type: Object as unknown as typeof ElTable,
    default: {} as typeof ElTable
  },
  // table的数据源
  dataSource: {
    type: Array
  },
  // table-column上的相关信息的配置
  columns: {
    type: Array as PropType<DataTableColumnProps[]>,
    default: () => [] as DataTableColumnProps[]
  }
});

if (props.dataSource) {
  tableData.value = props.dataSource;
}

/**
 * 事件定义
 */
const emit = defineEmits([
  "select",
  "select-all",
  "selection-change",
  "cell-mouse-enter",
  "cell-mouse-leave",
  "cell-click",
  "cell-dblclick",
  "cell-contextmenu",
  "row-click",
  "row-contextmenu",
  "row-dblclick",
  "header-click",
  "header-contextmenu",
  "sort-change",
  "filter-change",
  "current-change",
  "header-dragend",
  "expand-change"
]);

// 当用户手动勾选数据行的 Checkbox 时触发的事件
const select = (selection: any, row: any) => {
  emit("select", selection, row);
};
// 当用户手动勾选全选 Checkbox 时触发的事件
const selectAll = (selection: any) => {
  emit("select-all", selection);
};
// 当选择项发生变化时会触发该事件
const selectionChange = (selection: any) => {
  emit("selection-change", selection);
};
// 当单元格 hover 进入时会触发该事件
const cellMouseEnter = (row: any, column: any, cell: any, event: any) => {
  emit("cell-mouse-enter", row, column, cell, event);
};
// 当单元格 hover 退出时会触发该事件
const cellMouseLeave = (row: any, column: any, cell: any, event: any) => {
  emit("cell-mouse-leave", row, column, cell, event);
};
// 当某个单元格被点击时会触发该事件
const cellClick = (row: any, column: any, cell: any, event: any) => {
  emit("cell-click", row, column, cell, event);
};
// 当某个单元格被双击击时会触发该事件
const cellDblclick = (row: any, column: any, cell: any, event: any) => {
  emit("cell-dblclick", row, column, cell, event);
};
// 当某个单元格被鼠标右键点击时会触发该事件
const cellContextmenu = (row: any, column: any, cell: any, event: any) => {
  emit("cell-contextmenu", row, column, cell, event);
};
// 当某一行被点击时会触发该事件
const rowClick = (row: any, cell: any, event: any) => {
  emit("row-click", row, cell, event);
};
// 当某一行被鼠标右键点击时会触发该事件
const rowContextmenu = (row: any, cell: any, event: any) => {
  emit("row-contextmenu", row, cell, event);
};
// 当某一行被双击时会触发该事件
const rowDblclick = (row: any, cell: any, event: any) => {
  emit("row-dblclick", row, cell, event);
};
// 当某一列的表头被点击时会触发该事件
const headerClick = (cell: any, event: any) => {
  emit("header-click", cell, event);
};
// 当某一列的表头被鼠标右键点击时触发该事件
const headerContextmenu = (cell: any, event: any) => {
  emit("header-contextmenu", cell, event);
};
// 当表格的排序条件发生变化的时候会触发该事件
const sortChange = ({ column, prop, order }: any) => {
  emit("sort-change", { column, prop, order });
};
// column 的 key， 如果需要使用 filter-change 事件，则需要此属性标识是哪个 column 的筛选条件
const filterChange = (filters: any) => {
  emit("filter-change", filters);
};
// 当表格的当前行发生变化的时候会触发该事件，如果要高亮当前行，请打开表格的 highlight-current-row 属性
const currentChange = (currentRow: any, oldCurrentRow: any) => {
  emit("current-change", currentRow, oldCurrentRow);
};
// 当拖动表头改变了列的宽度的时候会触发该事件
const headerDragend = (newWidth: any, oldWidth: any, column: any, event: any) => {
  emit("header-dragend", newWidth, oldWidth, column, event);
};
// 当用户对某一行展开或者关闭的时候会触发该事件（展开行时，回调的第二个参数为 expandedRows；
// 树形表格时第二参数为 expanded）
const expandChange = (row: any, expanded: any) => {
  emit("expand-change", row, expanded);
};
// ? Table Events (END)
// ? Table Methods (START)
const tableRef = ref();
// 用于多选表格，清空用户的选择
const clearSelection = () => {
  tableRef.value.clearSelection();
};
// 用于多选表格，切换某一行的选中状态， 如果使用了第二个参数，
// 则是设置这一行选中与否（selected 为 true 则选中）
const toggleRowSelection = (row: any, selected: any) => {
  tableRef.value.toggleRowSelection(row, selected);
};
// 用于多选表格，切换全选和全不选
const toggleAllSelection = () => {
  tableRef.value.toggleAllSelection();
};
// 用于可扩展的表格或树表格，如果某行被扩展，则切换。
// 使用第二个参数，您可以直接设置该行应该被扩展或折叠。
const toggleRowExpansion = (row: any, expanded: any) => {
  tableRef.value.toggleRowExpansion(row, expanded);
};
// 用在单选表中，设置某一行被选中。
// 如果不带任何参数调用，它将清除选择。
const setCurrentRow = (row: any) => {
  tableRef.value.setCurrentRow(row);
};
// 清除排序，将数据恢复到原来的顺序
const clearSort = () => {
  tableRef.value.clearSort();
};
// 清除传入的列的过滤器columnKey。如果没有参数，则清除所有过滤器
const clearFilter = (columnKeys: any) => {
  tableRef.value.clearFilter(columnKeys);
};

// 手动排序表。属性prop用于设置排序列，属性order用于设置排序顺序
const sort = (prop: string, order: string) => {
  tableRef.value.sort(prop, order);
};

/**
 * 编辑操作
 * @param index
 * @param row
 */
const handleEditOperation = (index: number, row: any) => {
  console.log("edit", index, row);
  if (row) {
    modalTitle.value = "修改";
  }
  currentRow.value = toRaw(row);
  setModalVisiable(true);
};

// 删除操作
const handleDelete = (index: number, row: any) => {
  console.log("delete", index, row);
};

defineExpose({
  clearSelection,
  toggleRowSelection,
  toggleAllSelection,
  toggleRowExpansion,
  setCurrentRow,
  clearSort,
  clearFilter,
  sort
});
</script>

