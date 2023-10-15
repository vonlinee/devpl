<template>
  <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" background :page-sizes="[10, 20, 30, 40]"
    layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
    @current-change="handleCurrentChange">
  </el-pagination>
</template>
<script lang='ts'>
import { defineComponent, ref } from 'vue'

export default defineComponent({
  setup(props, context) {
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(100)

    const handleSizeChange = (val: number) => {
      console.log(`${val} items per page`)
    }
    const handleCurrentChange = (val: number) => {
      console.log(`current page: ${val}`)
    }

    return {
      currentPage,
      pageSize,
      total,
      handleSizeChange,
      handleCurrentChange
    }
  }
})
</script>
<style lang='' scoped>

</style>