<!-- 
  类型映射配置表
 -->
<template>
  <vxe-modal v-model="modalShowRef" title="类型映射表" width="75%" :draggable="false">
    <vxe-form :data="formData">
      <vxe-form-item title="规则分组" field="groupId">
        <template #default="{ data }">
          <el-select v-model="data.groupId">
            <el-option v-for="g in mappingGroups" :label="g.label" :value="g.value" :key="g.key"></el-option>
          </el-select>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型组" field="typeGroupId" :item-render="{}">
        <template #default="{ data }">
          <el-select v-model="data.typeGroupId" @change="handleTypeGroupChange" clearable>
            <el-option v-for="g in typeGroupOptions" :label="g.label" :value="g.value" :key="g.key"></el-option>
          </el-select>
        </template>
      </vxe-form-item>
      <vxe-form-item title="映射类型组" field="anotherTypeGroupId" :item-render="{}">
        <template #default="{ data }">
          <el-select v-model="data.anotherTypeGroupId" @change="queryAllDataTypeMappings" clearable>
            <el-option v-for="g in typeGroupOptions" :label="g.label" :value="g.value" :key="g.key"
              :disabled="g.label == formData.typeGroupId"></el-option>
          </el-select>
        </template>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="查询" @click="queryAllDataTypeMappings"></vxe-button>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="新增" @click="showModal"></vxe-button>
      </vxe-form-item>
    </vxe-form>

    <div>
      <splitpanes class="default-theme" vertical>
        <pane>
          <vxe-table :row-config="{ isCurrent: true, isHover: true }" border height="500"
            @current-change="handlePrimaryTypeChanged" :data="dataTypeMappingData.types">
            <vxe-column field="typeKey" title="主类型" align="center"></vxe-column>
          </vxe-table>
        </pane>
        <pane>
          <vxe-table :row-config="{ isCurrent: true, isHover: true }" border height="500" :data="mappedTypes"
            :menu-config="menuConfig" @menu-click="handleMenuClicked">
            <vxe-column field="typeKey" title="映射类型" align="center"></vxe-column>
          </vxe-table>
        </pane>
      </splitpanes>
    </div>

  </vxe-modal>
  <TypeMappingSaveOrUpdate ref="typeMappingSaveUpdateModalRef"></TypeMappingSaveOrUpdate>
</template>

<script setup lang="ts">
import {
  apiListTypeGroupOptions,
  apiListTypeMappingGroupOptions,
  apiRemoveDataMappingById,
  apiListDataTypeMappingByType,
  apiRemoveDataMappingByType,
} from "@/api/datatype"
import { Message } from "@/hooks/message"
import { reactive, ref, toRaw } from "vue"
import TypeMappingSaveOrUpdate from "./TypeMappingSaveOrUpdate.vue";

import { apiListDataTypeMappingByGroup } from '@/api/datatype';
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

import { VxeTableEvents, VxeTablePropTypes } from 'vxe-table';
import { ResponseStatus } from "@/utils/http/status";

const mappedTypes = ref<DataTypeItem[]>([])

const dataTypeMappingData = ref<DataTypeMappingByTypeGroup>({
  groupId: null,
  groupName: '',
  typeGroupId: undefined,
  anotherTypeGroupId: undefined,
  types: [],
  mappedDataTypes: []
})

const refreshData = (param: any) => {
  apiListDataTypeMappingByGroup(param).then((res) => {
    dataTypeMappingData.value = res.data
    mappedTypes.value = []
  })
}

const typeMappingSaveUpdateModalRef = ref()
// 主类型分组选项
const typeGroupOptions = ref<SelectOptionVO[]>([])
const modalShowRef = ref()

interface FormDataVO {
  groupId?: number
  typeGroupId?: string
  anotherTypeGroupId?: string
  typeId?: number
  typeKeyPattern?: string
}
const formData = reactive<FormDataVO>({
  groupId: 1, // 1 表示系统默认
  typeGroupId: undefined,
  anotherTypeGroupId: undefined,
  typeId: undefined,
  typeKeyPattern: "",
})

// 编辑时的可选择的下拉列表

const mappingGroups = ref<SelectOptionVO[]>([])

const queryAllDataTypeMappings = () => {
  refreshData(toRaw(formData))
}

/**
 * 删除数据类型映射关系
 * @param mapping
 */
const removeDataTypeMapping = (mapping: DataTypeMapping) => {
  if (mapping.id != undefined) {
    apiRemoveDataMappingById(mapping.id).then(() => {
      Message.info("删除成功")
      queryAllDataTypeMappings()
    })
  }
}

/**
 * 主类型更改
 * @param typeGroup 
 */
const handleTypeGroupChange = (typeGroup: string) => {
  // 主类型和映射类型相同，则重置映射类型
  if (formData.anotherTypeGroupId != null && typeGroup == formData.anotherTypeGroupId) {
    formData.anotherTypeGroupId = undefined
  }
}

/**
 * 打开数据类型映射编辑弹窗
 */
const showModal = () => {
  if (formData.groupId == undefined) {
    Message.error("先选择映射规则分组")
    return
  }
  typeMappingSaveUpdateModalRef.value.show(toRaw(formData))
}

defineExpose({
  show: () => {
    modalShowRef.value = true
    apiListTypeMappingGroupOptions().then((res) => {
      mappingGroups.value = res.data
    })

    apiListTypeGroupOptions().then((res) => {
      typeGroupOptions.value = res.data
    })
  },
})

const currentPrimaryType = ref<DataTypeItem | null>(null)

const handlePrimaryTypeChanged: VxeTableEvents.CurrentChange<DataTypeItem> = ({ newValue, oldValue, row, rowIndex }) => {
  currentPrimaryType.value = newValue
  if (newValue) {
    refreshMappedDataTypes(newValue.id)
  }
}

const refreshMappedDataTypes = (typeId: number) => {
  apiListDataTypeMappingByType({
    groupId: formData.groupId,
    typeId: typeId,
    anotherTypeGroupId: formData.anotherTypeGroupId
  }).then((res) => {
    mappedTypes.value = res.data
  })
}

const menuConfig = reactive<VxeTablePropTypes.MenuConfig>({
  body: {
    options: [
      [
        { code: 'del', name: '删除' },
      ]
    ]
  }
})

const handleMenuClicked: VxeTableEvents.MenuClick<DataTypeItem> = ({ menu, type, row, rowIndex, column, columnIndex, $event }) => {
  if (menu.code == 'del') {
    if (currentPrimaryType.value) {
      const typeId = currentPrimaryType.value?.id
      apiRemoveDataMappingByType({
        groupId: formData.groupId,
        typeId: typeId,
        anotherTypeIds: [row.typeId]
      }).then((res) => {
        if (res.code == ResponseStatus.SUCCESS) {
          refreshMappedDataTypes(typeId)
        }
      })
    }
  }
}
</script>
