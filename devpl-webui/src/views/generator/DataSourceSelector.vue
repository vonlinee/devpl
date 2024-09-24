<!--
 * @ 数据源选择
 * @author Von
 * @date 2023/7/24 16:57
-->
<script setup lang="ts">
import { onMounted, ref } from "vue"
import { apiListSelectableDataSources } from "@/api/datasource"

const selected = ref()

const dataSourceOptions = ref<DataSourceVO[]>()

const emits = defineEmits(["selection-change"])

const onSelectionChange = (val: DataSourceVO) => {
  emits("selection-change", val)
}

onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSourceOptions.value = res.data
    if (res.data && res.data.length > 0) {
      selected.value = res.data[0].id
    }
  })
})
</script>

<template>
  <el-select v-model="selected" @change="onSelectionChange">
    <el-option
      v-for="dataSource in dataSourceOptions"
      :key="dataSource.id"
      :value="dataSource.id"
      :label="dataSource.name"
    ></el-option>
  </el-select>
</template>

<style scoped lang="scss"></style>
