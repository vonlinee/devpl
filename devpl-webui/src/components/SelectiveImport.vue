<template>
	<el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
		<el-tab-pane label="JSON" name="first">
			<div class="code-editor-box">
				<monaco-editor language="json" value=""></monaco-editor>
			</div>
		</el-tab-pane>
		<el-tab-pane label="XML" name="second">
			<div class="code-editor-box">
				<monaco-editor language="xml" value=""></monaco-editor></div
		></el-tab-pane>
		<el-tab-pane label="JSON Schema" name="third">
			<div class="code-editor-box">
				<monaco-editor language="json" value=""></monaco-editor></div
		></el-tab-pane>
		<el-tab-pane label="DDL" name="fourth">
			<div class="code-editor-box">
				<monaco-editor language="sql" value=""></monaco-editor></div
		></el-tab-pane>
	</el-tabs>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
	console.log(tab, event)
}
</script>
<style>
.demo-tabs > .el-tabs__content {
	padding: 32px;
	color: #6b778c;
	font-size: 32px;
	font-weight: 600;
}

.code-editor-box {
	height: 400px;
}
</style>