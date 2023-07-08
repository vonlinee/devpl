<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";
import FileUpload from "@/components/upload/FileUpload.vue";

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

function handleResponse() {
    console.log("handleResponse")
}

let file = ref()

</script>

<template>
    <button @click="show">展示弹窗</button>
    <button @click="updateLanguage()">更新语言</button>
    <button @click="setLanguage()">设置语言</button>

    <el-input v-model="inputRef"></el-input>
    <adaptive-dialog ref="dialogRef"></adaptive-dialog>

    <file-upload accept="jpg"/>
</template>

<style scoped lang="scss">

</style>
