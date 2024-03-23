<!-- 
  类型选择弹窗
 -->
<template>
  <vxe-modal
    v-model="modalShowRef"
    title="类型映射"
    width="80%"
    show-footer
    :draggable="false"
    @confirm="submit($event)"
  >
    <el-row>
      <el-col :span="12">
        <vxe-form :data="formData1">
          <vxe-form-item title="类型分组" field="groupId">
            <template #default="{ data }">
              <el-select
                v-model="data.typeGroupId"
                @change="refreshTableData1"
                style="z-index: 9999"
              >
                <el-option
                  v-for="g in typeGroupOptions1"
                  :label="g.label"
                  :value="g.value"
                  :key="g.key"
                ></el-option>
              </el-select>
            </template>
          </vxe-form-item>
          <vxe-form-item
            title="类型Key"
            field="typeKeyPattern"
            :item-render="{}"
          >
            <template #default="{ data }">
              <vxe-input
                v-model="data.typeKeyPattern"
                placeholder="输入类型Key搜索"
                clearable
              ></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item
            title="类型名称"
            field="typeNamePattern"
            :item-render="{}"
          >
            <template #default="{ data }">
              <vxe-input
                v-model="data.typeNamePattern"
                placeholder="输入类型名称搜索"
                clearable
              ></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item>
            <vxe-button
              status="primary"
              content="查询"
              @click="refreshTableData1"
            ></vxe-button>
          </vxe-form-item>
        </vxe-form>

        <!-- 表格 -->
        <vxe-table
          ref="table1"
          border
          :data="tableData1"
          height="400px"
          @radio-change="handlePrimaryTypeChange"
        >
          <vxe-column type="radio" width="40" align="center"> </vxe-column>
          <vxe-column
            title="类型分组"
            width="20%"
            align="center"
            field="typeGroupId"
          >
          </vxe-column>
          <vxe-column
            field="typeKey"
            title="类型Key"
            align="center"
            :edit-render="{}"
          >
          </vxe-column>
          <vxe-column
            field="localeTypeName"
            title="类型名称"
            align="center"
            :edit-render="{}"
          >
          </vxe-column>
        </vxe-table>
        <vxe-pager
          v-model:current-page="pageVo1.page"
          v-model:page-size="pageVo1.limit"
          :total="pageVo1.total"
          @page-change="refreshTableData1"
        />
      </el-col>

      <el-col :span="12">
        <vxe-form :data="formData2">
          <vxe-form-item title="类型分组" field="groupId">
            <template #default="{ data }">
              <el-select
                v-model="data.typeGroupId"
                @change="refreshTableData2"
                style="z-index: 9999"
              >
                <el-option
                  v-for="g in typeGroupOptions2"
                  :label="g.label"
                  :value="g.value"
                  :key="g.key"
                ></el-option>
              </el-select>
            </template>
          </vxe-form-item>
          <vxe-form-item
            title="类型Key"
            field="typeKeyPattern"
            :item-render="{}"
          >
            <template #default="{ data }">
              <vxe-input
                v-model="data.typeKeyPattern"
                placeholder="输入类型Key搜索"
                clearable
              ></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item
            title="类型名称"
            field="typeNamePattern"
            :item-render="{}"
          >
            <template #default="{ data }">
              <vxe-input
                v-model="data.typeNamePattern"
                placeholder="输入类型名称搜索"
                clearable
              ></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item>
            <vxe-button
              status="primary"
              content="查询"
              @click="refreshTableData2"
            ></vxe-button>
          </vxe-form-item>
        </vxe-form>

        <!-- 表格 -->
        <vxe-table
          ref="table2"
          border
          :data="tableData2"
          height="400px"
          :radio-config="{ highlight: true }"
        >
          <vxe-column type="checkbox" width="40" align="center"> </vxe-column>
          <vxe-column
            title="类型分组"
            width="20%"
            align="center"
            field="typeGroupId"
          >
          </vxe-column>
          <vxe-column
            field="typeKey"
            title="类型Key"
            align="center"
            :edit-render="{}"
          >
          </vxe-column>
          <vxe-column
            field="localeTypeName"
            title="类型名称"
            align="center"
            :edit-render="{}"
          >
          </vxe-column>
        </vxe-table>
        <vxe-pager
          v-model:current-page="pageVo2.page"
          v-model:page-size="pageVo2.limit"
          :total="pageVo2.total"
          @page-change="refreshTableData2"
        />
      </el-col>
    </el-row>
  </vxe-modal>
