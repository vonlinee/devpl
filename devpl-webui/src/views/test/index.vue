<script setup lang="ts">

import { reactive, ref } from "vue";
import VxeGridTable from "@/components/table/VxeGridTable.vue";


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

interface FormDataVO {
  name: string
  sex: string
}

const formData1 = reactive<FormDataVO>({
  name: '',
  sex: '0'
})

</script>

<template>
  <!-- <data-table :options="options">
    <template #saveOrUpdate>
      
    </template>
  </data-table> -->

  <vxe-grid-table>
    <template #add>
      <vxe-form :data="formData1">
        <vxe-form-item title="名称" field="name" :item-render="{}">
          <template #default="{ data }">
            <vxe-input v-model="data.name" placeholder="请输入名称" clearable></vxe-input>
          </template>
        </vxe-form-item>
        <vxe-form-item title="性别" field="sex" :item-render="{}">
          <template #default="{ data }">
            <vxe-select v-model="data.sex" placeholder="请选择性别" clearable>
              <vxe-option value="1" label="女"></vxe-option>
              <vxe-option value="2" label="男"></vxe-option>
            </vxe-select>
          </template>
        </vxe-form-item>
        <vxe-form-item>
          <template #default>
            <vxe-button type="submit" status="primary" content="默认尺寸"></vxe-button>
          </template>
        </vxe-form-item>
      </vxe-form>
    </template>
  </vxe-grid-table>
</template>

<style lang="scss"></style>
