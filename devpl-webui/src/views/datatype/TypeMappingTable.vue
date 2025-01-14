<!-- 
  类型映射配置表
 -->
<template>
  <vxe-modal v-model="modalShowRef" title="类型映射表" width="70%" :draggable="false">
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
          <el-select v-model="data.typeGroupId" @change="queryAllDataTypeMappings" style="z-index: 9999" clearable>
            <el-option v-for="g in typeGroupOptions" :label="g.label" :value="g.value" :key="g.key"></el-option>
          </el-select>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型Key" field="typeKeyPattern" :item-render="{}">
        <template #default="{ data }">
          <vxe-input v-model="data.typeKeyPattern" placeholder="请输入类型Key搜索" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="查询" @click="queryAllDataTypeMappings"></vxe-button>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="新增" @click="showModal"></vxe-button>
      </vxe-form-item>
    </vxe-form>

    <!-- 表格 -->
    <vxe-table ref="table" :height="528" border :data="tableData">
      <vxe-column title="主类型分组" width="20%" align="center" field="typeGroupId">
      </vxe-column>
      <vxe-column title="主类型" width="20%" align="center" field="typeKey">
      </vxe-column>
      <vxe-column title="映射类型分组" field="anotherTypeGroupId" align="center">
      </vxe-column>
      <vxe-column title="映射类型" field="anotherTypeKey" align="center">
      </vxe-column>
      <vxe-column title="操作" fixed="right" width="100" align="center">
        <template #default="{ row }">
          <vxe-button type="text" status="danger" content="删除" @click="removeDataTypeMapping(row)"></vxe-button>
        </template>
      </vxe-column>
    </vxe-table>

    <vxe-pager v-model:current-page="pageVo.currentPage" v-model:page-size="pageVo.pageSize" :total="pageVo.total" />
  </vxe-modal>

  <vxe-modal v-model="modal1ShowRef" title="选择映射的数据类型" :mask="true" :show-footer="true">
    <vxe-table ref="tableRef" :height="400" :border="true" :data="mappableDataTypes">
      <vxe-column type="checkbox" width="40" align="center"></vxe-column>
      <vxe-column field="typeGroupId" title="数据类型组" align="center"></vxe-column>
      <vxe-column field="localeTypeName" title="数据类型" align="center"></vxe-column>
    </vxe-table>
    <template #footer>
      <vxe-button type="text" status="success">确定</vxe-button>
    </template>
  </vxe-modal>

  <DataTypeSelectModal ref="dataTypeSelectRef" @selected="handleSelected" @close="queryAllDataTypeMappings"
    DataTypeSelectModal />
</template>

<script setup lang="ts">
import {
  apiListTypeGroupOptions,
  apiListAllDataTypeMappings,
  apiListAllMappableDataTypes,
  apiListTypeMappingGroupOptions,
  apiRemoveDataMappingById,
} from "@/api/datatype"
import { Message } from "@/hooks/message"
import { reactive, ref, toRaw } from "vue"
import DataTypeSelectModal from "./DataTypeSelectModal.vue"

const table = ref()
const typeGroupOptions = ref()
const modalShowRef = ref()
const modal1ShowRef = ref()
const dataTypeSelectRef = ref()

const pageVo = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0,
})

const handleSelected = (items: DataTypeItem[]) => {
  if (items.length == 1) {
    const dt = items[0]
    tableData.value.push({
      typeId: dt.typeId,
      typeName: dt.localeTypeName,
      typeKey: dt.typeKey,
      anotherTypeId: undefined,
      groupId: 0,
      groupName: "",
      anotherTypeKey: undefined,
    })
  }
}

interface FormDataVO {
  groupId?: number
  typeGroupId?: string
  typeId?: number
  typeKeyPattern?: string
}
const formData = reactive<FormDataVO>({
  groupId: 1, // 1 表示系统默认
  typeGroupId: undefined,
  typeId: undefined,
  typeKeyPattern: "",
})

/**
 * 数据类型选择项
 */
interface DataTypeSelectVO {
  mapped: boolean
  typeId: number
}

// 编辑时的可选择的下拉列表
const mappableDataTypes = ref<DataTypeSelectVO[]>([])

const mappingGroups = ref<SelectOptionVO[]>([])
const tableData = ref<DataTypeMapping[]>([])

const queryAllDataTypeMappings = () => {
  apiListAllDataTypeMappings(toRaw(formData)).then((res) => {
    tableData.value = res.data
    pageVo.value.total = res.total || 0
  })
}

/**
 * 删除数据类型映射关系
 * @param mapping
 */
const removeDataTypeMapping = (mapping: DataTypeMapping) => {
  if (mapping.id != undefined) {
    apiRemoveDataMappingById(mapping.id).then((res) => {
      Message.info("删除成功")
      queryAllDataTypeMappings()
    })
  }
}

const tableRef = ref()

/**
 * 打开数据类型映射编辑弹窗
 */
const showModal = () => {
  if (formData.groupId == undefined) {
    Message.error("先选择映射规则分组")
    return
  }
  dataTypeSelectRef.value.show(formData.groupId)
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

    queryAllDataTypeMappings()
  },
})
</script>
