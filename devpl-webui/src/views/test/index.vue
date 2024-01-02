<template>
  <param-table :rows="rows" :data-types="dataTypes"></param-table>
</template>
<script lang="ts" setup>
import ParamTable from '@/components/ParamTable.vue';
import { ref, onMounted } from 'vue'

const rows = ref<ParamItem[]>([
  {
    id: 1,
    name: "name",
    leaf: false,
  }
])
const dataTypes = ref([])
onMounted(() => {

})

</script>
<style lang = "scss" >
.el-tabs__content {
  height: calc(100% - 55px);
  overflow-y: auto;
}
</style>
