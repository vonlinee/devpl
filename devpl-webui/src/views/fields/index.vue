<!--
 * @ 字段管理列表
 * @author Von
 * @date 2023/8/29 14:23
-->
<script setup lang="ts">
import { apiListFields, apiSaveOrUpdateField } from "@/api/fields"
import { onMounted, reactive, ref } from "vue"

import {
  VxeFormPropTypes,
  VXETable,
  VxeTableEvents,
  VxeTableInstance,
} from "vxe-table"
import TypeMappingTable from "@/views/datatype/TypeMappingTable.vue"

/**
 * 表格数据模型
 */
interface RowVO {
  fieldId: string
  fieldKey: string
  fieldName: string
  dataType: string
  description: string
  defaultValue: string
}

const xTable = ref<VxeTableInstance>()



/**
 * 新增和修改表单
 */
const formData = reactive({
  fieldName: "",
  fieldKey: "",
  dataType: "int",
  defaultValue: "",
  description: "",
})

const submitLoading = ref(false)
const showEdit = ref(false)
const typeMappingTableRef = ref()
const selectRow = ref<RowVO | null>()
const tableData = ref<RowVO[]>([])

const showTypeMappingTable = () => {
  typeMappingTableRef.value.show()
}

const sexList = ref([
  { label: "int", value: "0" },
  { label: "double", value: "1" },
])

/**
 * 表单校验规则
 */
const formRules = reactive<VxeFormPropTypes.Rules>({
  description: [
    { required: true, message: "请输入名称" },
    { min: 3, max: 5, message: "长度在 3 到 5 个字符" },
  ],
  fieldKey: [{ required: true, message: "请输入字段Key" }],
  dataType: [{ required: true, message: "请选择字段数据类型" }],
})

const insertEvent = () => {
  Object.assign(formData, {
    fieldName: "",
    fieldKey: "",
    dataType: "int",
    defaultValue: "",
    description: "",
  })
  selectRow.value = null
  showEdit.value = true
}

const editEvent = (row: RowVO) => {
  Object.assign(formData, row)
  selectRow.value = row
  showEdit.value = true
}

const cellDBLClickEvent: VxeTableEvents.CellDblclick<RowVO> = ({ row }) => {
  editEvent(row)
}

const removeEvent = async (row: RowVO) => {
  const type = await VXETable.modal.confirm("您确定要删除该数据?")
  if (type === "confirm") {
    const $table = xTable.value
    if ($table) {
      $table.remove(row)
    }
  }
}

/**
 * 表单提交
 */
const submitEvent = () => {
  submitLoading.value = true
  const $table = xTable.value
  if ($table) {
    apiSaveOrUpdateField(formData)
      .then((res) => {
        if (res.code == 200) {
          VXETable.modal.message({ content: "保存成功", status: "success" })
          submitLoading.value = false
          showEdit.value = false
          refreshTableData()
        }
      })
      .catch(() => {
        submitLoading.value = false
      })
  }
}

function refreshTableData() {
  apiListFields().then((res) => {
    tableData.value = res.data.list
  })
}

onMounted(() => {
  refreshTableData()
})
</script>

<template>
  <el-card>
    <div>
      <vxe-toolbar>
        <template #buttons>
          <vxe-button icon="vxe-icon-square-plus" @click="insertEvent()"
            >新增</vxe-button
          >
          <vxe-button @click="showTypeMappingTable()">类型映射</vxe-button>
        </template>
      </vxe-toolbar>

      <vxe-table
        ref="xTable"
        :border="true"
        show-overflow
        height="525"
        :column-config="{ resizable: true }"
        :row-config="{ isHover: true, isCurrent: true }"
        :data="tableData"
        @cell-dblclick="cellDBLClickEvent"
      >
        <vxe-column type="seq" width="60"></vxe-column>
        <vxe-column field="fieldKey" title="字段Key"></vxe-column>
        <vxe-column field="fieldName" title="名称"></vxe-column>
        <vxe-column field="dataType" title="数据类型"></vxe-column>
        <vxe-column
          field="defaultValue"
          title="默认值"
          show-overflow
        ></vxe-column>
        <vxe-column
          field="description"
          title="描述信息"
          show-overflow
        ></vxe-column>
        <vxe-column title="操作" width="100" show-overflow>
          <template #default="{ row }">
            <vxe-button
              type="text"
              icon="vxe-icon-edit"
              @click="editEvent(row)"
            ></vxe-button>
            <vxe-button
              type="text"
              icon="vxe-icon-delete"
              @click="removeEvent(row)"
            ></vxe-button>
          </template>
        </vxe-column>
      </vxe-table>

      <vxe-modal
        v-model="showEdit"
        :title="selectRow ? '编辑&保存' : '新增&保存'"
        width="800"
        min-width="600"
        min-height="300"
        :loading="submitLoading"
        resize
        destroy-on-close
      >
        <template #default>
          <vxe-form
            :data="formData"
            :rules="formRules"
            title-align="right"
            title-width="100"
            @submit="submitEvent"
          >
            <vxe-form-item
              field="fieldKey"
              title="字段Key"
              :span="12"
              :item-render="{}"
            >
              <template #default="{ data }">
                <vxe-input v-model="data.fieldKey"></vxe-input>
              </template>
            </vxe-form-item>

            <vxe-form-item
              field="fieldName"
              title="字段名称"
              :span="12"
              :item-render="{}"
            >
              <template #default="{ data }">
                <vxe-input
                  v-model="data.fieldName"
                  placeholder="请输入字段名称"
                ></vxe-input>
              </template>
            </vxe-form-item>

            <vxe-form-item
              field="dataType"
              title="数据类型"
              :span="12"
              :item-render="{}"
            >
              <template #default="{ data }">
                <vxe-select v-model="data.dataType" transfer>
                  <vxe-option
                    v-for="item in sexList"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label"
                  ></vxe-option>
                </vxe-select>
              </template>
            </vxe-form-item>
            <vxe-form-item
              field="defaultValue"
              title="默认值"
              :span="12"
              :item-render="{}"
            >
              <template #default="{ data }">
                <vxe-input
                  v-model="data.defaultValue"
                  placeholder="默认值"
                ></vxe-input>
              </template>
            </vxe-form-item>
            <vxe-form-item
              field="description"
              title="描述信息"
              :span="24"
              :item-render="{}"
            >
              <template #default="{ data }">
                <vxe-textarea
                  v-model="data.description"
                  resize="none"
                  rows="5"
                  placeholder="描述信息"
                ></vxe-textarea>
              </template>
            </vxe-form-item>
            <vxe-form-item align="center" title-align="left" :span="24">
              <template #default>
                <vxe-button type="submit">提交</vxe-button>
                <vxe-button type="reset">重置</vxe-button>
              </template>
            </vxe-form-item>
          </vxe-form>
        </template>
      </vxe-modal>
    </div>
  </el-card>

  <type-mapping-table ref="typeMappingTableRef"></type-mapping-table>

</template>

<style scoped lang="scss"></style>
