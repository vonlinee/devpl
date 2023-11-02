<template>
  <el-table :data="tableData" style="width: 100%" :border="true" stripe>
    <el-table-column type="selection" width="35" fixed="left" :resizable="false"/>
    <el-table-column :prop="index" :label="item" v-for="(item, index) in tableHeader" :key="index" show-overflow-tooltip>
    </el-table-column>
  </el-table>
</template>
<script setup lang='ts'>

interface ColumnDef {
  prop: string
}

const tableHeader = {
  name: "姓名",
  birth: "生日",
  address: "地址",
  age: "年龄",
  phone: "电话",
}

const heads = Object.values(tableHeader)
const props = Object.keys(tableHeader)

const tableData = [{
  name: '张三',
  date: '2016-05-02',
  address: '上海市普陀区金沙江路 1518 弄',
  age: 18,
  phone: "12345678910",
}, {
  date: '2016-05-04',
  name: '李四',
  address: '上海市普陀区金沙江路 1517 弄',
  age: 19,
  phone: "12345678911",
}, {
  date: '2016-05-01',
  name: '王五',
  address: '上海市普陀区金沙江路 1519 弄',
  age: 20,
  phone: "12345678912",
}, {
  date: '2016-05-03',
  name: '赵六',
  address: '上海市普陀区金沙江路 1516 弄',
  age: 21,
  phone: "12345678913",
}]




</script>
<style lang='scss' scoped></style>