<template>
  <el-table :data="rows" :border="true" stripe table-layout="auto" :show-overflow-tooltip="true"
            highlight-current-row>
    <el-table-column type="selection" width="35" fixed="left" :resizable="false" />
    <result-set-column :columnName="item.columnName" :column-type="item.columnType" v-for="(item, index) in headers"
                       :key="index"></result-set-column>
  </el-table>
</template>
<script setup lang="ts">
import { ResultSetColumnMetadata } from "./type";
import ResultSetColumn from "./ResultSetColumn.vue";

interface Props {
  headers: ResultSetColumnMetadata[],
  rows: []
}

const props = defineProps<Props>();

</script>
<style lang="scss" scoped>
.el-table th > .cell {
  white-space: nowrap;
}
</style>