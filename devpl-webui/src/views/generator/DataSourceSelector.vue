<!--
 * @ 数据源选择
 * @author Von
 * @date 2023/7/24 16:57
-->
<script setup lang="ts">

import { onMounted, ref } from "vue";
import { apiListSelectableDataSources } from "@/api/datasource";

const dataSourceOptions = ref<DataSourceVO[]>();

const emits = defineEmits([
  "selection-change"
]);

const onSelectionChange = (val: DataSourceVO) => {
  emits("selection-change", val);
};

onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSourceOptions.value = res.data
  });
});

</script>

<template>
  <el-select @change="onSelectionChange">
    <el-option v-for="dataSource in dataSourceOptions" :value="dataSource" :label="dataSource.name"
               :key="dataSource.id"></el-option>
  </el-select>
</template>

<style scoped lang="scss">

</style>
