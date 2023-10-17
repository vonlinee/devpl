<script setup lang="ts">

import { reactive, ref } from "vue";
import VxeGridTable, { VxeGridOptions } from "@/components/table/VxeGridTable.vue";
import { apiListDataTypes } from "@/api/datatype";


let modalVisiable = ref();

const click = () => {
  modalVisiable.value = true;
};

let options = {
  columns: [
    { type: "checkbox", width: 50 },
    { type: "seq", width: 60 },
    { field: "name", title: "名称", editRender: { autofocus: ".vxe-input--inner" }, slots: { edit: "name_edit" } },
    {
      title: "分类"
    },
    { field: "address", title: "地址", showOverflow: true }
  ],
  data: [{}, {}]
};

interface FormDataVO {
  name: string;
  sex: string;
}

const formData1 = reactive<FormDataVO>({
  name: "",
  sex: "0"
});

const gridOptions = reactive({
  columns: [
    { type: "checkbox", width: 50 },
    { type: "seq", width: 60 },
    { field: "name", title: "Name", editRender: { autofocus: ".vxe-input--inner" } },
    { field: "nickname", title: "Nickname", editRender: {} },
    { field: "role", title: "Role", editRender: {} },
    { field: "address", title: "Address", showOverflow: true, editRender: {} },
    { title: "操作", showOverflow: true, editRender: {} }
  ],
  save: apiListDataTypes
} as VxeGridOptions);

const formInline = reactive({
  user: '',
  region: '',
  date: '',
})

const onSubmit = () => {
  console.log('submit!')
}

</script>

<template>
  <vxe-grid-table :options="gridOptions">
    <template #form>
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="Approved by">
          <el-input v-model="formInline.user" placeholder="Approved by" clearable />
        </el-form-item>
        <el-form-item label="Activity zone">
          <el-select v-model="formInline.region" placeholder="Activity zone" clearable>
            <el-option label="Zone one" value="shanghai" />
            <el-option label="Zone two" value="beijing" />
          </el-select>
        </el-form-item>
        <el-form-item label="Activity time">
          <el-date-picker v-model="formInline.date" type="date" placeholder="Pick a date" clearable />
        </el-form-item>
      </el-form>
    </template>
  </vxe-grid-table>
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
