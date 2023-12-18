<template>
  <el-table ref="tableRef" border :data="tableData">
    <el-table-column label="列1" prop="id"></el-table-column>
    <el-table-column label="列2" prop="name"></el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { useDraggable } from "vue-draggable-plus";
import { onMounted, ref } from "vue";

const tableData = ref([{
  id: 1,
  name: "A"
}, {
  id: 1,
  name: "A"
}]);

const tableRef = ref()

onMounted(() => {
  const tableBody = tableRef.value.$refs.tableBody.querySelector("tbody");
  useDraggable(tableBody)
})


</script>

<style scoped>

</style>