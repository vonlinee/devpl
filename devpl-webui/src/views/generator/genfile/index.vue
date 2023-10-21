<!--
 * @ 文件类型选择
 * @author Von
 * @date 2023/7/24 17:35
-->
<script setup lang="ts">
import SvgIcon from "@/components/svg-icon/src/svg-icon.vue";
import {onMounted, ref} from "vue";
import {apiListGenFiles} from "@/api/generator";
import GenFileTypeDialog from "@/views/generator/GenFileTypeDialog.vue";

const tableData = ref<GenFile[]>([])

onMounted(() => {
    apiListGenFiles().then((res) => {
        res.data.forEach((item: GenFile) => {
            if (item.builtin) {
                item.selected = true
            }
        })
        tableData.value = res.data
    })
})

const fileTypeManagerRef = ref()

function showFileTypeManagerDialog() {
    fileTypeManagerRef.value.init()
}

function testButton() {
    
}

</script>

<template>
    <el-table :data="tableData" style="width: 100%" height="600px" border>
        <el-table-column type="selection" width="55" prop="selected"/>
        <el-table-column prop="fileName" label="文件类型"/>
        <el-table-column prop="templateName" label="模板">
            <template #default="scope">
                <div>
                    <el-text>{{ scope.row.templateName }}</el-text>
                    <svg-icon icon="icon-edit"></svg-icon>
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="remark" label="说明"/>
    </el-table>
    <el-button type="primary" @click="showFileTypeManagerDialog()">文件类型管理</el-button>
    <el-button type="primary" @click="testButton()">1</el-button>

    <gen-file-type-dialog ref="fileTypeManagerRef"></gen-file-type-dialog>
</template>

<style scoped lang="scss">

</style>
