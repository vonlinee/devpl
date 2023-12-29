<template>
  <ElPlusDataTable
    ref="tableRef"
    :page="tableData.page"
    :is-select="true"
    :row-key="'id'"
    :header="header"
    :table-data="
      tableData.data.slice(
        (tableData.page.pageIndex - 1) * tableData.page.pageSize,
        tableData.page.pageIndex * tableData.page.pageSize
      )
    "
  >
    <template #sex="scope">
      {{ scope.row.sex == "0" ? "男" : "女" }}
    </template>
    <template #operation="scope">
      <el-button @click="handelEdit(scope.row)">编辑</el-button>
      <el-button @click="handelDel(scope.row)">删除</el-button>
    </template>
  </ElPlusDataTable>
</template>

<script lang="ts" setup>
import ElPlusDataTable from "@/components/datatable/ElPlusDataTable.vue"
import { ref, reactive, onMounted } from "vue"

const tableRef = ref()
// 表头
const header = reactive([
  {
    label: "姓名",
    prop: "name",
  },
  {
    label: "性别",
    prop: "sex",
    isCustom: true,
  },
  {
    label: "年龄",
    prop: "age",
  },
  {
    label: "操作",
    prop: "operation",
    isCustom: true,
    fixed: "right",
    align: "center",
    width: 200,
  },
])
// 表格数据
const tableData = reactive({
  data: [],
  searchForm: {},
  page: {
    pageIndex: 1,
    pageSize: 10,
    total: 500,
  },
})
const handelEdit = (val: any) => {
  console.log("编辑")
}
const handelDel = (val: any) => {
  console.log("删除")
}
onMounted(() => {
  //模拟请求数据
  setTimeout(() => {
    //随机生成一些表格数据
    for (let i = 0; i < 500; i++) {
      let obj = {
        id: i,
        name: `c${i + 1}`,
        sex: parseInt(String(Math.random() * 2)),
        age: Math.round(Math.random() * 100),
      }
      tableData.data.push(obj)
    }
  }, 1500)
})
</script>
<style scoped></style>
