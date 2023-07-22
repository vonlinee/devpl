<!--
 * @ 文件生成类型弹窗
 * @author Von
 * @date 2023/7/20 21:08
-->
<script lang="ts" setup>
import {ref} from 'vue'
import {ElButton, ElTable} from 'element-plus'
import {apiListSelectableTemplates} from "@/api/template";
import {apiListGenFiles, apiSaveOrUpdateGenFiles} from "@/api/generator";
import {ElMessage} from "element-plus/es";

const dialogVisiableRef = ref(false)

// 表格
const singleTableRef = ref<InstanceType<typeof ElTable>>()

// 默认的文件生成与模板对应关系
const tableData = ref<GenFile[]>([])

/**
 * 获取表格数据
 */
function refreshTableData() {
    apiListGenFiles().then((res) => {
        tableData.value = res.data
    })
}

function init() {
    dialogVisiableRef.value = true
    templateOptions.value = []
    apiListSelectableTemplates().then(res => {
        templateOptions.value = res.data
    })
    refreshTableData()
}

/**
 * 新增一行
 */
function addNewFileType() {
    tableData.value.push({
        fileName: "文件类型名称",
        templateId: 1,
        remark: '',
        pid: 0,
        editing: true,
        builtin: false
    })
}

defineExpose({
    init
})

let templateOptions = ref([])

function handleCurrentChange() {

}

function submit() {

    apiSaveOrUpdateGenFiles(tableData.value).then((res) => {
        if (res.data) {
            ElMessage.info({
                message: "保存成功",
                onClose: () => {
                    dialogVisiableRef.value = false
                }
            })
        }
    })
}

function editHandle(row: GenFile) {
    row.editing = true
}

function saveHandle(row: GenFile) {
    row.editing = false
}

/**
 * 删除行
 * @param rowIndex
 */
function deleteHandle(rowIndex: number) {
    tableData.value.splice(rowIndex, 1)
}

</script>

<template>
    <el-dialog v-model="dialogVisiableRef" title="目标生成文件类型管理" draggable destroy-on-close
               :close-on-click-modal="false"
               :show-close="false"
               v-if="dialogVisiableRef">
        <el-table
            ref="singleTableRef"
            :data="tableData"
            table-layout="auto"
            highlight-current-row
            style="width: 100%"
            height="500px"
            @current-change="handleCurrentChange">
            <el-table-column type="index"/>
            <el-table-column property="fileName" label="文件类型">
                <template #default="scope">
                    <el-text v-if="!scope.row.editing">{{ scope.row.fileName }}</el-text>
                    <el-input v-if="scope.row.editing" v-model="scope.row.fileName"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="builtin" label="是否内置" type="selection"/>
            <el-table-column label="模板">
                <template #default="scope">
                    <div>
                        <el-text v-if="!scope.row.editing">{{ scope.row.templateName }}</el-text>
                        <el-select v-if="scope.row.editing" v-model="scope.row.templateId" class="m-2"
                                   placeholder="选择模板"
                                   filterable>
                            <el-option
                                v-for="item in templateOptions"
                                :key="item.templateId"
                                :label="item.templateName"
                                :value="item.templateId"/>
                        </el-select>
                    </div>
                </template>
            </el-table-column>
            <el-table-column property="remark" label="描述信息" width="240"/>

            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="250">
                <template #default="scope">
                    <el-button v-if="!scope.row.editing" type="primary" link @click="editHandle(scope.row)">编辑
                    </el-button>
                    <el-button v-if="scope.row.editing" type="primary" link @click="saveHandle(scope.row)">保存
                    </el-button>
                    <el-button type="primary" link @click="deleteHandle(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <template #footer>
            <el-button @click="refreshTableData()">刷新</el-button>
            <el-button @click="addNewFileType()">新增</el-button>
            <el-button @click="submit()">确认</el-button>
            <el-button @click="dialogVisiableRef = false">取消</el-button>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>
