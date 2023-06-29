<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

function showInfo() {
	let elementById = document.getElementById("editor-box");
	
	console.log(elementById)
}
</script>

<template>
	<div class="monaco" style="height: 500px">
		<monaco-editor value="111" language="css" ref="monacoRef" editor-options="{}"/>
		<monaco-editor value="111" language="css" ref="monacoRef" editor-options="{}"/>
		<button @click="showInfo()">11</button>
	</div>
</template>

<style scoped lang="scss">

</style>