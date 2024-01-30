<script setup lang="ts">
import { nextTick, ref } from "vue";
import TableCreator from "@/views/devtools/database/TableCreator.vue";

const visible = ref();
const tableCreatorRef = ref();

defineExpose({
  show(fields: FieldInfo[]) {
    visible.value = true;

    nextTick(() => tableCreatorRef.value.addColumns(fields));
  }
});

</script>

<template>
  <vxe-modal title="新建表" v-model="visible" height="80%" width="80%" show-footer transfer>
    <TableCreator ref="tableCreatorRef"></TableCreator>
  </vxe-modal>
</template>

<style scoped lang="scss">

</style>