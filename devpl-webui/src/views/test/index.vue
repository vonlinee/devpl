<script setup lang="ts">
import SelectiveImport from '@/components/SelectiveImport.vue';
import { ref } from 'vue';

function showInfo() {
	let elementById = document.getElementById("editor-box");
	
	console.log(elementById)
}

let showRef = ref(false)

function showDialog() {
	showRef.value = true
}

</script>

<template>
	<div>
		<el-button @click="showDialog()">弹窗</el-button>
	<el-dialog v-model="showRef" draggable>
		<selective-import></selective-import>
	</el-dialog>
	</div>
</template>

<style scoped lang="scss">

</style>