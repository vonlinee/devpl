<!--
 * @ 字段管理列表
 * @author Von
 * @date 2023/8/29 14:23
-->
<script setup lang="ts">
import { apiListFields } from "@/api/fields";
import { ref, reactive, onMounted } from "vue";

import {
  VXETable,
  VxeTableInstance,
  VxeColumnPropTypes,
  VxeFormPropTypes,
  VxeFormItemPropTypes,
  VxeTableEvents
} from "vxe-table";

/**
 * 表格数据模型
 */
interface RowVO {
  fieldId: string,
  fieldName: string,
  dataType: string,
  description: string,
  defaultValue: string
}

const xTable = ref<VxeTableInstance>();

const tableData = ref<RowVO[]>([]);

onMounted(() => {
  apiListFields().then(res => {
    tableData.value = res.data;
  })
})

let datas = ref([
  {
    fieldName: "zsdsd"
  }
])

</script>

<template>
  <el-card>
    <div>
      <vxe-table :border="true" show-overflow ref="xTable" height="300" :column-config="{ resizable: true }"
        :row-config="{ isHover: true }" :data="datas">
        <vxe-column type="seq" width="60"></vxe-column>
        <vxe-column field="fieldId" title="ID"></vxe-column>
        <vxe-column field="fieldName" title="名称"></vxe-column>
        <vxe-column field="dataType" title="数据类型"></vxe-column>
        <vxe-column field="defaultValue" title="默认值" show-overflow></vxe-column>
        <vxe-column field="description" title="描述信息" show-overflow></vxe-column>
        <vxe-column title="操作" width="100" show-overflow>
          <template #default="{ row }">
            <vxe-button type="text" icon="vxe-icon-edit"></vxe-button>
            <vxe-button type="text" icon="vxe-icon-delete"></vxe-button>
          </template>
        </vxe-column>
      </vxe-table>
    </div>
  </el-card>
</template>

<style scoped lang="scss"></style>
