<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";

let dialogRef = ref()

function show() {
    dialogRef.value.init()
}
</script>

<template>
    <button @click="show">Show</button>
    <adaptive-dialog ref="dialogRef"></adaptive-dialog>
</template>

<style scoped lang="scss">

</style>
