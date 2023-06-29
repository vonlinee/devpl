<template>
	<el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
		<el-tab-pane label="JSON" name="first">
			<div>
				<monaco-editor language="json" value=""></monaco-editor>
			</div>
		</el-tab-pane>
		<el-tab-pane label="XML" name="second">Config</el-tab-pane>
		<el-tab-pane label="JSON Schema" name="third">Role</el-tab-pane>
		<el-tab-pane label="DDL" name="fourth">Task</el-tab-pane>
	</el-tabs>
</template>
<script lang="ts" setup>
import {ref} from 'vue'
import type {TabsPaneContext} from 'element-plus'
import MonacoEditor from "@/components/MonacoEditor.vue";

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
</style>