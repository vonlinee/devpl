<script setup lang="ts">

import { reactive, ref } from "vue";
import { DataTableOptions } from "@/components/table/DataTable.vue";
import TestMyTable from "@/components/table/TestMyTable.vue";
let modalVisiable = ref();

const click = () => {
  modalVisiable.value = true;
};

interface FormDataVO {
  name: string;
  sex: string;
}

let tableData = []

for (let i = 0; i < 100; i++) {
  tableData.push({
    name: "name" + i,
    nickname: "nickname" + i
  })
}

const formData4 = {
  name: '',
  nickname: '1'
}

const gridOptions: DataTableOptions = {
  columns: [
    { type: "checkbox", width: 50 },
    { type: "seq", width: 60 },
    { field: "name", title: "Name", editRender: { autofocus: ".vxe-input--inner" } },
    { field: "nickname", title: "Nickname", editRender: {} },
    { field: "role", title: "Role", editRender: {} },
    { field: "address", title: "Address", showOverflow: true, editRender: {} }
  ],
  tableData: tableData,
  formData: formData4,
  queryPage: function(currentPage: number, pageSize: number): Promise<any> {
    throw new Error("Function not implemented.");
  }
};



</script>

<template>

  <test-my-table></test-my-table>
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
