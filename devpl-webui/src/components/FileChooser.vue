<!--
 * @ 单文件选择器
 * @author Von
 * @date 2023/8/6 15:28
-->
<script setup lang="ts">

import {defineComponent, ref} from "vue";

const inputRef = ref()
const text = ref()

/**
 * 打开文件选择弹窗
 */
async function showFileChooserDialog() {
    inputRef.value.click()
}

/**
 * 文件选择事件
 */
function onChange() {
    let file: File = inputRef.value.files[0]
    text.value = file.name
}

defineComponent({

})


</script>

<template>
    <el-input placeholder="请选择文件" v-model="text" type="text">
        <template #append>
            <el-button @click="showFileChooserDialog">浏览</el-button>
        </template>
    </el-input>
    <input ref="inputRef" type="file" style="display:none" @change="onChange" multiple="multiple"/>
</template>

<style scoped lang="scss">

</style>
