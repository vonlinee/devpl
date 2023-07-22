<template>
    <el-dialog v-model="dialogVisiableRef" title="代码生成结果" draggable width="80%">
        <el-collapse v-model="activeName" accordion>
            <el-collapse-item v-for="(item, index) in rootDirsRef" :title="item">
                <component :is="CodeTreeView" style="height: 600px" :dir="item"></component>
            </el-collapse-item>
        </el-collapse>
    </el-dialog>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import CodeTreeView from "@/views/generator/CodeTreeView.vue";
import {ElDialog} from "element-plus";

const dialogVisiableRef = ref(false)

// 根目录列表
const rootDirsRef = ref<string[]>()
const activeName = ref<string | null>(null)

/**
 * 初始化
 * @param dirs 需要展示的文件夹
 */
function init(dirs: string[]) {
    dialogVisiableRef.value = true
    rootDirsRef.value = dirs
    activeName.value = null
}

defineExpose({
    init
})
</script>
