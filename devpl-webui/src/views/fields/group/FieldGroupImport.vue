<template>
  <vxe-modal v-model="visible" title="导入字段分组" show-footer width="80%" height="80%">
    <el-row>
      <el-col :span="12">
        <el-tabs v-model="activeName" class="demo-tabs">
          <el-tab-pane label="JSON" name="first">User</el-tab-pane>
          <el-tab-pane label="Config" name="second">Config</el-tab-pane>
          <el-tab-pane label="Role" name="third">Role</el-tab-pane>
          <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :span="12">
        <FieldTreeTable></FieldTreeTable>
      </el-col>
    </el-row>
    <template #footer>
      <vxe-button>解析</vxe-button>
      <vxe-button>取消</vxe-button>
      <vxe-button status="primary">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>

import FieldTreeTable from "@/components/fields/FieldTreeTable.vue";
import { ref } from "vue";

const visible = ref();
const activeName = ref();

defineExpose({
  show() {
    visible.value = true;
  }
});

</script>

<style scoped>

</style>