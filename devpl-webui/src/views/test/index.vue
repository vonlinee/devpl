<script setup lang="ts">
import { ref } from 'vue'
import FileSelector from '@/components/FileSelector.vue'
import App1 from '@/views/test/App1.vue'
import App2 from '@/views/test/App2.vue'
import App3 from '@/views/test/App3.vue'
import CodeRunner from "@/views/test/CodeRunner.vue";

let file = ref()

let fileSelectorRef = ref()

function click() {
	let files = fileSelectorRef.value.getFiles()
	console.log(files)
}
</script>

<template>
	<app3></app3>

	<app1></app1>

	<file-selector ref="fileSelectorRef"></file-selector>
	<el-button @click="click">显示</el-button>

  <code-runner></code-runner>
</template>

<style lang="scss"></style>
