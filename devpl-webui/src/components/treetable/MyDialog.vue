<template>
  <div class="dialog-container" v-show="visible">
    <label>菜单名称：</label><input type="text" placeholder="菜单名称" ref="name"><br>
    <label>链接：</label><input type="text" placeholder="链接" ref="uri"><br>
    <label>ID：</label><input type="text" placeholder="ID" ref="idRef"><br>
    <button @click="save">保存</button>
    <button @click="cancel">取消</button>
  </div>
</template>

<script setup lang="ts">

import { ref } from "vue";

const name = ref();
const uri = ref();
const idRef = ref();

const visible = ref();
const id = ref();

const { onSave } = defineProps<{
  onSave: Function,
  onCancel?: Function
}>();

const save = (list: any) => {
  let data = {
    name: name.value.value,
    uri: uri.value.value,
    id: idRef.value.value
  };
  onSave(id.value, data);
  visible.value = false;
};

const cancel = () => {
  visible.value = false;
};

defineExpose({
  show(type: any, data: any) {
    visible.value = true;
    if (type === "edit") {
      name.value = data.name;
      uri.value = data.uri;
      idRef.value = data.id;
      id.value = data.id;
    } else {
      id.value = data;
    }
  }
});

</script>
<style lang="scss">
.dialog-container {
  position: fixed;
  width: 300px;
  height: 300px;
  background: #FFF;
  top: 50%;;
  left: 50%;
  margin-left: -150px;
  margin-top: -150px;
  box-shadow: 0 0 5px #ccc;

  label {
    display: inline-block;
    width: 80px;
  }
}
</style>
