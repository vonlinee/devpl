<script setup lang="ts">

import SplitPane from "@/components/splitpane/SplitPane.vue";

</script>

<template>
  <split-pane>
    <div style="background-color: #1e97f8; width: 100%">1</div>
    <div style="background-color: #37d25d;  width: 100%">2</div>
  </split-pane>
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
