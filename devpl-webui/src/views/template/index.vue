<template>
    <el-card>
        <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
            <el-form-item>
                <el-input v-model="state.queryForm.projectName" placeholder="项目名"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
                  @selection-change="selectionChangeHandle">
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column prop="templateId" label="模板ID" header-align="center" align="center"></el-table-column>
            <el-table-column prop="templateName" label="模板名称" header-align="center"
                             align="center"></el-table-column>
            <el-table-column prop="templatePath" label="模板路径" show-overflow-tooltip header-align="center"
                             align="center"></el-table-column>
            <el-table-column prop="content" label="模板内容" header-align="center"
                             align="center">
                <template #default="scope">
                    <el-text truncated>{{scope.row.content}}</el-text>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" show-overflow-tooltip header-align="center"
                             align="center"></el-table-column>
            <el-table-column prop="updateTime" label="更新时间" show-overflow-tooltip header-align="center"
                             align="center"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
                <template #default="scope">
                    <el-button type="primary" link @click="showTemplateEditDialog(scope.row)">模板</el-button>
                    <el-button type="primary" link @click="addOrUpdateHandle(scope.row.templateId)">修改</el-button>
                    <el-button type="primary" link @click="deleteBatchHandle(scope.row.templateId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            :current-page="state.page"
            :page-sizes="state.pageSizes"
            :page-size="state.limit"
            :total="state.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
        >
        </el-pagination>

        <el-dialog v-model="templateDialogShowing">
            <monaco-editor ref="templateContentEditorRef" language="freemarker2" height="600px"></monaco-editor>
        </el-dialog>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>
    </el-card>
</template>

<script lang="ts" setup>

import {onMounted, reactive, ref} from 'vue'
import {ElButton} from "element-plus";
import AddOrUpdate from './add-or-update.vue'
import {useCrud} from '@/hooks'
import {IHooksOptions} from '@/hooks/interface'
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

const state: IHooksOptions = reactive({
    dataListUrl: '/api/template/page',
    deleteUrl: '/api/template/delete/batch/ids',
    queryForm: {
        projectName: ''
    },
    primaryKey: 'templateId',
    isPage: true,
})

const {getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle} = useCrud(state)

const addOrUpdateRef = ref()
const addOrUpdateHandle = (id?: number) => {
    addOrUpdateRef.value.init(id)
}

const templateDialogShowing = ref(false)
const templateContentEditorRef = ref()

function showTemplateEditDialog(templateInfo: any) {
    templateDialogShowing.value = true
    if (templateInfo.type == 1) {
        // 获取文件内容
    } else {
        // 字符串模板
        templateContentEditorRef.value.setText(templateInfo.content)
    }
}

onMounted(() => getDataList())
</script>
