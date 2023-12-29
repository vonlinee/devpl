<!--
  语言选择
-->
<template>
  <el-select v-model="value" class="m-2" placeholder="Select">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<script lang="ts" setup>
import { ref } from "vue";

const options = [
  {
    value: "java",
    label: "Java"
  }
];
const value = ref(options[0].value);
</script>

<style scoped>
select {
  display: inline;
  border: none;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.5);
}
</style>
