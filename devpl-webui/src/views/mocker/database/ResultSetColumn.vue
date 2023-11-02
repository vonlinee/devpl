<template>
  <el-table-column :prop="columnName" :label="columnName" show-overflow-tooltip>
    <template v-slot:header="scope">
      <span class="result_set_header">{{ scope.column.label }}</span>
    </template>
  </el-table-column>
</template>
<script setup lang="ts">

interface ResultSetColumnProps {
  /**
   * 列名称
   */
  columnName: string,
  /**
   * 列数据类型: 根据数据类型决定图标样式
   */
  columnType: number
}

const { columnName, columnType } = defineProps<ResultSetColumnProps>();


</script>
<style lang="scss" scoped>
.result_set_header {
  word-break: break-all;
  // 文字长度超出限定宽度，则隐藏超出的内容
  overflow: hidden;
  // 设置文字在一行显示，不能换行
  // white-space: nowrap;
  // 规定当文本溢出时，显示省略符号来代表被修剪的文本
  // text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1; // 数字代表文字需要显示几行
}
</style>