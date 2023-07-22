<!--
 * @代码生成步骤面板
 * @author Von
 * @date 2023/7/20 17:20
-->
<script setup lang="ts">
import {onMounted, ref} from "vue";
import GenFileTypeDialog from "@/views/generator/GenFileTypeDialog.vue";
import {apiListGenFiles} from "@/api/generator";
import SvgIcon from "@/components/svg-icon/src/svg-icon.vue";

const tabPosition = ref('left')

const tableData = ref([])

onMounted(() => {
    apiListGenFiles().then((res) => {
        tableData.value = res.data
    })
})

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
            <el-table :data="tableData" style="width: 100%" height="600px">
                <el-table-column type="selection" width="55"/>
                <el-table-column prop="fileName" label="文件类型"/>
                <el-table-column prop="templateName" label="模板">
                    <template #default="scope">
                        <div>
                            <el-text>{{scope.row.templateName}}</el-text>
                            <svg-icon icon="icon-edit"></svg-icon>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="说明"/>
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
