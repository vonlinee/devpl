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
    language: 'javascript' | 'typescript' | 'css' | 'json'
    editorOptions: {
        type: Object
        required: false
    }
    id: string
}

const props = defineProps<EditorProps>()

let editorBoxRef = ref()

let content = ref()

const editorOptions: IStandaloneEditorConstructionOptions = {
    //编辑器初始显示代码
    value: props.value || '',
    language: props.language,
    minimap: {enabled: false},
    fontSize: 15,
    automaticLayout: true, // 自动布局
    theme: 'vs',
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
}
</style>
