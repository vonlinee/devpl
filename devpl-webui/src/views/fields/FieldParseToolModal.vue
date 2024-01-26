<script setup lang="ts">
import { ref } from "vue"
import { ElMessage } from "element-plus"
import { apiParseFields } from "@/api/fields"
import { isBlank } from "@/utils/tool"
import { Splitpanes, Pane } from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import FieldParserInput from "./FieldParserInput.vue"
import FieldTree from "@/components/fields/FieldTree.vue"

const modalVisible = ref()

const init = () => {
  modalVisible.value = true
}

defineExpose({
  init: init,
})

const fields = ref<FieldInfo[]>()

const fieldParserInputRef = ref()
const fieldTableRef = ref()

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
    const existed = fields.value || []
    // 合并
    res.data?.fields.forEach((i: any) => {
      if (!existed.find((f) => f.fieldKey == i.fieldKey)) {
        existed?.push(i)
      }
    })
    fieldTableRef.value.setFields(existed)
  })
}

const emits = defineEmits([
  // 完成
  "finished",
])

const onModalClose = () => {
  emits("finished", fields.value)
}
</script>

<template>
  <vxe-modal v-model="modalVisible" title="字段解析" :draggable="false" show-footer :mask-closable="false" width="80%"
    height="80%" :z-index="2000" @close="onModalClose">
    <template #default>
      <Splitpanes>
        <Pane>
          <FieldParserInput ref="fieldParserInputRef"></FieldParserInput>
        </Pane>
        <Pane>
          <FieldTree ref="fieldTableRef"></FieldTree>
        </Pane>
      </Splitpanes>
    </template>
    <template #footer>
      <vxe-button status="primary" @click="parseFields">解析</vxe-button>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss">
.demo-tabs>.el-tabs__content {
  height: 100%;
  overflow-y: scroll;
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
