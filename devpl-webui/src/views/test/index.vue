<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";

let dialogRef = ref()



const editorRef = ref()
const languageRef = ref()

const inputRef = ref()

function updateLanguage() {
    languageRef.value = editorRef.value.getLanguage()
}

const dialogVisible = ref(false)

function show() {
    // dialogRef.value.init()
    dialogVisible.value = true
}

function setLanguage() {
    editorRef.value.setLanguage(inputRef.value)
}
let file = ref()

</script>

<template>
    <button @click="show">展示弹窗</button>
    <button @click="updateLanguage()">更新语言</button>
    <button @click="setLanguage()">设置语言</button>

    <el-input v-model="inputRef"></el-input>
    <adaptive-dialog ref="dialogRef"></adaptive-dialog>
</template>

<style lang="scss">

</style>
