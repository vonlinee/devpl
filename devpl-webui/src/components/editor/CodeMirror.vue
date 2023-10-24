<template>
  <div ref="editorRef" class="editor-box"></div>
</template>

<script lang="ts" setup>
import { basicSetup, EditorView } from "codemirror";
import { Compartment, EditorState } from "@codemirror/state";
import { javascript } from "@codemirror/lang-javascript";
import { json } from "@codemirror/lang-json";
import { onMounted, ref } from "vue";
import { cpp } from "@codemirror/lang-cpp";

const editorRef = ref(), editorView = ref<EditorView>();

interface CodeMirrorProps {
  lang: string;
  width?: string | number;
  height?: string | number;
}

const props = withDefaults(defineProps<CodeMirrorProps>(), {
  lang: "json"
});

// Compartment用于切换编辑器的语言配置
const language = new Compartment, tabSize = new Compartment;

/**
 * https://codemirror.net/examples/config/
 * https://juejin.cn/post/7275973778914459660
 */
const initEditor = () => {
  if (typeof editorView.value !== "undefined") {
    editorView.value.destroy();
  }
  const jsonString = "hello world";
  const state: EditorState = EditorState.create({
    doc: jsonString,
    // // 注意这里就需要以这种方式引入动态配置
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
  },
  setText(val: string) {
    editorView.value?.dispatch({
      changes: {
        from: 0,
        to: editorView.value?.state.doc.length,
        insert: val
      }
    });
  },
  getText() {
    return editorView.value?.state.doc.toString();
  },
  setLanguage(lang: string) {
    editorView.value?.dispatch({
      changes: [
        {
          from: 0,
          to: editorView.value.state.doc.length,
          insert: ""
        }
      ],
      effects: language.reconfigure(lang == "js" ? javascript() : cpp())
    });
  }
});

</script>

<style lang="scss" scoped>
.editor-box {
  width: 100vw;
  height: 100vh;
}

.CodeMirror {
  font-family: "Courier New", monospace;
  font-size: 14px;
  line-height: 1.5;
}
</style>
