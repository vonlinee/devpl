<!--
  不同于FieldTreeTable，FieldTable不展示树形结构
-->
<template>
  <el-table :data="tableData" border height="100%" @cell-click="handleCellCilcked">
    <el-table-column label="名称" prop="fieldKey" show-overflow-tooltip></el-table-column>
    <el-table-column label="数据类型" prop="dataType" show-overflow-tooltip>
      <template #default="scope">
        <el-input v-if="scope.row.editing"></el-input>
        <span v-if="scope.row.editing || true">{{ scope.row.dataType }}</span>
      </template>
    </el-table-column>
    <el-table-column v-if="showDefaultValueColumn" label="默认值" prop="defaultValue"
      show-overflow-tooltip></el-table-column>
    <el-table-column label="描述信息" prop="comment" show-overflow-tooltip></el-table-column>
    <el-table-column label="操作" fixed="right" align="center">
      <template #default="scope">
        <el-button link @click="removeRow(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { ref } from "vue"

const props = withDefaults(
  defineProps<{
    /**
     * 是否展示默认值列
     */
    showDefaultValueColumn?: boolean
  }>(),
  {
    showDefaultValueColumn: false,
  }
)

/**
 * 点击编辑
 * @param row 
 * @param column 
 * @param cell 
 * @param event 
 */
const handleCellCilcked = (row: any, column: any, cell: any, event: any) => {

}

const tableData = ref<FieldInfo[]>()

/**
 * 删除行
 * @param row
 */
const removeRow = (row: FieldInfo) => {
  tableData.value = tableData.value?.filter((f) => f.fieldKey != row.fieldKey)
}

defineExpose({
  getFields() {
    return tableData.value
  },
  setFields(fields?: FieldInfo[]) {
    tableData.value = fields || []
  },
})
</script>

<style lang="scss" scoped></style>
