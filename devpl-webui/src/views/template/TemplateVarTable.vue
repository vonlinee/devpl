<!--
  模板参数表
-->
<script setup lang="ts">
import { apiParseTemplateVariables } from "@/api/template";
import { ref } from "vue"

const visible = ref()

const tableData = ref<TemplateParam[]>([])

const templateInfoRef = ref<TemplateInfo>()

const parseTemplateVariables = () => {
  const template = templateInfoRef.value;
  if (template && template.id != undefined) {
    apiParseTemplateVariables(template.id).then((res) => {

    })
  }
}

defineExpose({
  show(templateInfo: TemplateInfo) {
    templateInfoRef.value = templateInfo
    visible.value = true
  },
})
</script>

<template>
  <vxe-modal :data="tableData" title="模板参数信息" v-model="visible" height="80%" width="70%">
    <vxe-button content="解析" @click="parseTemplateVariables"></vxe-button>
    <vxe-table ref="tableRef" border show-overflow :column-config="{ resizable: true }"
      :edit-config="{ trigger: 'click', mode: 'row' }">
      <vxe-column type="seq" width="60"></vxe-column>
      <vxe-column field="paramKey" title="参数Key" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.paramKey" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="paramName" title="参数名称" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.paramName" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column field="dataType" title="数据类型" :edit-render="{}">
        <template #default="{ row }">
          <span>{{ row.dataType }}</span>
        </template>
        <template #edit="{ row }">
          <vxe-select v-model="row.dataType" type="text" transfer></vxe-select>
        </template>
      </vxe-column>
      <vxe-column field="paramValue" title="参数值" :edit-render="{}">
        <template #edit="{ row }">
          <vxe-input v-model="row.paramValue" type="text"></vxe-input>
        </template>
      </vxe-column>
      <vxe-column fixed="right" title="操作" width="80" header-align="center" align="center">
        <template #default="scope">
          <el-button link type="primary"> 删除 </el-button>
        </template>
      </vxe-column>
    </vxe-table>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
