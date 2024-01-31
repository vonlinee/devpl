<template>
  <el-table border :data="tableData" :height="height + 'px'">
    <el-table-column fixed prop="paramKey" label="参数Key" width="150" />
    <el-table-column prop="name" label="参数名称" width="120" />
    <el-table-column prop="state" label="默认值" width="120" />
    <el-table-column prop="city" label="数据类型" width="120" />
    <el-table-column fixed="right" label="操作" width="120">
      <template #default="scope">
        <el-button link type="primary" @click.prevent="deleteRow(scope.$index)">
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-button class="mt-4" style="width: 100%" @click="onAddItem"
    >增加
  </el-button>
</template>

<script lang="ts" setup>
import { ref } from "vue"

const { height } = withDefaults(
  defineProps<{
    height?: number
  }>(),
  {
    height: 500,
  }
)

const tableData = ref<TemplateParam[]>([])

const deleteRow = (index: number) => {
  tableData.value.splice(index, 1)
}

const onAddItem = () => {
  tableData.value.push({
    paramName: "",
    paramValue: "",
    defaultValue: "",
    dataTypeId: "",
  })
}
</script>
