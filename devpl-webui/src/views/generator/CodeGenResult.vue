<template>
    <el-dialog v-model="dialogVisiableRef" title="生成结果" draggable width="80%" :close-on-click-modal="false"
               top="20px" append-to-body>
        <el-collapse v-model="activeNames" accordion>
            <el-collapse-item v-for="(item, index) in rootDirsRef" :title="item" :name="index">
                <component :is="CodeTreeView" style="height: 600px" :dir="item"></component>
            </el-collapse-item>
        </el-collapse>
    </el-dialog>
</template>

<script lang="ts" setup>
import {nextTick, ref} from 'vue'
import CodeTreeView from "@/views/generator/CodeTreeView.vue";
import {ElDialog} from "element-plus";

const dialogVisiableRef = ref()

// 根目录列表
const rootDirsRef = ref<string[]>()
const activeNames = ref<string[]>(['0'])

/**
 * 初始化
 * @param dirs 需要展示的文件夹
 */
function init(dirs: string[]) {
    dialogVisiableRef.value = true
    rootDirsRef.value = dirs
    if (dirs.length > 0) {
        nextTick(() => activeNames.value.push('1')) // 默认展开第一个
    }
}

defineExpose({
    init
})
</script>
