<!-- 
	封装monaco-editor编辑器
 -->
<script lang="ts">
import * as monaco from "monaco-editor";
import { editor } from "monaco-editor";
import { defineComponent, h, onMounted, reactive, ref, toRefs } from "vue";
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;
import ITextModel = editor.ITextModel;

/**
 * 必须给高度才能显示
 */
export default defineComponent({
  name: "MonacoEditor",
  props: {
    // 初始宽度
    width: {
      type: String,
      default: "100%",
      required: false
    },
    // 初始高度
    height: {
      type: String,
      default: "100%",
      required: false
    },
    // 初始值
    text: {
      type: String,
      required: false,
      default: ""
    },
    // 初始语言
    language: {
      type: String,
      required: true
    },
    options: {
      type: Object,
      required: false,
      default: () => {
        return {};
      }
    },
    minimap: {
      type: Boolean,
      default: false,
      required: false
    },
    // 是否只读,不可通过界面输入，但是可以通过API设置文本
    readOnly: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  setup(props: any, context) {
    const { language, text, width, height, readOnly, minimap } = toRefs(props);
    // 初始高度
    let initialHeight: string = height.value;

    if (language.value == "json") {
      monaco.languages.json.jsonDefaults.setDiagnosticsOptions({
        validate: true,
        allowComments: true, // 允许JSON注释，json5
        schemaValidation: "error"
      });
    }

    const editorOptions: IStandaloneEditorConstructionOptions = reactive({
      value: text.value, // 编辑器初始显示文字
      language: language.value, // 语言支持
      minimap: {
        enabled: minimap // 关闭编辑区域右侧小地图
      },
      lineNumbers: "on",
      lineNumbersMinChars: 3,
      fontSize: 15,
      automaticLayout: true, // 自适应布局
      theme: "vs",
      foldingStrategy: "indentation",
      renderLineHighlight: "all", // 行亮
      selectOnLineNumbers: true, // 显示行号
      readOnly: readOnly.value, // 只读
      roundedSelection: false,
      scrollBeyondLastLine: false, // 取消代码后面一大段空白
      overviewRulerBorder: false // 不要滚动条的边框
    });

    // 编辑器容器节点
    const editorBoxRef = ref<HTMLDivElement>();

    // 编辑器实例
    let monacoEditor: monaco.editor.IStandaloneCodeEditor;
    onMounted(() => {
      if (!monacoEditor && editorBoxRef.value) {
        monacoEditor = monaco.editor.create(editorBoxRef.value, editorOptions);
        // 中文拼音输入法时也会触发onDidChangeContent事件，还未输入文本
        // monacoEditor.getModel()?.onDidChangeContent((event) => {
        //
        // });
      }
    });

    /**
     * 暴露组件API
     */
    context.expose({
      /**
       * 获取编辑器的文本
       */
      getText: function(): string {
        if (monacoEditor) {
          return monacoEditor.getValue();
        }
        return "";
      },
      /**
       * 设置编辑器的文本
       * @param text
       */
      setText: function(text: string) {
        monacoEditor?.setValue(text);
      },
      /**
       * 设置语言模式 https://github.com/Microsoft/monaco-editor/issues/539
       * @param lang 如果为null，则会设置为plaintext
       */
      setLanguage: function(lang: string): void {
        if (monacoEditor) {
          const textModel: ITextModel | null = monacoEditor.getModel();
          if (textModel) {
            editorOptions.language = lang;
            monaco.editor.setModelLanguage(textModel, lang);
          }
        }
      },
      /**
       * 获取编辑器语言模式
       */
      getLanguage: function(): string | undefined {
        return editorOptions.language;
      }
    });
    return () =>
      h(
        "div",
        {
          ref: editorBoxRef,
          // monaco-editor容器div
          class: "monaco-editor-container",
          style: {
            width: width.value,
            height: initialHeight,
            margin: 0,
            padding: 0,
            textAlign: "left",
            display: "block"
          }
        },
        []
      );
  }
});
</script>
