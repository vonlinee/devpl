<template>
  <el-card>
    <el-button @click="showFieldImportModal">导入</el-button>
    <el-button @click="refreshTableData()">刷新</el-button>
  </el-card>
  <el-table border row-key="id" :data="tableData" height="100%">
    <el-table-column type="selection" width="35" align="center"></el-table-column>
    <el-table-column prop="groupName" label="组名称">
      <template #default="scope">
        <span>{{ scope.row.groupName }}</span>
        <span style="margin-left: 5px; color: red;">({{ scope.row.fieldCount || 0 }}个字段)</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="500" fixed="right">
      <template #default="scope">
        <el-button link @click="javaPojoGenModal.show(scope.row.id)">Java</el-button>
        <el-button link>转为模型</el-button>
        <el-button link @click="otherModal.show(scope.row.id)">其他</el-button>
        <el-button link @click="showFieldGroupEditModal(scope.row)">编辑</el-button>
        <el-button link @click="removeFieldGroup(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
    <template #empty>
      <el-text>暂无数据</el-text>
    </template>
  </el-table>
  <el-button class="mt-4" style="width: 100%" @click="onAddItem">新增字段组</el-button>

  <FieldGroupImport ref="importFieldGroupModal" @finished="onParseFinished"></FieldGroupImport>

  <JavaPojoGen ref="javaPojoGenModal"></JavaPojoGen>

  <Other ref="otherModal"></Other>

  <FieldGroupEdit ref="fieldGroupEditModalRef"></FieldGroupEdit>
</template>

<script lang="ts" setup>
import FieldGroupImport from "./FieldGroupImport.vue";
import { onMounted, ref } from "vue";
import {
  apiDeleteFieldGroup,
  apiNewFieldGroup,
  apiPageFieldGroup
} from "@/api/fields";
import JavaPojoGen from "@/views/fields/group/JavaPojoGen.vue";
import Other from "@/views/fields/group/Other.vue";
import { Message } from "@/hooks/message";
import FieldGroupEdit from "@/views/fields/group/FieldGroupEdit.vue";

const importFieldGroupModal = ref();
const tableData = ref<FieldGroup[]>([]);
const javaPojoGenModal = ref();
const otherModal = ref();
const fieldGroupEditModalRef = ref();

const showFieldImportModal = () => {
  importFieldGroupModal.value.show();
};

const onParseFinished = (fields: FieldInfo[]) => {
  if (fields && fields.length > 0) {
    apiNewFieldGroup(fields).then((res) => {
      if (res) {
        Message.info("添加字段组成功");
        refreshTableData();
      }
    });
  }
};

const showFieldGroupEditModal = (group?: FieldGroup) => {
  fieldGroupEditModalRef.value.show(group);
};

const refreshTableData = () => {
  apiPageFieldGroup().then((res) => {
    tableData.value = res.data;
  });
};

onMounted(() => {
  refreshTableData();
});

const onAddItem = () => {
  apiNewFieldGroup().then((res) => {
    if (res.data) {
      tableData.value.push(res.data);
    }
  });
};

const removeFieldGroup = (id: number) => {
  apiDeleteFieldGroup(id).then(() => {
    Message.info("删除成功");
    refreshTableData();
  });
};
</script>

<style scoped></style>
