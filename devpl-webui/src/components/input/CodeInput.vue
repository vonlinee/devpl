<template>
  <div>
    <div class="code-input_title_container">
      <span class="title_label">输入 {{ lang }}</span>
      <button class="btn-format" @click="formatCode">格式化</button>
    </div>
    <monaco-editor ref="editorRef" :language="lang" :text="text" height="400px"></monaco-editor>
  </div>
</template>

<script lang="ts" setup>

import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { computed, ref } from "vue";

const { lang, code } = defineProps<{
  lang: string,
  code?: string
}>();

const editorRef = ref();
const emits = defineEmits(["update:code"]);

const text = computed({
  get() {
    editorRef.value?.getText();
  },
  set(value) {
    editorRef.value?.setText(value);
  }
});

const formatCode = () => {

};


</script>

<style lang="scss" scoped>
.code-input_title_container {
  background-color: white;
  display: flex;
  flex-direction: row;
  padding: 5px;

  .title_label {
    flex: 1;
    padding: 5px 3px 5px 3px;
  }

  .btn-format {
    /* 设置按钮的背景颜色 */
    background-color: white; /* 绿色 */
    /* 设置按钮的边框 */
    border: none;
    /* 设置按钮的字体大小 */
    //font-size: 16px;

    /* 设置按钮的宽度和高度 */
    //width: 100px;
    //height: 50px;

    padding: 5px 3px 5px 3px;

    /* 设置按钮的文本对齐方式 */
    text-align: center;

    /* 设置按钮的边框圆角 */
    border-radius: 5px; /* 圆角 */
  }

  .btn-format:hover {
    cursor: pointer;
    background-color: #e2dfdf; /* 绿色hover状态 */
  }
}
</style>