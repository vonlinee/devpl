<!-- 
  字段信息表单, 此处编辑不和后台交互
  包含字段值编辑，和字段管理那里的表单有区别
 -->
<template>
  <vxe-modal title="字段编辑" v-model="visible" show-close show-footer @close="resetFields">
    <el-form :model="formData" class="demo-form-inline" label-width="80px" label-position="left">
      <el-form-item label="fieldKey">
        <el-input v-model="formData.fieldKey" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="数据类型">
        <el-select v-model="formData.dataType" placeholder="" allow-create clearable filterable>
          <el-option :label="dataType.label" :value="dataType.value" v-for="dataType in dataTypeOptions" />
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
import { ref, toRaw } from "vue";

/**
 * 可选择的数据类型
 */
const dataTypeOptions = ref<DataTypeSelectOption[]>([
  {
    label: "String",
    value: "String",
    key: "string"
  },
  {
    label: "Integer",
    value: "Integer",
    key: "Integer"
  },
  {
    label: "Boolean",
    value: "Boolean",
    key: "Boolean"
  },
  {
    label: "Array",
    value: "Array",
    key: "Array"
  },
  {
    label: "Object",
    value: "Object",
    key: "Object"
  },
  {
    label: "Number",
    value: "Number",
    key: "Number"
  },
  {
    label: "null",
    value: "null",
    key: "null"
  },
  {
    label: "any",
    value: "any",
    key: "any"
  }
]);

const visible = ref();

const formData = ref<FieldInfo>({
  fieldKey: "",
  id: undefined,
  parentId: undefined,
  dataType: "Any",
  description: ""
});

let current: FieldInfo | undefined = undefined;

const resetFields = () => {
  formData.value = {
    fieldName: "",
    fieldKey: "",
    dataType: "Any",
    defaultValue: "",
    description: ""
  };
};

/**
 * 提交
 */
const onSubmit = () => {
  if (current) {
    visible.value = false;
    Object.assign(current, formData.value);
    resetFields();
  }
};

defineExpose({
  show(f?: FieldInfo) {
    if (f) {
      current = f;
      formData.value = Object.assign({}, toRaw(f));
      visible.value = true;
    }
  }
});

</script>