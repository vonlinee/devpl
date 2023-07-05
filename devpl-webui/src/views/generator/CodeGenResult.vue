<template>
    <el-dialog v-model="dialogVisiableRef" title="代码生成结果" draggable>
        <el-collapse v-model="activeName" accordion>
            <el-collapse-item v-for="(item, index) in rootDirsRef" :title="item">
                <component :is="CodeTreeView" style="height: 400px" :dir="item"></component>
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

/**
 * 初始化
 * @param dirs 需要展示的文件夹
 */
function init(dirs: string[]) {
    dialogVisiableRef.value = true
    rootDirsRef.value = dirs
}

defineExpose({
    init
})

const activeName = ref('1')
</script>+
