<!-- 
  下拉框选择器
  用于选择的值和要显示的文本不同的场景
 -->
<template>
  <el-select v-model="selectedValue" @change="handleValueChange">
    <el-option v-for="option in options" :key="option[key]" :label="option[label]" :value="option[value]"></el-option>
  </el-select>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const selectedValue = ref()

const handleValueChange = (val: any) => {
  const option = options.filter(option => option[value] == val)[0]
  if (option) {
    selectedValue.value = option[value]
  }
}

const { options, value } = withDefaults(defineProps<{
  /**
   * key字段名
   */
  key?: string,
  /**
   * label字段名
   */
  label?: string,
  /**
   * value字段名
   */
  value?: string,
  /**
   * 对象列表，而非基本类型列表
   */
  options: any[]
}>(), {
  key: "key",
  label: "label",
  value: "value"
})

</script>