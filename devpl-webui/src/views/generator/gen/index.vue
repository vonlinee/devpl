<template>
    <el-select>

    </el-select>

    <el-table :data="tableData" style="width: 100%">
        <el-table-column fixed prop="fileTypeName" label="文件类型名称" align="center" width="200"/>
        <el-table-column prop="templateName" label="模板" align="center"/>
        <el-table-column fixed="right" label="操作" align="center">
            <template #default="scope">
                <el-button
                    link type="primary" @click="openGenFileManager">数据
                </el-button>
                <el-button
                    link
                    type="primary"
                    @click.prevent="deleteRow(scope.$index)">删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-button class="mt-4" style="width: 100%" @click="onAddItem"
    >Add Item
    </el-button>

    <template-data-source-manager ref="templateDataSourceManagerRef"></template-data-source-manager>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import dayjs from 'dayjs'
import TemplateDataSourceManager from "@/views/generator/gen/TemplateDataSourceManager.vue";

let templateDataSourceManagerRef = ref()

function openGenFileManager() {
    templateDataSourceManagerRef.value.init()
}

/**
 * 目标生成文件
 */
interface TargetGenFile {
    taskId: string,
    templateId: number,
    templateName: string,
    fileTypeName: string
}

const now = new Date()

const tableData = ref<TargetGenFile[]>([])

const deleteRow = (index: number) => {
    tableData.value.splice(index, 1)
}

const onAddItem = () => {
    now.setDate(now.getDate() + 1)
    tableData.value.push({
        templateId: 1,
        templateName: "模板名称",
        fileTypeName: "文件类型名称",
        taskId: ""

    })
}
</script>
