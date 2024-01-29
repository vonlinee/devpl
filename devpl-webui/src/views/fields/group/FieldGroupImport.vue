<template>
  <vxe-modal v-model="visible" title="导入字段分组" show-footer width="90%" height="90%" @close="handleClose">
    <Splitpanes>
      <Pane>
        <FieldParserInput ref="fieldParserInputRef"></FieldParserInput>
      </Pane>
      <Pane>
        <FieldTree ref="fieldTableRef"></FieldTree>
      </Pane>
    </Splitpanes>
    <template #footer>
      <vxe-button @click="parseFields">解析</vxe-button>
      <vxe-button>取消</vxe-button>
      <vxe-button status="primary" @click="submit">确定</vxe-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import FieldParserInput from "../FieldParserInput.vue"
import FieldTree from "@/components/fields/FieldTree.vue"
import { ref, toRaw } from "vue";
import { isBlank } from "@/utils/tool"
import { ElMessage } from "element-plus"
import { apiParseFields } from "@/api/fields"

import { Pane, Splitpanes } from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import { Message } from "@/hooks/message"

const visible = ref()

const fieldParserInputRef = ref()
const fieldTableRef = ref()

const emits = defineEmits([
  "finished"
])

const handleClose = (flag?: boolean) => {
  const fields = fieldTableRef.value.getFields()
  if (flag) {
    visible.value = !flag
  }
  emits("finished", toRaw(fields))
}

const parseFields = () => {
  const inputType: string = fieldParserInputRef.value.getInputType()
  let text = fieldParserInputRef.value.getParseableText()
  if (isBlank(text)) {
    ElMessage("输入文本为空")
    return
  }
  let options = fieldParserInputRef.value.getOptions()
  apiParseFields({
    type: inputType,
    content: text,
    ...options,
  }).then((res) => {
    if (res.data?.failed) {
      Message.error(res.data?.errorMsg)
    }
    fieldTableRef.value.setFields(res.data.fields)
  })
}

const submit = () => {
  handleClose(true)
}

defineExpose({
  show() {
    visible.value = true
  },
})
</script>

<style scoped></style>
