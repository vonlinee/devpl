<!--
  语言选择下拉列表：包含语言的logo
-->
<template>
  <el-select
    v-model="value"
    class="m-2"
    placeholder="选择语言"
    @change="handleChange"
  >
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<script lang="ts" setup>
import { ref } from "vue"

const options = [
  {
    value: "java",
    label: "Java",
  },
  {
    value: "ts",
    label: "TypeScript",
  },
]
const value = ref(options[0].value)

const emits = defineEmits(["selection-change"])

const handleChange = (val: any) => {
  emits("selection-change", val)
}
</script>

<style scoped>
select {
  display: inline;
  border: none;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.5);
}
</style>
