<script setup lang="ts">

import DataTable, { DataTableOptions } from "@/components/table/DataTable.vue";
import { reactive, ref } from "vue";


let modalVisiable = ref()

const click = () => {
  modalVisiable.value = true
}

let options = {
  columns: [
    { type: 'checkbox', width: 50 },
    { type: 'seq', width: 60 },
    { field: 'name', title: '名称', editRender: { autofocus: '.vxe-input--inner' }, slots: { edit: 'name_edit' } },
    {
      title: '分类',
    },
    { field: 'address', title: '地址', showOverflow: true }
  ],
  data: [{

  }, {}]
}

</script>

<template>
  <data-table :options="options">
    <template #saveOrUpdate>
      
    </template>
  </data-table>

</template>

<style lang="scss"></style>
