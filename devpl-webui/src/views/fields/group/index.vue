<template>
  <el-button @click="showFieldImportModal">导入</el-button>

  <FieldGroupImport ref="importFieldGroupModal"></FieldGroupImport>
  <el-card>

  </el-card>
  <el-table border row-key="id" :data="tableData">
    <el-table-column type="selection" width="35" align="center"></el-table-column>
    <el-table-column prop="groupName" label="组名称"></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-button @click="javaPojoGenModal.show(scope.row.id)">Java</el-button>
        <el-button>SQL</el-button>
        <el-button>转为模型</el-button>
      </template>
    </el-table-column>
    <template #empty>
      <el-text>暂无数据</el-text>
    </template>
  </el-table>

  <JavaPojoGen ref="javaPojoGenModal"></JavaPojoGen>
</template>

<script lang="ts" setup>
import FieldGroupImport from "./FieldGroupImport.vue";
import { onMounted, ref } from "vue";
import { apiPageFieldGroup } from "@/api/fields";
import JavaPojoGen from "@/views/fields/group/JavaPojoGen.vue";

const importFieldGroupModal = ref();
const tableData = ref();
const javaPojoGenModal = ref();

const showFieldImportModal = () => {
  importFieldGroupModal.value.show();
};

onMounted(() => {
  apiPageFieldGroup().then((res) => {
    tableData.value = res.data;
  });
});
</script>

<style scoped>

</style>