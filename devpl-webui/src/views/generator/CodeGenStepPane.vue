<!--
 * @代码生成步骤面板
-->
<script setup lang="ts">
import {ref} from "vue";
import CodeGenInfo from "@/views/generator/CodeGenInfo.vue";
import DataSourceSelector from "@/views/generator/DataSourceSelector.vue";

const tabPosition = ref('left')

let activeName = ref<string>('0')

let index = 0;
let tabDisableFlag = ref<boolean[]>([false, true, true, true])

/**
 * 更新标签栏的禁用状态
 * @param next
 */
function updateTabDisableStatus(next: boolean) {
    index = index + (next ? 1 : -1)
    if (index == 4) {
        index = 3
    }
    if (index == -1) {
        index = 0
    }
    for (let i = 0; i < tabDisableFlag.value.length; i++) {
        tabDisableFlag.value[i] = i != index;
    }
    // 选中对应的标签
    activeName.value = (index + 1).toString()
}

</script>

<template>
    <div style="height: 800px">
        <el-tabs v-model="activeName" :tab-position="tabPosition" class="demo-tabs" active>
            <el-tab-pane label="项目基本信息" :disabled="tabDisableFlag[0]" name="1">
                <code-gen-info v-if="activeName == '1'"></code-gen-info>
            </el-tab-pane>
            <el-tab-pane label="选择文件类型" :disabled="tabDisableFlag[1]" name="2">
                <el-breadcrumb v-if="activeName == '2'"></el-breadcrumb>
            </el-tab-pane>
            <el-tab-pane label="模板数据源" :disabled="tabDisableFlag[2]" name="3">
                <data-source-selector v-if="activeName == '3'"></data-source-selector>
            </el-tab-pane>
            <el-tab-pane label="确认结果" :disabled="tabDisableFlag[3]" name="4">
                <el-button v-if="activeName == '4'"></el-button>
            </el-tab-pane>
        </el-tabs>
    </div>
    <el-button-group>
        <el-button @click="updateTabDisableStatus(false)">上一步</el-button>
        <el-button @click="updateTabDisableStatus(true)">下一步</el-button>
    </el-button-group>
</template>

<style scoped lang="scss">

</style>
