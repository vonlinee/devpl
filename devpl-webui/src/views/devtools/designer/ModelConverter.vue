<!--
  输入为Schema类，输出可以是SQL或者其他Schema
-->
<template>
  <div>
    <monaco-editor language="java" height="600px" />
  </div>
</template>

<script setup lang="ts">

import { ref } from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

const visible = ref();

</script>

<style scoped>

</style>