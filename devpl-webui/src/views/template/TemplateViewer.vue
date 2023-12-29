<!--
 * @ 查看模板
 * @author Von
 * @date 2023/7/31 13:06
-->
<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { nextTick, ref } from "vue"

const templateDialogShowing = ref()
const titleRef = ref()
const templateContentEditorRef = ref()

function init(title: string, content: string) {
  titleRef.value = title
  templateDialogShowing.value = true
  nextTick(() => templateContentEditorRef.value.setText(content))
}

defineExpose({
  init: init,
})
</script>

<template>
  <vxe-modal
    v-model="templateDialogShowing"
    :title="titleRef"
    :draggable="false"
    width="80%"
    show-zoom
    resize
    show-footer
  >
    <monaco-editor
      ref="templateContentEditorRef"
      language="freemarker2"
      height="600px"
      read-only
    ></monaco-editor>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
