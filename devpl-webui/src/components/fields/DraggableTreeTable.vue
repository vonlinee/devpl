<template>
  <el-table
    ref="tableRef"
    :data="tableData"
    row-key="id"
    style="width: 100%"
    :tree-props="tableTreeProps"
  >
    <el-table-column prop="date" label="Date" width="180" />
    <el-table-column prop="name" label="Name" width="180" />
    <el-table-column
      fixed="right"
      label="操作"
      width="204"
      class-name="operationCloumn"
    >
      <template #default="scope">
        <span @click="deleteClick(scope.row)">编辑</span>
        <span @click="deleteClick(scope.row)">{{
          scope.row.enable ? "停用" : "启用"
        }}</span>
        <span @click="deleteClick(scope.row)">删除</span>
        <i class="moveIcon iconfont icon-yidong-16px"></i>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, ref } from "vue"
import { useTableDragSort } from "./common"
const { initDragSort, dragEnd } = useTableDragSort()

const tableTreeProps = {}

const tableData = ref<any[]>([
  {
    name: "AAAAAA",
    date: "2022",
  },
  {
    name: "BBBBBB",
    date: "2023",
  },
])

const deleteClick = (row: any) => {}

onMounted(() => {
  const tableBodyDom = document.querySelector(
    ".el-table .el-table__body-wrapper .el-scrollbar__wrap tbody"
  ) as HTMLElement
  initDragSort(tableBodyDom, tableData.value)
})
dragEnd(async (newTableData: any[]) => {
  console.log(newTableData)
  tableData.value = newTableData

  // 拖拽后由于改变了表格的dom顺序，所以需要重新初始化
  await nextTick()
  const tableBodyDom = document.querySelector(
    ".el-table .el-table__body-wrapper .el-scrollbar__wrap tbody"
  ) as HTMLElement
  initDragSort(tableBodyDom, tableData.value)
})
</script>

<style scoped>
tbody {
  position: relative;
}
</style>
