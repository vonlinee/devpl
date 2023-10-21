<script setup lang="ts">

import { ref } from "vue";
import PopupWindow from "@/components/dialog/PopupForm.vue";

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
  <popup-window ref="dialogRef" :form-data="formData" @submit="submit"></popup-window>

  <!-- <test-my-table></test-my-table> -->
</template>

<style lang="scss">
.demo-form-inline .el-input {
  --el-input-width: 220px;
}
</style>
