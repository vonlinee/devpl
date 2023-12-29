<!-- 
    字段解析输入
 -->
<script setup lang="ts">
import { reactive, ref } from "vue"
import { TabsPaneContext } from "element-plus"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"

const activeTabName = ref("java")
const modalVisible = ref()

/**
 * 字段映射规则
 * 指定列的索引号(从1开始)或者列名称与字段含义的对应关系
 */
const columnMappingForm = reactive({
  fieldNameColumn: "1",
  fieldTypeColumn: "2",
  fieldDescColumn: "3",
})

const handleTabClicked = (tab: TabsPaneContext, event: Event) => {
  activeTabName.value = tab.paneName as string
}

const init = () => {
  modalVisible.value = true
}

const fields = ref<FieldInfo[]>()

type MonacoEditorType = typeof MonacoEditor

const javaEditorRef = ref<MonacoEditorType>()
// 仅支持mysql
const sqlEditorRef = ref<MonacoEditorType>()
// 支持json5
const jsonEditorRef = ref<MonacoEditorType>()
// TS/JS
const tsOrJsEditorRef = ref<MonacoEditorType>()
// html文本解析
const html1EditorRef = ref<MonacoEditorType>()
// html dom文本解析
const html2EditorRef = ref<MonacoEditorType>()

const getInputText = () => {
  const inputType: string = activeTabName.value
  let text = ""
  switch (inputType) {
    case "java":
      text = javaEditorRef.value?.getText()
      break
    case "sql":
      text = sqlEditorRef.value?.getText()
      break
    case "json":
      text = jsonEditorRef.value?.getText()
      break
    case "ts/js":
      text = tsOrJsEditorRef.value?.getText()
      break
    case "html1":
      text = html1EditorRef.value?.getText()
      break
    case "html2":
      text = html2EditorRef.value?.getText()
      break
    default:
      break
  }
  return text
}

const emits = defineEmits([
  // 完成
  "finished",
])

/**
 * 暴露API
 */
defineExpose({
  init: init,
  /**
   * 获取待解析的文本
   */
  getParseableText() {
    return getInputText()
  },
  /**
   * 输入类型
   */
  getInputType() {
    return activeTabName.value
  },
})
</script>

<template>
  <el-tabs
    v-model="activeTabName"
    class="demo-tabs"
    height="600px"
    @tab-click="handleTabClicked"
  >
    <el-tab-pane label="Java" name="java">
      <monaco-editor
        ref="javaEditorRef"
        language="java"
        height="480px"
      ></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="SQL" name="sql">
      <monaco-editor
        ref="sqlEditorRef"
        language="sql"
        height="480px"
      ></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="JSON" name="json">
      <monaco-editor
        ref="jsonEditorRef"
        language="json"
        height="480px"
      ></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="TS/JS" name="ts/js">
      <monaco-editor
        ref="jsonEditorRef"
        language="json"
        height="480px"
      ></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="HTML文本" name="html1">
      <monaco-editor
        ref="html1EditorRef"
        language="text"
        height="480px"
      ></monaco-editor>
      <el-card>
        <el-form
          :form="columnMappingForm"
          label-width="150"
          label-position="left"
        >
          <el-form-item label="字段名称列">
            <el-input v-model="columnMappingForm.fieldNameColumn"></el-input>
          </el-form-item>
          <el-form-item label="字段数据类型列">
            <el-input v-model="columnMappingForm.fieldTypeColumn"></el-input>
          </el-form-item>
          <el-form-item label="字段描述信息列">
            <el-input v-model="columnMappingForm.fieldDescColumn"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
    </el-tab-pane>
    <el-tab-pane label="HTML Dom" name="html2">
      <monaco-editor
        ref="html2EditorRef"
        language="html"
        height="480px"
      ></monaco-editor>
      <el-card>
        <el-form
          :form="columnMappingForm"
          label-width="150"
          label-position="left"
        >
          <el-form-item label="字段名称列">
            <el-input v-model="columnMappingForm.fieldNameColumn"></el-input>
          </el-form-item>
          <el-form-item label="字段数据类型列">
            <el-input v-model="columnMappingForm.fieldTypeColumn"></el-input>
          </el-form-item>
          <el-form-item label="字段描述信息列">
            <el-input v-model="columnMappingForm.fieldDescColumn"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
    </el-tab-pane>
  </el-tabs>
</template>

<style scoped lang="scss">
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
