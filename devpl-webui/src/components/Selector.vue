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

const handleValueChange = (val: any) => {
  const option = options.filter(option => option[value] == val)[0]
  if (option) {
    
  }
}

const selectedValue = ref()

const { options, value } = defineProps<{
  key: string,
  label: string,
  value: string,
  options: any[]
}>()

</script>