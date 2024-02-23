<script setup lang="ts">
import { ref } from "vue";
import TableCreator from "@/views/devtools/database/TableCreator.vue";
import { apiGetTableCreatorColumns } from "@/api/devtools";

const visible = ref();
const tableCreatorRef = ref();

const show = () => {
  tableCreatorRef.value.init()
}

const loading = ref(false)

defineExpose({
  show(fieldGroupId: number) {
    visible.value = true;

    loading.value = true
    apiGetTableCreatorColumns(fieldGroupId).then((res) => {
      tableCreatorRef.value.setColumns(res.data)
      loading.value = false
    })
  }
});

</script>

<template>
  <vxe-modal v-bind:loading="loading" title="新建表" v-model="visible" height="80%" width="80%" show-footer transfer @show="show">
    <TableCreator ref="tableCreatorRef"></TableCreator>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>