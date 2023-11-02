<script setup lang="ts">
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import MonacoEditor from '@/components/editor/MonacoEditor.vue';
import SimpleModal from '@/components/dialog/SimpleModal.vue';
import { apiParseFields } from '@/api/fields';
import { isBlank } from '@/utils/tool';

const activeTabName = ref('java')
const modal = ref<typeof SimpleModal>()

const handleClick = (tab: TabsPaneContext, event: Event) => {
  
}

const init = () => {
  modal.value?.show()
}

defineExpose({
  init: init
})

const javaEditorRef = ref<typeof MonacoEditor>()
// 仅支持mysql
const sqlEditorRef = ref<typeof MonacoEditor>()
// 支持json5
const jsonEditorRef = ref<typeof MonacoEditor>()
const submit = () => {
  const inputType: string = activeTabName.value
  let text = ''
  switch (inputType) {
    case 'java':
      text = javaEditorRef.value?.getText()
      break;
    case 'sql':
      text = sqlEditorRef.value?.getText()
      break;
    case 'json':
      text = jsonEditorRef.value?.getText()
      break;
    default:
      break;
  }
  if (isBlank(text)) {
    alert("输入文本为空")
  }
  apiParseFields({ type: inputType, content: text }).then((res) => {
    console.log(res);
  })
}
</script>

<template>
  <simple-modal ref="modal" title="导入字段" @on-ok-button-clicked="submit">
    <el-tabs v-model="activeTabName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="Java" name="java">
        <monaco-editor ref="javaEditorRef" language="java" height="480px"></monaco-editor>
      </el-tab-pane>
      <el-tab-pane label="SQL" name="sql">
        <monaco-editor ref="sqlEditorRef" language="sql" height="480px"></monaco-editor>
      </el-tab-pane>
      <el-tab-pane label="JSON" name="json">
        <monaco-editor ref="jsonEditorRef" language="json" height="480px"></monaco-editor>
      </el-tab-pane>
    </el-tabs>
  </simple-modal>
</template>

<style scoped lang="scss">
.demo-tabs>.el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>