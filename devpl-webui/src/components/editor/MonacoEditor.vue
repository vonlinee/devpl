<script setup lang="ts">
import * as monaco from 'monaco-editor'
import {editor} from 'monaco-editor'
import {onMounted, reactive, ref} from 'vue'
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import ITextModel = editor.ITextModel;

// 编辑器实例
let monacoEditor: monaco.editor.IStandaloneCodeEditor | undefined = undefined

/**
 * 组件暴露的属性
 */
interface EditorProps {
    // 初始值
    value?: string
    // 初始语言
    language: 'javascript' | 'typescript' | 'css' | 'json' | 'xml' | 'java' | 'freemarker' | 'velocity' | 'sql'
    // 编辑器选项，如果为null则使用默认选项
    editorOptions?: object
}

const props = defineProps<EditorProps>()

let editorBoxRef = ref()

let content = ref()

const editorOptions: IStandaloneEditorConstructionOptions = reactive({
    value: props.value || '', // 编辑器初始显示文字
    language: props.language, // 语言支持
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
    ...props.editorOptions
})

onMounted(() => {
    if (!monacoEditor) {
        monacoEditor = createMonacoEditor(editorBoxRef.value)
    }
})

/**
 * 创建编辑器节点
 * @param domElement
 */
function createMonacoEditor(domElement: HTMLElement): IStandaloneCodeEditor {
    return monaco.editor.create(domElement!, editorOptions)
}

/**
 * 暴露API
 */
defineExpose({
    getText: function () {
        return monacoEditor?.getValue()
    },
    setText: function (text: string) {
        monacoEditor?.setValue(text)
    },
    /**
     * 设置语言模式 https://github.com/Microsoft/monaco-editor/issues/539
     * @param lang
     */
    setLanguage(lang: string) {
        if (monacoEditor) {
            const textModel: ITextModel | null = monacoEditor.getModel()
            if (textModel) {
                monaco.editor.setModelLanguage(textModel, lang)
            }
        }
    }
})

</script>

<template>
    <div id="editor-box" ref="editorBoxRef"></div>
</template>

<style scoped lang="scss">
#editor-box {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
    text-align: left;
    display: inline-block;
}
</style>
