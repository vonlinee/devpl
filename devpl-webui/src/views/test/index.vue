<script setup lang="ts">

import CodeGenResult from "@/views/generator/CodeGenResult.vue";
import {ref} from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import FileUpload from "@/components/FileUpload.vue";

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

    <file-upload></file-upload>
</template>

<style scoped lang="scss">

</style>
