<template>
    <div class="demo-collapse">
        <el-collapse v-model="activeName" accordion>
            <el-collapse-item v-for="(item, index) in dirs" :title="item">
                <component :is="CodeTreeView" style="height: 400px" :dir="item"></component>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import CodeTreeView from "@/views/generator/CodeTreeView.vue";

interface PropOfThis {
    // 根目录列表
    dirs: string[]
}

defineProps<PropOfThis>()

const activeName = ref('1')
</script>
