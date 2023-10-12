<!-- 
    样式固定的表格，使用 vxe-grid 实现
 -->
<template>
  <vxe-grid v-bind="gridOptions">
    <template #name_edit="{ row }">
      <vxe-input v-model="row.name"></vxe-input>
    </template>
    <template #nickname_edit="{ row }">
      <vxe-input v-model="row.nickname"></vxe-input>
    </template>
    <template #role_edit="{ row }">
      <vxe-input v-model="row.role"></vxe-input>
    </template>
    <template #address_edit="{ row }">
      <vxe-input v-model="row.address"></vxe-input>
    </template>
  </vxe-grid>

  <vxe-modal v-model="modalVisiableRef" draggable>
    <!-- 新增或修改弹窗 -->
    <slot name="modal"></slot>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { useVxeGridTable, VDTOptions } from "./vxedatatable";

/**
 * 组件属性
 */
export interface VxeDataTableProps {
  /**
   * 表格配置项
   */
  options: VDTOptions,
}

const modalVisiableRef = ref(false);

// 组件传入的属性
let { options } = defineProps<VxeDataTableProps>();

let gridOptions = useVxeGridTable(options);

</script>
  