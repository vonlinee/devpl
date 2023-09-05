<!--
 * @
 * @author Von
 * @date 2023/8/29 14:23
-->
<script setup lang="ts">
import MonacoEditor from '@/components/editor/MonacoEditor.vue'
import { ref } from 'vue'

import JSON5 from 'json5'

let editorRef = ref()

function parse() {
	let text = editorRef.value.getText()

	try {
		let result = JSON5.parse(text)
		console.log(result)
	} catch (e) {
		alert(e)
	}
}
</script>

<template>
	<monaco-editor ref="editorRef" language="json" height="400px"></monaco-editor>
	<el-button @click="parse">parse</el-button>
</template>

<style scoped lang="scss"></style>
