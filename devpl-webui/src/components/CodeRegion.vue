<!--代码区域-->
<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import LanguageSelector from "@/components/LanguageSelector.vue"
import { ref } from "vue"
import { CopyDocument } from "@element-plus/icons-vue"
import { ElIcon, ElMessage } from "element-plus"

const props = withDefaults(
  defineProps<{
    lang: string
    langSelector?: boolean
    /**
     * 是否需要格式化按钮
     */
    format?: boolean
  }>(),
  {
    lang: "text",
    languageSelector: false,
    format: false,
  }
)

const languageMode = ref(props.lang)
const editorRef = ref()
const value = ref("")
const containerRef = ref()

function copyTextClipboard() {
  const text = editorRef.value.getText()
  // 写入文本到剪贴板
  navigator.clipboard.writeText(text).then(
    function () {
      ElMessage({
        message: "复制成功",
        center: true,
      })
    },
    function () {
      ElMessage({
        message: "复制失败",
        center: true,
      })
    }
  )
}

defineExpose({
  getText: function () {
    return editorRef.value.getText()
  },
  setText: function (val: string) {
    editorRef.value.setText(val)
  },
})
</script>

<template>
  <div ref="containerRef" class="code-region-container">
    <div class="code-region-toolbar">
      <LanguageSelector v-if="props.languageSelector"></LanguageSelector>
      <div style="flex: 1; text-align: right">
        <button v-if="props.format" class="toolbar-button">格式化</button>
        <button class="toolbar-button" @click="copyTextClipboard">
          <el-icon style="position: relative; top: 2px">
            <CopyDocument></CopyDocument>
          </el-icon>
          复制代码
        </button>
      </div>
    </div>
    <div class="editor-container">
      <monaco-editor ref="editorRef" :language="languageMode"></monaco-editor>
    </div>
  </div>
</template>

<style scoped lang="scss">
.code-region-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  // overflow-y: scroll;

  .editor-container {
    flex-grow: 1;
  }
}

.code-region-toolbar {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  .toolbar-button {
    border: none;
    background: rgba(255, 255, 255, 0.5);
    /*整个按钮的不透明度，会影响到文字，0完全透明，1完全不透明*/
    // opacity: 0.7;
    padding: 5px 10px 5px 10px;
    color: #000;
    text-align: center;
    align-items: center;
    text-decoration: none;
    display: inline-block;
    font-family: Arial, sans-serif;
    transition: all 0.3s ease;
    vertical-align: middle;
    line-height: 18px;
    justify-content: center;
    cursor: pointer;
  }

  .toolbar-button:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  }
}
</style>
