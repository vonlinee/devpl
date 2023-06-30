<script setup lang="ts">

import FileTreeView from "@/components/FileTreeView.vue";
import {ref} from "vue";

let dialogRef = ref()
</script>

<template>
    <el-button @click="dialogRef = true"></el-button>
    <el-dialog v-model="dialogRef" draggable>
        <file-tree-view></file-tree-view>
    </el-dialog>

</template>

<style scoped lang="scss">

</style>
