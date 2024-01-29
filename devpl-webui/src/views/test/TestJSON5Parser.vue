<script setup lang="ts">

import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { ref } from "vue";
import JSON5 from "json5";

const res = ref();
const editor = ref();

const parse = () => {
  let text = editor.value.getText();
  res.value = JSON5.parse(text, function(key, value) {
    console.log(key, value);
  });
};

</script>

<template>
  <monaco-editor ref="editor" language="json" height="500px"></monaco-editor>

  <el-button @click="parse">parse
  </el-button>

  {{ res }}
</template>

<style scoped lang="scss">

</style>