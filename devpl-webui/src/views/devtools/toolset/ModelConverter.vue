<!--
  输入为Schema类，输出可以是SQL或者其他Schema
-->
<template>
  <div>
    <el-form>
      <el-form-item label="类名">

      </el-form-item>
    </el-form>
    <div>
      <monaco-editor language="java" width="600px" height="600px" minimap />
      <monaco-editor language="java" width="600px" height="600px" minimap />
    </div>
  </div>
</template>

<script setup lang="ts">

import MonacoEditor from "@/components/editor/MonacoEditor.vue";


</script>

<style scoped>

</style>