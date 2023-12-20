<template>
  <FieldTree selectable></FieldTree>

</template>

<script setup lang="ts">

import FieldTree from "@/components/fields/FieldTree.vue";

</script>

<style scoped lang="scss">
.page {
  height: 600px;
  padding: 10px;

  .pane {
    border: 1px solid black;
  }

  .container {
    height: 100%;
    border: 1px solid black;
  }
}

</style>

