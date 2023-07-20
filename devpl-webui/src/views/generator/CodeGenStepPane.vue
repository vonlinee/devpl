<!--
 * @代码生成步骤面板
 * @author Von
 * @date 2023/7/20 17:20
-->
<script setup lang="ts">
import {ref} from "vue";
import GenFileTypeDialog from "@/views/generator/GenFileTypeDialog.vue";

const tabPosition = ref('left')

const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
]

const fileTypeManagerRef = ref()
function showFileTypeManagerDialog() {
    fileTypeManagerRef.value.init()
}

</script>

<template>
    <el-tabs :tab-position="tabPosition" class="demo-tabs">
        <el-tab-pane label="项目基本信息">

        </el-tab-pane>
        <el-tab-pane label="生成文件类型">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55"/>
                <el-table-column prop="date" label="文件类型" width="180"/>
                <el-table-column prop="name" label="模板" width="180"/>
                <el-table-column prop="address" label="说明"/>
            </el-table>
            <el-button type="primary">自定义文件类型</el-button>
            <el-button type="primary" @click="showFileTypeManagerDialog()">文件类型管理</el-button>
        </el-tab-pane>
        <el-tab-pane label="确认结果">

        </el-tab-pane>
    </el-tabs>

    <gen-file-type-dialog ref="fileTypeManagerRef"></gen-file-type-dialog>
</template>

<style scoped lang="scss">

</style>
