<!--
  表格面板：包含查询条件，表格，分页等
-->
<template>
  <el-card>
    <slot name="form"></slot>
  </el-card>
  <el-table> </el-table>
</template>

<script lang="ts">
import { defineComponent } from "vue"

const TablePane = defineComponent({
  setup: () => {},
})
</script>

<style lang="scss" scoped></style>
