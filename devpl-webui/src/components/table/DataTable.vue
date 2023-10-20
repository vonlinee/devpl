<template>
  <div>
    <!-- 头部工具栏 -->
    <div v-if="config.tools && config.tools.length > 0 && config.tools[0].label" class="data-table-toolbar">
      <template v-for="(tool, key) in config.tools">
        <!-- 具名插槽 -->
        <slot v-if="tool.slot" :name="tool.slot" />
        <el-button v-else :key="key" :type="tool.type" :size="tool.size" :plain="tool.plain" :round="tool.round"
          :circle="tool.circle" :icon="tool.icon" :style="tool.style" :disabled="tool.disabled"
          @click.native.prevent="tool.onClick">
          {{ tool.label }}
        </el-button>
      </template>
    </div>

    <el-table v-loading="options.loading" :element-loading-text="options.elementLoadingText"
      :element-loading-spinner="options.elementLoadingSpinner" :element-loading-svg="options.elementLoadingSvg"
      :element-loading-background="options.elementLoadingBackground" ref="tableRef" class="el-table" :data="tableDataRef"
      :height="options.height == undefined ? 616 : options.height" :max-height="`${options.maxHeight}px`"
      :stripe="options.stripe == undefined ? true : options.stripe"
      :border="options.border == undefined ? true : options.border"
      :size="options.size == undefined ? 'default' : options.size" :fit="options.fit == undefined ? false : options.fit"
      :show-header="options.showHeader == undefined ? true : options.showHeader"
      :current-row-key="options.currentRowKey == undefined ? '' : options.currentRowKey"
      :highlight-current-row="options.highlightCurrentRow == undefined ? true : options.highlightCurrentRow"
      :empty-text="options.emptyText == undefined ? '' : options.emptyText" :row-key="options.rowKey"
      :row-class-name="options.rowClassName" :cell-class-name="options.cellClassName"
      :tooltip-effect="options.tooltipEffect == undefined ? 'light' : options.tooltipEffect"
      :show-summary="options.showSummary == undefined ? false : options.showSummary"
      :sum-text="options.sumText == undefined ? '' : options.sumText"
      :select-on-indeterminate="options.selectOnIndeterminate == undefined ? false : options.selectOnIndeterminate"
      :indent="options.indent == undefined ? 2 : options.indent" :lazy="options.lazy == undefined ? true : options.lazy"
      :load="options.load" :tree-props="options.treeProps" @select="select" @select-all="selectAll"
      @selection-change="selectionChange" @cell-mouse-enter="cellMouseEnter" @cell-mouse-leave="cellMouseLeave"
      @cell-click="cellClick" @cell-dblclick="cellDblclick" @cell-contextmenu="cellContextmenu" @row-click="rowClick"
      @row-contextmenu="rowContextmenu" @row-dblclick="rowDblclick" @header-click="headerClick"
      @header-contextmenu="headerContextmenu" @sort-change="sortChange" @filter-change="filterChange"
      @current-change="currentChange" @header-dragend="headerDragend" @expand-change="expandChange">
      <template v-for="item in columns">
        <data-table-column :column="item">
          <!-- 当slot-header有值时使用插槽类型 -->
          <template v-if="item.headerSlot" #header="scope">
            <slot :name="item.headerSlot" :column="scope.column" :index="scope.index"></slot>
          </template>
          <!-- 当slot有值时使用插槽类型 -->
          <template v-if="item.slot" #default="scope">
            <slot :name="item.slot" :row="scope.row" :column="scope.column" :index="scope.index"></slot>
          </template>
        </data-table-column>
      </template>
      <template #append>
        <slot name="append">

        </slot>
      </template>

      <el-table-column label="操作" fixed="right" width="140px" :sortable="false" :resizable="false" header-align="center"
        align="center">
        <template #default="scope">
          <el-button type="primary" @click="handleEditOperation(scope.$index, scope.row)" :icon="Edit" />
          <el-button type="danger" @click="handleDelete(scope.$index, scope.row)" :icon="Delete" />
        </template>
      </el-table-column>
    </el-table>

    <vxe-modal v-model="modalVisiable" :title="modalTitle" width="800" height="600" show-footer>
      <template #default>
        <slot name="modal" :form="formObject"></slot>
      </template>
      <template #footer>
        <div>
          <el-button type="primary" @click="saveOrUpdate(false, false)">取消</el-button>
          <el-button type="success" @click="saveOrUpdate(false, true)">确定</el-button>
        </div>
      </template>
    </vxe-modal>

    <el-pagination background layout="prev, pager, next, jumper, sizes, total" v-model:current-page="currentPageIndex"
      v-model:page-size="currentPageSize" :page-sizes="pageSizeList" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, toRaw } from "vue";
