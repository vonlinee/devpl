<template>
  <el-button @click="importFieldGroupModal.show()">导入</el-button>

  <FieldGroupImport ref="importFieldGroupModal"></FieldGroupImport>

  <el-table border row-key="id" :data="tableData">
    <el-table-column type="selection" width="35" align="center"></el-table-column>
    <el-table-column prop="groupName" label="组名称"></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
    <template #empty>
      <el-text>暂无数据</el-text>
    </template>
  </el-table>

</template>

<script lang="ts" setup>
import FieldGroupImport from "./FieldGroupImport.vue";
import { ref } from "vue";

const importFieldGroupModal = ref();
const tableData = ref()


</script>

<style scoped>

</style>