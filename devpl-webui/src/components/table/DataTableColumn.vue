<template>
  <el-table-column
    :type="column.type"
    :index="column.index"
    :label="column.label"
    :column-key="column['column-key']"
    :prop="column.prop"
    :width="column.width"
    :min-width="column['min-width']"
    :fixed="column.fixed"
    :render-header="column['render-header']"
    :sortable="column.sortable"
    :sort-method="column['sort-method']"
    :sort-by="column['sort-by']"
    :sort-orders="column['sort-orders']"
    :resizable="column.resizable"
    :formatter="column.formatter"
    :show-overflow-tooltip="column['show-overflow-tooltip']"
    :align="column.align"
    :header-align="column['header-align']"
    :class-name="column['class-name']"
    :label-class-name="column['label-class-name']"
    :selectable="column.selectable"
    :reserve-selection="column['reserve-selection']"
    :filters="column.filters"
    :filter-placement="column['filter-placement']"
    :filter-multiple="column['filter-multiple']"
    :filter-method="column['filter-method']"
    :filtered-value="column['filtered-value']"
  >
    <!-- (START) 当slot-header有值时使用插槽类型 -->
    <template v-if="column['header-slot']" #header="scope">
      <slot name="header" :row="scope.row" :column="scope.column" :index="scope.index"></slot>
    </template>
    <!-- (END) 当slot-header有值时使用插槽类型 -->
    <!-- (START) 当slot有值时使用插槽类型 -->
    <template v-if="column.slot" #default="scope">
      <slot name="default" :row="scope.row" :column="scope.column" :index="scope.index"></slot>
    </template>
    <!-- (END) 当slot有值时使用插槽类型 -->
  </el-table-column>
</template>
<script lang="ts" setup>
import { PropType } from 'vue'
import { DataTableColumnProps } from './interface'

const props = defineProps({
  column: {
    type: Object as PropType<DataTableColumnProps>,
    default: () => {},
  },
})

</script>

