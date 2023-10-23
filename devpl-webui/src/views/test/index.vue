<script setup lang="ts">

import { ref } from "vue";
import SaveOrUpdate from "@/components/dialog/SaveOrUpdate.vue";
import CodeMirror from "@/components/editor/CodeMirror.vue";

let modalVisiable = ref();

const click = () => {
  modalVisiable.value = true;
};

interface FormDataVO {
  name: string;
  sex: string;
}

let tableData = [];

for (let i = 0; i < 100; i++) {
  tableData.push({
    name: "name" + i,
    nickname: "nickname" + i
  });
}

const formData4 = {
  name: "",
  nickname: "1"
};

let dialogRef = ref();

const show = () => {
  dialogRef.value.show();
};

let formData = {
  name: "Zs",
  age: 12
};

const submit = (formData: any, setVisiable: (val: boolean) => void) => {
  console.log(formData);
  setVisiable(false)
};

</script>

<template>
  <button @click="show">Open</button>
  <save-or-update ref="dialogRef" :form-data="formData" @submit="submit">
    <template #default="scope">
      <span>{{ scope}}</span>
    </template>
  </save-or-update>

  <code-mirror></code-mirror>

  <!-- <test-my-table></test-my-table> -->
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
