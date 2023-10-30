<script setup lang="ts">

import { ref } from "vue";
import DbTable from "@/components/db/DbTable.vue";

let modalVisiable = ref();

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

const editorRef = ref()
const inputValue = ref()
const func1 = () => {
  console.log(editorRef.value.getText());
}

const func2 = () => {
  console.log(inputValue.value.value);
  editorRef.value.setText(inputValue.value.value)
}

</script>

<template>
  <db-table></db-table>

  <!--  <button @click="show">Open</button>-->
  <!--  <button @click="func1">getText</button>-->
  <!--  <input ref="inputValue">-->
  <!--  <button @click="func2">setText</button>-->
  <!--  <save-or-update ref="dialogRef" :form-data="formData" @submit="submit">-->
  <!--    <template #default="scope">-->
  <!--      <span>{{ scope}}</span>-->
  <!--    </template>-->
  <!--  </save-or-update>-->

  <!--  <suspense>-->
  <!--    <code-mirror ref="editorRef" lang="java"></code-mirror>-->
  <!--  </suspense>-->

  <!-- <test-my-table></test-my-table> -->
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
