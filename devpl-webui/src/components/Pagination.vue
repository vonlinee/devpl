<!--
  分页组件
-->
<template>
  <el-pagination background layout="prev, pager, next, sizes" :total="total"
                 :current-page="currentPage"
                 :page-size="currentPageSize"
                 @change="handleChange"
  />
</template>

<script lang="ts" setup>

import { ref } from "vue";

const currentPage = ref();
const currentPageSize = ref();

const emits = defineEmits([
  "change"
]);

const handleChange = (currentPage: number, pageSize: number) => {
  emits("change", currentPage, pageSize);
};

const { total } = withDefaults(defineProps<{
  total?: number
}>(), {
  total: 0
});

</script>

<style lang="scss" scoped>

</style>