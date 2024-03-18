<template>
  <vxe-toolbar>
    <template #buttons>
      <vxe-button content="查询" status="primary" @click="queryList"></vxe-button>
      <vxe-button content="新增" status="primary" @click="onAddItem"></vxe-button>
      <!-- <vxe-button content="下拉按钮">
        <template #dropdowns>
          <vxe-button type="text" content="按钮1"></vxe-button>
          <vxe-button type="text" content="按钮2"></vxe-button>
          <vxe-button type="text" content="按钮3"></vxe-button>
        </template>
</vxe-button> -->
    </template>
  </vxe-toolbar>

  <vxe-table ref="tableRef" border show-overflow :data="tableData" :column-config="{ resizable: true }" :height="height"
    :edit-config="{ trigger: 'click', mode: 'row' }">
    <vxe-column type="seq" width="60"></vxe-column>
    <vxe-column field="paramKey" title="参数Key" :edit-render="{}" width="180">
      <template #edit="{ row }">
        <vxe-input v-model="row.paramKey" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column field="paramName" title="参数名称" :edit-render="{}" width="180">
      <template #edit="{ row }">
        <vxe-input v-model="row.paramName" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column field="dataType" title="数据类型" :edit-render="{}" width="160">
      <template #default="{ row }">
        <span>{{ row.dataType }}</span>
      </template>
      <template #edit="{ row }">
        <vxe-select v-model="row.dataType" type="text" :options="dataTypeOptions" transfer></vxe-select>
      </template>
    </vxe-column>
    <vxe-column field="paramValue" title="参数值" :edit-render="{}">
      <template #edit="{ row }">
        <vxe-input v-model="row.paramValue" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column fixed="right" title="操作" width="80" header-align="center" align="center">
      <template #default="scope">
        <el-button link type="primary" @click.prevent="removeRow(scope.row)">
          删除
        </el-button>
      </template>
    </vxe-column>
  </vxe-table>
</template>

<script lang="ts" setup>
import { apiListTemplateParams, apiListTemplateParamValueDataTypeOptions } from "@/api/template";
import { ref, onMounted } from "vue"

const { height } = withDefaults(
  defineProps<{
    height?: number
  }>(),
  {
    height: 500,
  }
)
const tableRef = ref()
const dataTypeOptions = ref([])

const tableData = ref<TemplateParam[]>([])


const removeRow = async (row: TemplateParam) => {
  const $table = tableRef.value
  if ($table) {
    await $table.remove(row)
  }
}

const queryList = () => {
  apiListTemplateParams().then((res) => {
    tableData.value = res.data
  })
}

const onAddItem = () => {
  tableData.value.push({
    paramName: "",
    paramValue: "",
    defaultValue: "",
    dataTypeId: "",
  })
}

defineExpose({
  /**
   * 获取表的数据
   */
  getParams() {
    return tableData.value
  }
})

onMounted(() => {
  queryList()

  apiListTemplateParamValueDataTypeOptions().then((res) => {
    dataTypeOptions.value = res.data
  })
})
</script>
