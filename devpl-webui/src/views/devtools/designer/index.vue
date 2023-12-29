<!-- 
  https://www.cnblogs.com/liuqin-always/p/14103450.html
  拖拽行的时候，同时拖拽该行及其嵌套的所有行
 -->
<template>
  <div>
    <converter></converter>
    <el-card>
      <el-button>导入</el-button>
      <Splitpanes>
        <Pane>
          <el-table
            border
            :data="selectableFields"
            :height="tableHeight"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="35"
              align="center"
            ></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column
              prop="fieldName"
              label="字段名称"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </Pane>
        <Pane>
          <el-table
            ref="fieldTable"
            border
            class="sortable-row-gen"
            row-key="id"
            :data="selectedFields"
            default-expand-all
            highlight-current-row
            :height="tableHeight"
            row-class-name="field-row"
          >
            <el-table-column
              type="selection"
              width="35"
              align="center"
            ></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column
              prop="fieldName"
              label="字段名称"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </Pane>
      </Splitpanes>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { Splitpanes, Pane } from "splitpanes"
import "splitpanes/dist/splitpanes.css"

import { apiListAllFields } from "@/api/fields"
import Converter from "@/views/devtools/toolset/SqlConverter.vue"

const selectableFields = ref<FieldInfo[]>([])
const selectedFields = ref<FieldInfo[]>([])

const sortable = ref()
const tableHeight = "600px"

const handleSelectionChange = (val: FieldInfo[]) => {
  selectedFields.value = val
}

onMounted(() => {
  apiListAllFields().then((res) => {
    selectableFields.value = res.data
  })
  if (!sortable.value) {
  }
})
</script>

<style lang="scss" scoped></style>
