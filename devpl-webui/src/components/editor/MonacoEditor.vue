<script lang="ts">
import * as monaco from 'monaco-editor'
import {editor} from 'monaco-editor'
import {defineComponent, h, onMounted, reactive, ref, toRefs} from 'vue'
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import ITextModel = editor.ITextModel;

// 可用的语言选项
const languageOptions = ref([
    "bat", "cpp", "css", "dockerfile", "go", "graphql", "html", "ini",
    "java", "javascript", "json", "julia", "kotlin", "less", "markdown", "mysql", "objective-c", "pascal", "pascaligo",
    "perl", "php", "powershell", "python", "r", "redis", "rust", "scala", "scheme", "scss", "shell",
    "sophia", "sql", "swift", "tcl", "typescript", "xml", "yaml"])

export default defineComponent({
    name: 'MonacoEditor',
    props: {
        // 初始宽度
        width: {
            type: String,
            default: '100%',
            required: false
        },
        // 初始高度
        height: {
            type: String,
            default: '100%',
            required: false
        },
        // 初始值
        text: {
            type: String,
            required: false,
        },
        // 初始语言
        language: {
            type: String,
            required: true,
        },
        options: {
            type: Object,
            required: false
        }
    },
    setup(props: any, context) {
        const {language, text, width, height} = toRefs(props)
        const editorOptions: IStandaloneEditorConstructionOptions = reactive({
            value: text.value, // 编辑器初始显示文字
            language: language.value, // 语言支持
            minimap: {enabled: false},
            fontSize: 15,
            automaticLayout: true, // 自适应布局
            theme: 'vs',
            foldingStrategy: 'indentation',
            renderLineHighlight: 'all', // 行亮
            selectOnLineNumbers: true, // 显示行号
            readOnly: false, // 只读
            roundedSelection: false,
            scrollBeyondLastLine: false, // 取消代码后面一大段空白
            overviewRulerBorder: false, // 不要滚动条的边框
        })

        /**
         * 创建编辑器节点
         * @param domElement
         */
        function createMonacoEditor(domElement: HTMLElement): IStandaloneCodeEditor {
            return monaco.editor.create(domElement!, editorOptions)
        }

        // 编辑器容器引用
        const editorBoxRef = ref()

        // 编辑器实例
        let monacoEditor: monaco.editor.IStandaloneCodeEditor | undefined = undefined
        onMounted(() => {
            if (!monacoEditor) {
                monacoEditor = createMonacoEditor(editorBoxRef.value)
            }
        })

        /**
         * 暴露API
         */
        context.expose({
            getText: function () {
                return monacoEditor?.getValue()
            },
            setText: function (text: string) {
                monacoEditor?.setValue(text)
            },
            /**
             * 设置语言模式 https://github.com/Microsoft/monaco-editor/issues/539
             * @param lang 如果为null，则会设置为plaintext
             */
            setLanguage(lang: string) {
                if (monacoEditor) {
                    const textModel: ITextModel | null = monacoEditor.getModel()
                    if (textModel) {
                        editorOptions.language = lang
                        monaco.editor.setModelLanguage(textModel, lang)
                    }
                }
            },
            getLanguage() {
                return editorOptions.language;
            }
        })
        return () => h("div", {
            ref: editorBoxRef,
            class: 'editor-container',
            style: {
                width: width.value,
                height: height.value,
                margin: 0,
                padding: 0,
                textAlign: 'left',
                display: 'inline-block'
            }
        }, [])
    }
});
</script>
