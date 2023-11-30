<template>
  <vxe-modal title="字段选择" v-model="visible" @close="handleClose" destroy-on-close :mask="false" width="400px">
    <field-select-table ref="fieldTable" :existed="existedFields"
      @selection-change="handleSelection"></field-select-table>
  </vxe-modal>
</template>

<script lang="ts" setup>

import { ref } from "vue";
import FieldSelectTable from "@/views/fields/FieldSelectTable.vue";

const visible = ref();
const fieldTable = ref();
const existedFields = ref<FieldInfo[]>();

const emits = defineEmits([
  "selection-change"
]);

const handleSelection = (val: FieldInfo[]) => {
  emits("selection-change", val);
};

const handleClose = () => {

};

defineExpose({
  show: (existed?: FieldInfo[]) => {
    visible.value = true;
    existedFields.value = existed || [];
  }
});

</script>

<style scoped></style>