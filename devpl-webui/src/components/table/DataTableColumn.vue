<template>
  <el-table-column
    :type="column.type"
    :index="column.index"
    :label="column.label"
    :column-key="column.columnKey"
    :prop="column.prop"
    :width="column.width"
    :min-width="column.minWidth"
    :fixed="column.fixed"
    :render-header="column.renderHeader"
    :sortable="column.sortable"
    :sort-method="column.sortMethod"
    :sort-by="column.sortBy"
    :sort-orders="column.sortOrders"
    :resizable="column.resizable"
    :formatter="column.formatter"
    :show-overflow-tooltip="column.showOverflowTooltip"
    :align="column.align"
    :header-align="column.headerAlign"
    :class-name="column.className"
    :label-class-name="column.labelClassName"
    :selectable="column.selectable"
    :reserve-selection="column.reserveSelection"
    :filters="column.filters"
    :filter-placement="column.filterPlacement"
    :filter-multiple="column.filterMultiple"
    :filter-method="column.filterMethod"
    :filtered-value="column.filteredValue"
  >
    <!-- (START) 当slot-header有值时使用插槽类型 -->
    <template v-if="column.headerSlot" #header="scope">
      <slot
        name="header"
        :row="scope.row"
        :column="scope.column"
        :index="scope.index"
      ></slot>
    </template>
    <!-- (END) 当slot-header有值时使用插槽类型 -->
    <!-- (START) 当slot有值时使用插槽类型 -->
    <template v-if="column.slot" #default="scope">
      <slot
        name="default"
        :row="scope.row"
        :column="scope.column"
        :index="scope.index"
      ></slot>
    </template>
    <!-- (END) 当slot有值时使用插槽类型 -->
  </el-table-column>
</template>
<script lang="ts" setup>
import { PropType } from "vue"
import { DataTableColumnProps } from "./interface"

const props = defineProps({
  column: {
    type: Object as PropType<DataTableColumnProps>,
    default: () => {},
  },
})
</script>
