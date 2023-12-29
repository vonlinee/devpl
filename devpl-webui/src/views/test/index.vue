<template>
  <FieldTreeTable></FieldTreeTable>

  <FieldTree selectable></FieldTree>
  <Pagination></Pagination>
</template>

<script setup lang="ts">
import FieldTreeTable from "@/components/fields/FieldTreeTable.vue"

import FieldTree from "@/components/fields/FieldTree.vue"
import Pagination from "@/components/Pagination.vue"
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
