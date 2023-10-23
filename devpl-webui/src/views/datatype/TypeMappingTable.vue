<!-- 
  类型映射配置表
 -->
<template>
  <vxe-modal v-model="modalShowRef" title="类型映射表" width="60%">

    <vxe-form :data="formData">
      <vxe-form-item title="类型组" field="name" :item-render="{}" span="10">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型ID" field="name" :item-render="{}" span="10">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="查询" @click="queryAllDataTypeMappings"></vxe-button>
      </vxe-form-item>
      <vxe-form-item>
        <vxe-button status="primary" content="新增" @click="showModal"></vxe-button>
      </vxe-form-item>
    </vxe-form>

    <vxe-table :border="true" :data="tableData" @cell-dblclick="cellDbClickHandler">
      <vxe-column title="主类型" width="10%" align="center" field="typeName"></vxe-column>
      <vxe-column field="name" title="映射类型" align="center" :edit-render="{}">
        <template #default="{ row }">
          <span>{{ row.typeGroupId }}</span>
        </template>
      </vxe-column>
    </vxe-table>

    <vxe-pager v-model:current-page="pageVo.currentPage" v-model:page-size="pageVo.pageSize" :total="pageVo.total" />
  </vxe-modal>

  <vxe-modal v-model="modal1ShowRef" title="选择映射的数据类型" :mask="true" :show-footer="true">
    <vxe-table ref="tableRef" :border="true" :data="mappeableDataTypes">
      <vxe-column type="checkbox" width="40" align="center"></vxe-column>
      <vxe-column field="typeGroupId" title="数据类型组" align="center"></vxe-column>
      <vxe-column field="typeName" title="数据类型" align="center"></vxe-column>
    </vxe-table>
    <template #footer>
      <vxe-button type="text" status="success" @click="getSelectEvent">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiAddDataTypeMapping, apiListAllDataTypeMappings, apiListAllMappableDataTypes } from "@/api/datatype";
import { onMounted, reactive, ref } from "vue";
import { VxeTableDefines } from "vxe-table/types/table";

import { useMessage } from "@/hooks/useElMessage";

const modalShowRef = ref();
const modal1ShowRef = ref();
const loading = ref();

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
  pageSize: 30,
  total: 8
})

const formData = reactive<FormDataVO>({
  name: '',
  nickname: '',
  sex: '',
  age: 30,
  status: '1',
  date: '',
  active: false,
  flagList: []
})

interface DataTypeSelectVO {
  mapped: boolean,
  typeId: number
}

// 编辑时的可选择的下拉列表
const mappeableDataTypes = ref<DataTypeSelectVO[]>([])

interface RowVO {
  typeId: number | undefined
  typeName: string,
  anotherTypeId: string | undefined,
}

const tableData = ref<RowVO[]>([])

const queryAllDataTypeMappings = () => {
  apiListAllDataTypeMappings(undefined, undefined).then((res) => {
    if (res.code == 200) {
      tableData.value = res.list
    }
  })
}

onMounted(() => {
  queryAllDataTypeMappings()
})

const tableRef = ref()

const getSelectEvent = () => {
  const $table = tableRef.value
  if ($table) {
    const selectRecords: any[] = $table.getCheckboxRecords()

    const list = []
    for (let index = 0; index < selectRecords.length; index++) {
      const element = selectRecords[index];
      list.push({
        typeId: element.typeId,
        anotherTypeId: undefined
      })
    }
    apiAddDataTypeMapping(list).then((res) => {
      if (res.code == 200) {
        useMessage(modal1ShowRef).info("保存成功")
      }
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
      if (res.code == 200) {
        mappeableDataTypes.value = res.list
      }
    })
  }
}


/**
 * 单元格双击事件处理
 * @param params 
 */
const cellDbClickHandler = (params: VxeTableDefines.CellDblclickEventParams<any>) => {
  if (params.$columnIndex == 1) {
    getSelctableDataTypes(params.row.typeId)
    modal1ShowRef.value = true
    loading.value = false
  }
}

const showModal = () => {
  getSelctableDataTypes()
  modal1ShowRef.value = true
  loading.value = false
  modal1ShowRef.value = true
}

defineExpose({
  show: () => modalShowRef.value = true
});

</script>
