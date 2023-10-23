<template>
  <div ref="editorRef" class="editor-box"></div>
</template>

<script lang="ts" setup>
import { basicSetup, EditorView } from "codemirror";
import { Compartment, EditorState } from "@codemirror/state";
import { javascript } from "@codemirror/lang-javascript";
import { json } from "@codemirror/lang-json";
import { onMounted, ref } from "vue";

const editorRef = ref(), editorView = ref();

const language = new Compartment, tabSize = new Compartment;

/**
 * https://codemirror.net/examples/config/
 */
const initEditor = () => {
  if (typeof editorView.value !== "undefined") {
    editorView.value.destroy();
  }
  const jsonString = "";
  const state: EditorState = EditorState.create({
    doc: jsonString,
    extensions: [basicSetup, language.of(javascript()), json(), tabSize.of(EditorState.tabSize.of(8))]
  });
  if (editorRef.value) {
    editorView.value = new EditorView({
      state: state,
      parent: editorRef.value
    });
  }
};
// onMounted生命周期可以保证读取到dom元素
onMounted(() => {
  initEditor();
});

defineExpose({
  setTabSize: (view: EditorView, size: number) => {
    view.dispatch({
      effects: tabSize.reconfigure(EditorState.tabSize.of(size))
    });
  }
});

</script>

<style lang="scss" scoped>
.editor-box {
  width: 100vw;
  height: 100vh;
}
</style>
