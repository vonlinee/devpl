<template>
  <div style="height: 30px; border-style: dotted; display: flex; flex-direction: row;">
    <ResultSetTableCell v-for="column in columns" :columnName="column.name"></ResultSetTableCell>
  </div>
</template>

<script setup lang="ts">
import ResultSetTableCell from '../devtools/database/ResultSetTableCell.vue';

const { row, columns } = defineProps<{
  /**
   * 每行的对象
   */
  row: Record<string, any>,
  /**
   * 所有列
   */
  columns: any[]
}>()

</script>
