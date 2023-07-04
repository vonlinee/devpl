<script setup lang="ts">

import CodeGenResult from "@/views/generator/CodeGenResult.vue";
import {ref} from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

let dirs = ref(['D:/Temp/2222', 'D:/Temp/menu'])

let editorRef = ref()

function show() {
    editorRef.value.setLanguage()
}

let langRef = ref(`javascript`)

</script>

<template>
<!--    <code-gen-result :dirs="dirs"></code-gen-result>-->
    <button @click="show">Change</button>
    <el-input v-model="langRef"></el-input>
    <monaco-editor ref="editorRef" language="json" style="height: 500px"></monaco-editor>

    {{ langRef}}
</template>

<style scoped lang="scss">

</style>
