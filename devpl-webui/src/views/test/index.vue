<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";
import TestDialog from "@/components/dialog/TestDialog.vue";

let dialogRef = ref()
let dialogRef1 = ref()
let dialogVisiable = ref()

function show() {
    dialogRef.value.init()
}

function show1() {
    dialogVisiable.value = true
}

</script>

<template>
    <el-button @click="show">打开弹窗</el-button>
    <el-button @click="show1">打开弹窗1</el-button>

    <adaptive-dialog ref="dialogRef"></adaptive-dialog>

    <el-dialog v-model="dialogVisiable" draggable>
        <div style="width: 1200px; height: 1000px"></div>
    </el-dialog>

    <test-dialog></test-dialog>
</template>

<style lang="scss">

</style>
