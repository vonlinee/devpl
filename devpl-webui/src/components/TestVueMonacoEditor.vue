<script lang="ts" setup>
import * as monaco from 'monaco-editor'
import {ref} from "vue";
import MonacoEditor from "@/components/MonacoEditor.vue";

const content = ref('{"text":"1"}')
const language = ref('json')
const editorMounted = (editor: monaco.editor.IStandaloneCodeEditor) => {
	console.log('editor实例加载完成', editor)
}
</script>
<template>
	<MonacoEditor
		v-model="content"
		:language="language"
		height="500px"
		@editor-mounted="editorMounted"
	></MonacoEditor>
</template>
