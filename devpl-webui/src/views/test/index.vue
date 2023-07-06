<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

let dialogRef = ref()

function show() {
    dialogRef.value.init()
}

const editorRef = ref()
const languageRef = ref()

const inputRef = ref()

function updateLanguage() {
    languageRef.value = editorRef.value.getLanguage()
}

function setLanguage() {
    editorRef.value.setLanguage(inputRef.value)
}

</script>

<template>
    <button @click="show">展示弹窗</button>
    <button @click="updateLanguage()">更新语言</button>
    <button @click="setLanguage()">设置语言</button>

    <el-input v-model="inputRef"></el-input>
    <adaptive-dialog ref="dialogRef"></adaptive-dialog>

    <monaco-editor ref="editorRef" text="helloworld" language="typescript" width="500px" height="300px"></monaco-editor>

    {{ languageRef }}
</template>

<style scoped lang="scss">

</style>