import { ElMessage, ElMessageBox, ElTable } from "element-plus";
import { DataTableColumnProps, DataTableOptions } from "./interface";
import { Delete, Edit } from "@element-plus/icons-vue";
import DataTableColumn from "@/components/table/DataTableColumn.vue";

// 表格行数据模型
type RowDataModel = Record<string, any>
// 表单数据模型
type FormDataModel = Record<string, any>

/**
 * 接口配置项
 */
export interface DataTableApiOptions {
  /**
   * 分页查询
   * @param pageIndex 页码
   * @param pageSize 每页记录条数
   * @param params 自定义查询参数
   */
  queryPage?: (pageIndex: number, pageSize: number, params: any) => Promise<any>,
  /**
   * 删除单条记录
   * @param row 数据行
   */
  deleteOne?: (row: RowDataModel) => Promise<any>,
  /**
   * 更新单条记录
   * @param row 数据行
   */
  update?: (row: RowDataModel) => Promise<any>,
  /**
   * 保存单条记录
   * @param row 数据行
   */
  save?: (row: RowDataModel) => Promise<any>
}

/**
 * 工具栏按钮属性
 */
export interface ToolBarButtonProps {
  label: string;
  circle?: boolean;
  icon?: string;
  style?: string;
  disabled?: boolean;
  round?: boolean;
  plain?: boolean;
  size?: string | number;
  slot?: string;
  type?: string;
  onClick?: (event: Event) => void;
}

/**
 * DataTable表单配置
 */
export interface DataTableFormOptions {
  /**
   * 表单数据对象，用于绑定表单数据项的值
   */
  formData: FormDataModel,
  /**
   * 展示弹窗之前调用
   * 表单和选中的行是独立的，修改表单不会直接修改表格行数据
   * @param row 选中的行，可能为null，表示新增
   * @param formData 之前的表单数据，如果row为null，则formData为初始化传入的formData值
   * @returns 返回即将显示在表单上的数据
   */
  editConverter: (row: RowDataModel, formData: FormDataModel) => FormDataModel,
  /**
   * 重置表单，弹窗关闭时进行重置表单
   * @param formData 当前表单数据
   */
  resetConverter: (formData: FormDataModel) => FormDataModel
}

/**
 * 组件表格的总配置项
 */
export interface DataTableConfig {
  tableData: RowDataModel[];
  columns: DataTableColumnProps[];
  options: DataTableOptions;
  api: DataTableApiOptions;
  form: DataTableFormOptions;
  /**
   * 是否开启分页组件
   */
  pageable: boolean;
  /**
   * 是否开启工具栏
   */
  toolbar: boolean,
  /**
   * 工具栏按钮属性
   */
  tools: ToolBarButtonProps[]
}

/**
 * 属性定义
 */
const { config } = defineProps<{
  config: DataTableConfig
}>();

const { columns, options, api, form, toolbar, pageable } = config;