</template>

<script setup lang="ts">
import {
  apiAddDataTypeMapping,
  apiListMappableAnotherTypeOptions,
  apiListSelectablePrimaryTypeOptions,
  apiListTypeGroupOptions,
} from "@/api/datatype"
import { Message } from "@/hooks/message"
import { reactive, ref, toRaw } from "vue"
import { VxeModalDefines } from "vxe-table/types/all"

const table1 = ref()
const table2 = ref()

const modalShowRef = ref()
const typeGroupOptions1 = ref()
const typeGroupOptions2 = ref()

const formData = reactive<{
  groupId?: number
  primaryTypeId?: number
  excludeTypeId?: number
}>({
  groupId: undefined,
  primaryTypeId: undefined,
  excludeTypeId: undefined,
})

const formData1 = reactive({
  typeGroupId: "",
  typeNamePattern: "",
  typeKeyPattern: "",
})

const formData2 = reactive({
  typeId: undefined,
  excludeTypeId: undefined,
  typeGroupId: "",
  typeNamePattern: "",
  typeKeyPattern: "",
})

const refreshTableData1 = () => {
  if (formData.primaryTypeId == null) {
    const pageParam = pageVo1.value
    const param = toRaw(formData1)
    apiListSelectablePrimaryTypeOptions({
      page: pageParam.page,
      limit: pageParam.limit,
      ...param,
    }).then((res) => {
      pageVo1.value.total = res.total || 0
      tableData1.value = res.data
    })
  }
}

const refreshTableData2 = () => {
  if (formData.primaryTypeId == null) {
    const pageParam = pageVo2.value
    const param = toRaw(formData2)
    apiListMappableAnotherTypeOptions({
      page: pageParam.page,
      limit: pageParam.limit,
      ...param,
    }).then((res) => {
      pageVo2.value.total = res.total || 0
      tableData2.value = res.data
    })
  }
}

/**
 * 主类型变更，刷新可映射的类型列表
 */
const handlePrimaryTypeChange = (param: any) => {
  let excludeTypeGroupId = null
  if (param.newValue != null) {
    formData2.excludeTypeId = param.newValue.id
    excludeTypeGroupId = param.newValue.typeGroupId
  }
  refreshTableData2()

  apiListTypeGroupOptions(excludeTypeGroupId).then((res) => {
    typeGroupOptions2.value = res.data
  })
}

const emits = defineEmits(["selected", "close"])

const pageVo1 = ref<{
  page: number
  limit: number
  total: number
}>({
  page: 1,
  limit: 10,
  total: 0,
})

const pageVo2 = ref<{
  page: number
  limit: number
  total: number
}>({
  page: 1,
  limit: 10,
  total: 0,
})

// 编辑时的可选择的下拉列表

const tableData1 = ref<DataTypeItem[]>([])
const tableData2 = ref<DataTypeItem[]>([])

/**
 * 表单提交
 */
const submit = (event: VxeModalDefines.ConfirmEventParams) => {
  const type = table1.value.getRadioRecord(false)
  const anotherTypes = table2.value.getCheckboxRecords(false)

  if (type == null) {
    Message.error("未选择主类型")
    event.stopPropagation()
    return
  } else if (anotherTypes == null || anotherTypes.length == 0) {
    Message.error("未选择映射类型")
    event.stopPropagation()
    return
  } else {
    apiAddDataTypeMapping({
      groupId: formData.groupId,
      typeId: type.id,
      anotherTypeIds: anotherTypes.map((dt: DataTypeItem) => dt.id),
    }).then((res) => {
      if (res.data) {
        Message.info("添加成功")
        modalShowRef.value = false
      } else {
        Message.error("添加失败")
      }
    })

    emits("close")
  }
}

defineExpose({
  show: (groupId: number, typeId?: number) => {
    formData.primaryTypeId = typeId
    formData.groupId = groupId

    modalShowRef.value = true
    apiListTypeGroupOptions().then((res) => {
      typeGroupOptions1.value = res.data
    })
    refreshTableData1()
  },
})
</script>
