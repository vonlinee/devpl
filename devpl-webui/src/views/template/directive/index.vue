<!-- 
  模板指令管理
 -->
<template>
  <el-button @click="saveOrUpdateRef.show()">
</el-button>

  <MonacoEditor ref="editor" language="java" height="500px"></MonacoEditor>

  <el-button @click="getSample">获取示例</el-button>

  <save-or-update-directive ref="saveOrUpdateRef"></save-or-update-directive>
</template>
<script lang='ts' setup>
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { apiGetCustomTemplateDirectiveExample } from "@/api/template";
import { ref } from "vue";
import SaveOrUpdateDirective from "@/views/template/directive/SaveOrUpdateDirective.vue";

const editor = ref();
const saveOrUpdateRef = ref();

const getSample = () => {
  apiGetCustomTemplateDirectiveExample().then((res) => editor.value.setText(res.data));
};
</script>
<style lang="scss" scoped>
</style>