// 窗口显示状态
const modalVisiable = ref<boolean>(false);
// 当前操作的行数据
const currentRow: RowDataModel = ref<RowDataModel>();
// 表单对象 和当前行的数据关联
const formObject = ref(config.form.formData);
// 分页大小列表
const pageSizeList = [10, 20, 30, 40];
// 当前页码
const currentPageIndex = ref(1);
// 当前每页数据条数
const currentPageSize = ref(10);
// 总记录数
const total = ref(5);
// 弹窗标题
const modalTitle = ref("新增");
// Element-Plus Table Ref
const tableRef = ref();
// 表格数据
const tableDataRef = ref<any[]>([]);
if (config.tableData) {
  tableDataRef.value = config.tableData;
}

/**
 * 展示消息
 * @param msg 要展示的消息文本
 * @param failed 操作是否失败，如果成功，消息提醒完毕后会关闭弹窗，如果失败仅提示失败消息
 */
const showMessage = (msg: string, failed: boolean) => {
  if (failed) {
    ElMessage({
      message: msg,
      type: 'error',
      center: true,
      duration: 600
    });
  } else {
    ElMessage({
      message: msg,
      type: 'info',
      center: true,
      duration: 600,
      onClose: () => {
        modalVisiable.value = false;
      }
    });
  }
}

/**
 * 更新或者保存
 * @param showModal 弹窗显示状态
 * @param okOrCancel true表示确认，false表示取消
 */
const saveOrUpdate = (showModal: boolean, okOrCancel: boolean) => {
  if (showModal) {
    modalVisiable.value = showModal;
  } else {
    if (okOrCancel) {
      // 关闭弹窗
      if ("新增" == modalTitle.value) {
        if (api?.save) {
          api?.save(currentRow.value).then((res: any) => {
            if (res.code == 200) {
              showMessage("保存成功", false)
            } else {
              showMessage("保存失败", true)
            }
          });
        }
      } else if ("修改" == modalTitle.value) {
        if (api?.update) {
          api?.update(currentRow.value).then((res: any) => {
            if (res.code == 200) {
              showMessage("修改成功", false)
            } else {
              showMessage("修改失败", true)
            }
          });
        } else {
          modalVisiable.value = showModal;
        }
      }
    } else {
      modalVisiable.value = showModal;
    }
  }
};

/**
 * 刷新表格数据，更新分页信息
 * @param pageIndex 第几页，从1开始
 * @param pageSize 每页数据条数
 */
const refreshTableData = (pageIndex: number, pageSize: number) => {
  if (api?.queryPage) {
    api?.queryPage(pageIndex, pageSize, null).then(((res: any) => {
      if (res.code == 200) {
        total.value = res.data.total;
        tableDataRef.value = res.data.list;
      }
    }));
  }
};

/**
 * 分页大小改变
 * @param val
 */
const handleSizeChange = (val: number) => {
  refreshTableData(currentPageIndex.value, val);
};

/**
 * 当前页码改变回调
 * @param val
 */
const handleCurrentChange = (val: number) => {
  refreshTableData(val, currentPageSize.value);
};

// 初始化时加载数据
onMounted(() => {
  refreshTableData(1, 10);
});

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
 * 编辑操作 打开编辑弹窗
 * @param index
 * @param row
 */
const handleEditOperation = (index: number, row: any) => {
  if (row) {
    modalTitle.value = "修改";
  }
  let newformObject = form.editConverter(toRaw(row), formObject.value);
  if (newformObject) {
    formObject.value = newformObject;
  }
  saveOrUpdate(true, true);
};

// 删除操作
const handleDelete = (index: number, row: any) => {
  ElMessageBox.confirm(
    "是否删除第" + (index + 1) + "行?",
    "确认删除",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning"
    }
  ).then(() => {
    if (api?.deleteOne) {
      api?.deleteOne(toRaw(row)).then((res) => {
        if (res.code == 200) {
          console.log(row.toString());
          ElMessage.info("删除" + row + "成功");
        } else {
          ElMessage.error("删除" + row + "失败");
        }
      });
    }
  }).catch(() => {
    console.log("取消删除", index);
  });
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

<style scoped>
.el-pagination {
  text-align: right;
}
</style>