<template>
  <el-table
    :data="rows"
    width="100%"
    :border="true"
    stripe
    table-layout="auto"
    :show-overflow-tooltip="true"
    :hegiht="600"
    highlight-current-row
  >
    <el-table-column
      type="selection"
      width="35"
      fixed="left"
      :resizable="false"
    />
    <result-set-column
      v-for="(item, index) in headers"
      :key="index"
      :column-name="item.columnName"
      :column-type="item.columnType"
    ></result-set-column>
  </el-table>
</template>
<script setup lang="ts">
import ResultSetColumn from "./ResultSetColumn.vue"

interface ResultSetTableProps {
  headers: ResultSetColumnMetadata[]
  rows: any[]
}

const props = defineProps<ResultSetTableProps>()
</script>
<style lang="scss" scoped>
.el-table th > .cell {
  white-space: nowrap;
}
</style>
