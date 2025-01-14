<template>
  <vxe-modal
    v-model="showEdit"
    :title="modalTitle"
    width="800"
    min-width="600"
    min-height="300"
    :loading="submitLoading"
    @close="resetFields"
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
          field="typeGroupId"
          title="类型分组"
          :span="12"
          :item-render="{}"
        >
          <template #default="{ data }">
            <vxe-select
              v-model="data.typeGroupId"
              transfer
              clearable
              filterable
              @change="(val) => onTypeGroupChange(val, data)"
            >
              <vxe-option
                v-for="item in typeGroupOptions"
                :key="item.key"
                :value="item.value"
                :label="item.label"
              ></vxe-option>
            </vxe-select>
          </template>
        </vxe-form-item>
        <vxe-form-item
          field="dataType"
          title="数据类型"
          :span="12"
          :item-render="{}"
        >
          <template #default="{ data }">
            <vxe-select v-model="data.dataType" transfer clearable filterable>
              <vxe-option
                v-for="item in fieldDataTypeOptions"
                :key="item.key"
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
            <vxe-button status="success" type="submit">提交</vxe-button>
            <vxe-button status="danger" type="reset">重置</vxe-button>
          </template>
        </vxe-form-item>
      </vxe-form>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { apiSaveOrUpdateField } from "@/api/fields"
import { VxeFormPropTypes } from "vxe-table"
import { onMounted, reactive, ref, toRaw } from "vue"
import { ElMessage } from "element-plus"
import { apiListDataTypeOptions, apiListTypeGroupOptions } from "@/api/datatype"

const showEdit = ref(false)
const modalTitle = ref()
const submitLoading = ref(false)

const fieldDataTypeOptions = ref<SelectOptionVO[]>([])
const typeGroupOptions = ref<SelectOptionVO[]>([])

/**
 * 新增和修改表单
 */
const formData = ref({
  fieldName: "",
  fieldKey: "",
  dataType: "int",
  typeGroupId: "",
  defaultValue: "",
  description: "",
})

const resetFields = () => {
  formData.value = {
    fieldName: "",
    fieldKey: "",
    typeGroupId: "",
    dataType: "int",
    defaultValue: "",
    description: "",
  }
}

const getDataTypes = (typeGroupId: string) => {
  apiListDataTypeOptions(typeGroupId).then((res) => {
    fieldDataTypeOptions.value = res.data
  })
}

/**
 * 类型分组下拉菜单联动
 * @param param
 * @param row
 */
const onTypeGroupChange = (param: any, row: any) => {
  getDataTypes(param.value)
}

onMounted(() => {
  apiListTypeGroupOptions().then((res) => {
    typeGroupOptions.value = res.data
    if (res.data?.length > 0) {
      formData.value.typeGroupId = res.data[0].value
      apiListDataTypeOptions(formData.value.typeGroupId).then((res) => {
        fieldDataTypeOptions.value = res.data
      })
    }
  })
})

/**
 * 表单校验规则
 */
const formRules = reactive<VxeFormPropTypes.Rules>({
  fieldKey: [{ required: true, content: "请输入字段Key" }],
  dataType: [{ required: true, content: "请选择字段数据类型" }],
  typeGroupId: [{ required: true, content: "请选择字段数据类型" }],
})

const emits = defineEmits(["refresh-table"])

/**
 * 新增字段表单提交
 */
const submitEvent = () => {
  // 表单填充默认值
  submitLoading.value = true
  apiSaveOrUpdateField(toRaw(formData.value))
    .then((res) => {
      ElMessage({
        message: "保存成功",
        duration: 500,
      })
      submitLoading.value = false
      emits("refresh-table")
      showEdit.value = false
    })
    .catch(() => {
      submitLoading.value = false
    })
}

defineExpose({
  show: (row?: any) => {
    if (row) {
      modalTitle.value = "编辑&保存"
      formData.value = row
    } else {
      modalTitle.value = "新增&保存"
    }
    showEdit.value = true
  },
})
</script>

<style lang="scss" scoped></style>
