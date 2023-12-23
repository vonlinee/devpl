<template>
  <FieldTreeTable></FieldTreeTable>
</template>

<script setup lang="ts">

import FieldTreeTable from "@/components/fields/FieldTreeTable.vue";
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

