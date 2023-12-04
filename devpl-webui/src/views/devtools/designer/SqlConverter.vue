<!--
  输入为SQL(支持DDL，查询SQL等)，转换为其他，比如实体类
-->
<template>
  <div>
    <monaco-editor ref="editorRef" language="ts"></monaco-editor>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

const options = reactive({
  inputType: "DDL"
});

const editorRef = ref();

</script>