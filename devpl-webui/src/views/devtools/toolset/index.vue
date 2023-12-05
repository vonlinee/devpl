<template>
  <el-tabs v-model="activeTabName">
    <el-tab-pane label="模型转换">
      <model-converter></model-converter>
    </el-tab-pane>
    <el-tab-pane label="SQL转换">
      <sql-converter></sql-converter>
    </el-tab-pane>
    <el-tab-pane></el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import ModelConverter from "@/views/devtools/toolset/ModelConverter.vue";
import SqlConverter from "@/views/devtools/toolset/SqlConverter.vue";

const activeTabName = ref();

</script>

<style lang="scss" scoped>

</style>
