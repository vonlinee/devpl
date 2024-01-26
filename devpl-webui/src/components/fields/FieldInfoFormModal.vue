<!-- 
  字段信息表单, 编辑不和后台交互
  包含字段值编辑，和字段管理那里的表单有区别
 -->
<template>
  <vxe-modal title="字段编辑" v-model="visible" show-close show-footer @close="resetFields">
    <el-form :inline="true" :model="formData" class="demo-form-inline">
      <el-form-item label="fieldKey">
        <el-input v-model="formData.fieldKey" placeholder="英文名" clearable />
      </el-form-item>
      <el-form-item label="数据类型">
        <el-select v-model="formData.dataType" placeholder="Activity zone" clearable>
          <el-option label="Zone one" value="shanghai" />
          <el-option label="Zone two" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="描述信息">
        <vxe-textarea v-model="formData.description" resize="none" rows="5" placeholder="描述信息"></vxe-textarea>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { ref, toRaw } from 'vue';
import { toArray } from 'xe-utils';

const visible = ref()

const formData = ref<FieldInfo>({
  fieldKey: "",
  id: undefined,
  parentId: undefined,
  dataType: "Any",
  description: ""
})

let current: FieldInfo | undefined = undefined

const resetFields = () => {
  formData.value = {
    fieldName: "",
    fieldKey: "",
    dataType: "Any",
    defaultValue: "",
    description: "",
  }
}

/**
 * 提交
 */
const onSubmit = () => {
  if (current) {
    visible.value = false
    Object.assign(current, formData.value)
    resetFields()
  }
}

defineExpose({
  show(f?: FieldInfo) {
    if (f) {
      current = f
      formData.value = Object.assign({}, toRaw(f))
      visible.value = true
    }
  }
})

</script>