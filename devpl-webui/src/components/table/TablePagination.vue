<!-- 
  专用于表格的分页
 -->
<template>
  <el-pagination :current-page="option.page" :page-sizes="option.pageSizes" :page-size="option.limit"
    :total="option.total" layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle">
  </el-pagination>
</template>
<script lang='ts' setup>

import { DataTableOption } from '@/hooks/interface'

const { option, sizeChangeHandle, currentChangeHandle } = defineProps<{
  option: DataTableOption,
  sizeChangeHandle: (val: number) => void,
  currentChangeHandle: (val: number) => void
}>()

</script>
<style lang="scss" scoped></style>