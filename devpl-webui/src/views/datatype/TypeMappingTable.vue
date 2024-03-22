<!-- 
  类型映射配置表
 -->
<template>
  <vxe-modal v-model="modalShowRef" title="类型映射表" width="70%">
    <vxe-form :data="formData">
      <vxe-form-item title="规则分组" field="groupId">
        <template #default="{ data }">
          <el-select v-model="data.groupId">
            <el-option v-for="g in mappingGroups" :label="g.label" :value="g.value" :key="g.key"></el-option>
          </el-select>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型组" field="name" :item-render="{}" span="10">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入类型组名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型ID" field="name" :item-render="{}" span="10">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入类型ID" clearable></vxe-input>
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
    <vxe-table ref="table" :height="528" border :data="tableData" @cell-dblclick="cellDbClickHandler">
      <vxe-column title="主类型" width="20%" align="center" field="typeName">
        <template #default="{ row }">
          <el-select v-if="row.editing" v-model="row.typeId">
            <el-option v-for="g in mappingGroups" :label="g.label" :value="g.value" :key="g.key"></el-option>
          </el-select>
        </template>
      </vxe-column>
      <vxe-column field="name" title="映射类型" align="center" :edit-render="{}">
        <template #default="{ row }">
          <div>{{ row.typeGroupId }} <el-icon :size="16" style="align-self: flex-end;">
              <Edit />
            </el-icon></div>
        </template>
      </vxe-column>
      <vxe-column title="操作">
        <template #default="{ row }">
          <vxe-button type="text" status="primary" content="编辑" @click="row.editing = !row.editing"></vxe-button>
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
      <vxe-button type="text" status="success" @click="getSelectEvent">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import {
  apiAddDataTypeMapping,
  apiListAllDataTypeMappings,
  apiListAllMappableDataTypes,
  apiListTypeMappingGroupOptions,
} from "@/api/datatype"
import { Message } from "@/hooks/message"
import { reactive, ref } from "vue"
import { VxeTableDefines } from "vxe-table/types/table"
import { Edit } from '@element-plus/icons'

const table = ref()

const modalShowRef = ref()
const modal1ShowRef = ref()
const loading = ref()

interface FormDataVO {
  name: string
  nickname: string
  sex: string
  age: number
  status: string
  date: string
  active: boolean
  flagList: string[]
}

const pageVo = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0,
})

const formData = reactive<FormDataVO>({
  name: "",
  nickname: "",
  sex: "",
  age: 30,
  status: "1",
  date: "",
  active: false,
  flagList: [],
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
  apiListAllDataTypeMappings(undefined, undefined).then((res) => {
    tableData.value = res.data
    pageVo.value.total = res.total || 0
  })
}

const addNewMapping = () => {
  tableData.value.push({
    typeId: undefined,
    typeName: "",
    anotherTypeId: undefined,
    groupId: 0,
    groupName: "",
    typeKey: undefined,
    anotherTypeKey: undefined
  })
}

/**
 * 删除数据类型映射关系
 * @param mapping 
 */
const removeDataTypeMapping = (mapping: DataTypeMapping) => {
  table.value.remove(mapping)
}

const tableRef = ref()

const getSelectEvent = () => {
  const $table = tableRef.value
  if ($table) {
    const selectRecords: any[] = $table.getCheckboxRecords()

    const list = []
    for (let index = 0; index < selectRecords.length; index++) {
      const element = selectRecords[index]
      list.push({
        typeId: element.typeId,
        anotherTypeId: undefined,
      })
    }
    apiAddDataTypeMapping(list).then((res) => {
      Message.info("保存成功")
      // 刷新列表
      queryAllDataTypeMappings()
    })
  }
}

const editingTypeId = ref<number>()

const getSelctableDataTypes = (id: number | undefined = undefined) => {
  let f = false
  if (editingTypeId.value == undefined) {
    editingTypeId.value = id
    f = true
  } else if (id != editingTypeId.value) {
    f = true
  }
  if (f) {
    apiListAllMappableDataTypes(id).then((res) => {
      mappableDataTypes.value = res.data
    })
  }
}

/**
 * 单元格双击事件处理
 * @param params
 */
const cellDbClickHandler = (
  params: VxeTableDefines.CellDblclickEventParams<any>
) => {
  if (params.$columnIndex == 1) {
    getSelctableDataTypes(params.row.typeId)
    modal1ShowRef.value = true
    loading.value = false
  }
}

const showModal = () => {

  addNewMapping()


  // getSelctableDataTypes()
  // modal1ShowRef.value = true
  // loading.value = false
  // modal1ShowRef.value = true
}

defineExpose({
  show: () => {
    modalShowRef.value = true

    apiListTypeMappingGroupOptions().then((res) => {
      mappingGroups.value = res.data
    })

    queryAllDataTypeMappings()
  },
})
</script>
