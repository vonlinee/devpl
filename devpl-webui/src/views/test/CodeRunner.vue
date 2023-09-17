<template>
  <el-select v-model="value" class="m-2" placeholder="Select" size="large">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
  <monaco-editor language="JavaScript" height="500px"></monaco-editor>
</template>

<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { ref } from "vue";

const value = ref("");

const options = [
  {
    value: "Option1",
    label: "Option1"
  },
  {
    value: "Option2",
    label: "Option2"
  },
  {
    value: "Option3",
    label: "Option3"
  },
  {
    value: "Option4",
    label: "Option4"
  },
  {
    value: "Option5",
    label: "Option5"
  }
];


</script>

<style scoped>

</style>
