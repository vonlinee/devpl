<template>
  <vxe-modal v-model="visible" title="导入字段分组" show-footer width="80%" height="80%">
    <el-row>
      <el-col :span="12">
        <FieldParserInput ref="fieldParserInputRef"></FieldParserInput>
      </el-col>
      <el-col :span="12">
        <FieldTreeTable ref="fieldTableRef"></FieldTreeTable>
      </el-col>
    </el-row>
    <template #footer>
      <vxe-button @click="parseFields">解析</vxe-button>
      <vxe-button>取消</vxe-button>
      <vxe-button status="primary">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>

import FieldTreeTable from "@/components/fields/FieldTreeTable.vue";
import FieldParserInput from "../FieldParserInput.vue";
import { reactive, ref, toRaw } from "vue";
import { isBlank } from "@/utils/tool";
import { ElMessage } from "element-plus";
import { apiParseFields } from "@/api/fields";

const visible = ref();

const fieldParserInputRef = ref()
const fieldTableRef = ref()

/**
 * 字段映射规则
 * 指定列的索引号(从1开始)或者列名称与字段含义的对应关系
 */
 const columnMappingForm = reactive({
  fieldNameColumn: "1",
  fieldTypeColumn: "2",
  fieldDescColumn: "3"
});


const parseFields = () => {
  const inputType: string = fieldParserInputRef.value.getInputType()
  let text = fieldParserInputRef.value.getParseableText()
  if (isBlank(text)) {
    ElMessage("输入文本为空");
    return;
  }
  apiParseFields({
    type: inputType, content: text,
    ...toRaw(columnMappingForm)
  }).then((res) => {
    fieldTableRef.value.setFields(res.data)
  });
};

defineExpose({
  show() {
    visible.value = true;
  }
});

</script>

<style scoped>

</style>