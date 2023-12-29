<!--代码区域-->
<script setup lang="ts">

import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { ref } from "vue";

const { lang } = defineProps<{
  lang: string
}>();

const languageMode = ref(lang);

defineExpose({});

</script>

<template>
  <div style="display: inline-block">
    <el-select>
      <el-option value="Java"></el-option>
      <el-option value="TypeScript"></el-option>
    </el-select>
    <el-button>格式化</el-button>
    <el-button>复制</el-button>
  </div>
  <monaco-editor :language="languageMode" height="500px"></monaco-editor>
</template>

<style scoped lang="scss">

</style>