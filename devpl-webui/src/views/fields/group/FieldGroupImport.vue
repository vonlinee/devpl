<template>
  <vxe-modal v-model="visible" title="导入字段分组" show-footer width="90%" height="90%">
    <Splitpanes>
      <Pane>
        <FieldParserInput ref="fieldParserInputRef"></FieldParserInput>
      </Pane>
      <Pane>
        <FieldTreeTable ref="fieldTableRef"></FieldTreeTable>
      </Pane>
    </Splitpanes>
    <template #footer>
      <vxe-button @click="parseFields">解析</vxe-button>
      <vxe-button>取消</vxe-button>
      <vxe-button status="primary">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import FieldTreeTable from "@/components/fields/FieldTreeTable.vue"
import FieldParserInput from "../FieldParserInput.vue"
import { ref } from "vue"
import { isBlank } from "@/utils/tool"
import { ElMessage } from "element-plus"
import { apiParseFields } from "@/api/fields"

import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

const visible = ref()

const fieldParserInputRef = ref()
const fieldTableRef = ref()

const parseFields = () => {
  const inputType: string = fieldParserInputRef.value.getInputType()
  let text = fieldParserInputRef.value.getParseableText()
  if (isBlank(text)) {
    ElMessage("输入文本为空")
    return
  }
  let options = fieldParserInputRef.value.getOptions();
  apiParseFields({
    type: inputType,
    content: text,
    options,
  }).then((res) => {
    fieldTableRef.value.setFields(res.data)
  })
}

defineExpose({
  show() {
    visible.value = true
  },
})
</script>

<style scoped></style>
