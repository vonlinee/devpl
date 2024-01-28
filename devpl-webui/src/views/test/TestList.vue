<script setup lang="ts">

import List from "@/components/List.vue";
import { ref } from "vue";

/**
 * 可选择的数据类型
 */
const dataTypeOptions = ref<DataTypeSelectOption[]>([
  {
    label: "String",
    value: "String",
    key: "string"
  },
  {
    label: "Integer",
    value: "Integer",
    key: "Integer"
  },
  {
    label: "Boolean",
    value: "Boolean",
    key: "Boolean"
  },
  {
    label: "Array",
    value: "Array",
    key: "Array"
  },
  {
    label: "Object",
    value: "Object",
    key: "Object"
  },
  {
    label: "Number",
    value: "Number",
    key: "Number"
  },
  {
    label: "null",
    value: "null",
    key: "null"
  },
  {
    label: "any",
    value: "any",
    key: "any"
  }
]);
</script>

<template>
  <List :items="dataTypeOptions">
    <template #item="scope">
      {{ scope.item.label }}
    </template>
  </List>
</template>

<style scoped lang="scss">

</style>