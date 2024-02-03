<!--
 * @ 查看模板
 * @author Von
 * @date 2023/7/31 13:06
-->
<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { nextTick, ref } from "vue"

const visible = ref()
const titleRef = ref()
const templateContentEditorRef = ref()

const { readonly } = withDefaults(defineProps<{
  /**
   * 是否只读
   */
  readonly: boolean
}>(), {
  readonly: true
})

defineExpose({
  /**
   * 初始化
   * @param title 窗口标题
   * @param content 模板内容文本
   */
  init(title: string, content: string) {
    titleRef.value = title
    visible.value = true
    nextTick(() => templateContentEditorRef.value.setText(content))
  }
})
</script>

<template>
  <vxe-modal v-model="visible" :title="titleRef" :draggable="false" width="80%" show-zoom resize show-footer>
    <monaco-editor ref="templateContentEditorRef" language="freemarker2" height="600px"
      :read-only="readonly"></monaco-editor>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
