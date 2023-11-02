<template>
  <el-table-column :prop="columnName" :label="columnName" show-overflow-tooltip></el-table-column>
</template>
<script setup lang='ts'>

interface Props {
  /**
   * 列名称
   */
  columnName: string,
  /**
   * 列数据类型: 根据数据类型决定图标样式
   */
  columnType: number
}

const { columnName, columnType } = defineProps<Props>()

</script>
<style lang='scss' scoped></style>