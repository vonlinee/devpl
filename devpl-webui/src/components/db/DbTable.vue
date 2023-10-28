<script setup lang="ts">

interface Column {
  // 列
  title: string;
  // 列的字段
  prop: string;
}

const columnList = [
  { prop: "name", label: "姓名" },
  { prop: "name", label: "姓名" },
  { prop: "name", label: "姓名" },
  { prop: "name", label: "姓名" },
];
const testDatas = [
  { No: 1, name: "张三", age: 24, city: "广州", tel: "13312345678" },
  { No: 2, name: "李四", age: 25, city: "九江", tel: "18899998888" },
  { No: 3, name: "王五", age: 26, city: "六盘水", tel: "13600001111" },
  { No: 4, name: "赵二", age: 27, city: "菏泽", tel: "13145209420" }
];

</script>

<template>
  <el-table :data="testDatas" border stripe style="width: 100%">
    <el-table-column type="index" label="No." :width="50" />
    <el-table-column
      v-for="col in columnList"
      :prop="col.prop"
      :label="col.label"
      :key="col.prop"
    />
  </el-table>

</template>

<style scoped lang="scss">

</style>