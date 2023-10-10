<!-- 
  https://juejin.cn/post/7177758799951265851
 -->
<template>
  <!-- 是否显示表头的全选 -->
  <div :class="{ isSingle: props.isSingle }">
    <el-table :header-cell-style="props.headerCellStyle" :header-row-style="props.headerRowStyle" :data="props.tableData"
      :max-height="props.maxHeight" :size="props.size" ref="tableRef" style="width: 100%"
      :highlight-current-row="props.highlightCurrentRow" @current-change="changeCurrent" @selection-change="changeSelect"
      @select="changeSelectTap" @select-all="changeSelectAll" :row-key="props.rowKey"
      :tree-props="props.treeProps || treeProps">
      <!-- 是否开启多选功能 -->
      <el-table-column v-if="props.isSelect" :reserve-selection="true" type="selection" width="50" />
      <!-- 遍历表头数据 -->
      <template v-for="(item, index) in props.header">
        <el-table-column :key="index" :align="item.align || 'center'" :sortable="item.sortable" :width="item.width"
          :min-width="item.minWidth" :label="item.label" :prop="item.prop" :fixed="item.fixed"
          v-if="item.isShow == false ? false : true" show-overflow-tooltip>
          <!-- 自定义行slot -->
          <template v-if="item.isCustom" #default="scope">
            <slot :name="item.prop" :row="scope.row" :column="scope.column" :index="scope.$index"></slot>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <!-- 分页，传了分页数据就会展示 -->
    <div v-if="page" class="mt15 flex-center">
      <el-pagination :currentPage="props.page?.pageIndex" :page-size="props.page?.pageSize" :page-sizes="[10, 20, 30]"
        layout="sizes, prev, pager, next, jumper" :total="props.page?.total" :background="true"
        :hide-on-single-page="false" @size-change="changeSize" @current-change="changePage">
      </el-pagination>
    </div>
  </div>
</template>

<script lang="ts" setup>

import { ref } from "vue";
import { ElTable, ElPagination } from "element-plus";

//表格实例
const tableRef = ref();

//表格数据的配置项
interface Header {
  label: string;
  prop?: string;
  width?: string | number;
  minWidth?: string | number;
  sortable?: boolean;
  align?: string;
  isCustom?: boolean;
  fixed?: string;
  isShow?: boolean;
}

//分页数据配置项
interface Page {
  total?: number;
  pageSize?: number;
  pageIndex?: number;
  background?: boolean;
}

//props配置项
interface Props {
  tableData: Array<any>; //表格数据
  page?: Page; //分页数据
  isSelect?: boolean; //是否支持多选
  isSingle?: boolean; //是否隐藏全选
  size?: "large" | "default" | "small"; //table的尺寸
  header: Array<Header>; //表头数据
  maxHeight?: string | number;
  headerRowStyle?: Function | object;
  headerCellStyle?: Function | object;
  highlightCurrentRow?: boolean;
  rowKey?: string,
  treeProps?: Function | object
}

const treeProps = { children: "children", hasChildren: "hasChildren" };

//设置props默认配置项
const props = withDefaults(defineProps<Props>(), {
  isSelect: false,
  isOperation: true,
  size: "default",
  highlightCurrentRow: false,
  rowKey: "id",
  isSingle: false
});


// 每页条数 改变时触发
const changeSize = (size: number) => {
  emit("changeSize", size);
};

//翻页时触发
const changePage = (page: number) => {
  emit("changePage", page);
};

// 选择行
const changeCurrent = (currentRow: any, oldCurrentRow: any) => {
  emit("changeCurrent", { currentRow, oldCurrentRow });
};
// 不管全选还是勾选都会触发
const changeSelect = (val: any) => {
  emit("onSelect", val);
};
// 手动全选
const changeSelectAll = (val: any) => {
  emit("onSelectAll", val);
};
// 手动勾选
const changeSelectTap = (val: any, row: any) => {
  emit("onSelectTap", val, row);
};
//让父组件可以获取到table实例
defineExpose({ tableRef });
//注册事件
const emit = defineEmits(["changeSize", "changePage", "changeCurrent", "onSelect", "onSelectTap", "onSelectAll"]);
</script>

<style lang="scss" scoped>
.isSingle {
  :deep(*) {
    .el-table__header {
      .el-checkbox {
        visibility: hidden;
      }
    }
  }
}
</style>
