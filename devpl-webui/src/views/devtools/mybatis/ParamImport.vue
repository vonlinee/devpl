<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { ref } from "vue"
import { ElButton } from "element-plus"

const showRef = ref(false)
const activeName = ref("json")
const overrideMode = ref(1)

/**
 * 覆盖模式:
 * 1-智能合并: 根据参数名相等决定是否覆盖，不存在的进行追加
 * 2-完全覆盖
 */
const overrideModeOptions = ref([
  {
    label: "智能合并",
    value: 1,
  },
  {
    label: "完全覆盖",
    value: 2,
  },
])

const emits = defineEmits<{
  (e: "submit", type: string, input: string): void
}>()

// const importTabPane = ref()

const jsonEditorRef = ref()
const urlEditorRef = ref()

function getInputText(type: string) {
  let text = ''
  if (type == 'json') {
    text = jsonEditorRef.value.getText()
    // if (window.hasText(text)) {
    //   obj = JSON.stringify(text)
    // }
    // return text
  }
  if (type == 'url') {
    text = urlEditorRef.value.getText()
    // if (window.hasText(text)) {
    //   let url = new URL(text)
    //   obj = url.searchParams
    // }
    // return obj
  }
  return text
}

/**
 * 提交更改
 */
function submit() {
  const tabName = activeName.value
  emits("submit", tabName, getInputText(tabName))
}

defineExpose({
  init: () => {
    showRef.value = true
  },
})
</script>

<template>
  <vxe-modal v-model="showRef" size="small" title="参数导入" :show-footer="true" :z-index="2000" :width="800">
    <template #default>
      <el-tabs v-model="activeName" class="editor-tabs">
        <el-tab-pane label="JSON(仅支持最外层)" name="json">
          <div class="code-editor-box">
            <monaco-editor ref="jsonEditorRef" language="json" value=""></monaco-editor>
          </div>
        </el-tab-pane>
        <el-tab-pane label="URL" name="url">
          <div class="code-editor-box">
            <monaco-editor ref="urlEditorRef" language="text" value=""></monaco-editor>
          </div>
        </el-tab-pane>
      </el-tabs>
    </template>
    <template #footer>
      <el-row align="middle" style="margin-bottom: 1px">
        <el-col :span="2">
          <el-text class="mx-1">覆盖模式</el-text>
        </el-col>
        <el-col :span="8" :offset="1">
          <span style="display: flex">
            <el-select v-model="overrideMode" class="m-2">
              <el-option v-for="item in overrideModeOptions" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
          </span>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" @click="submit">确定</el-button>
        </el-col>
      </el-row>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss">
.editor-tabs>.el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.code-editor-box {
  height: 400px;
}
</style>
