<!--
 * @ 文件生成类型弹窗
 * @author Von
 * @date 2023/7/20 21:08
-->
<script lang="ts" setup>
import {ref} from 'vue'
import {ElTable} from 'element-plus'
import {apiListSelectableTemplates} from "@/api/template";
import {apiListGenFiles} from "@/api/generator";

const dialogVisiableRef = ref(false)

interface GenFile {
    pid: number
    fileName: string,
    templateId?: number,
    remark: string
}

// 表格
const singleTableRef = ref<InstanceType<typeof ElTable>>()

// 默认的文件生成与模板对应关系
const tableData = ref()

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

defineExpose({
    init
})

let templateOptions = ref([])

function handleCurrentChange() {

}
</script>

<template>
    <el-dialog v-model="dialogVisiableRef" title="目标生成文件类型管理" draggable destroy-on-close
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
            <el-table-column property="fileName" label="文件类型"/>
            <el-table-column label="模板">
                <template #default="scope">
                    <div>
                        <el-select v-model="scope.row.templateId" class="m-2" placeholder="选择模板"
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
        </el-table>
        <template #footer>
            <el-button>新增</el-button>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>
