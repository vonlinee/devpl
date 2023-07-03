<script setup lang="ts">
import * as monaco from 'monaco-editor'
import {editor} from 'monaco-editor'
import {onMounted, ref} from 'vue'
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;

// 编辑器实例
let monacoEditor: monaco.editor.IStandaloneCodeEditor | undefined = undefined

/**
 * 组件暴露的属性
 */
interface EditorProps {
    value: string
    language: 'javascript' | 'typescript' | 'css' | 'json' | 'xml' | 'java' | 'freemarker' | 'velocity' | 'sql'
    editorOptions?: object
}

const props = defineProps<EditorProps>()

let editorBoxRef = ref()

let content = ref()

const editorOptions: IStandaloneEditorConstructionOptions = {
    // 编辑器初始显示文字
    value: props.value || '',
    // 语言支持
    language: props.language,
    minimap: {enabled: false},
    fontSize: 15,
    // 自适应布局
    automaticLayout: true,
    theme: 'vs',
    foldingStrategy: 'indentation',
    renderLineHighlight: 'all', // 行亮
    selectOnLineNumbers: true, // 显示行号
    readOnly: false, // 只读
    scrollBeyondLastLine: false, // 取消代码后面一大段空白
    overviewRulerBorder: false, // 不要滚动条的边框
    ...props.editorOptions
}

onMounted(() => {
    if (!monacoEditor) {
        monacoEditor = createMonacoEditor(editorBoxRef.value)
    }
})

function createMonacoEditor(domElement: HTMLElement): IStandaloneCodeEditor {
    return monaco.editor.create(domElement!, editorOptions)
}

defineExpose({
    getText: function () {
        return monacoEditor?.getValue()
    },
    setText: function (text: string) {
        monacoEditor?.setValue(text)
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
