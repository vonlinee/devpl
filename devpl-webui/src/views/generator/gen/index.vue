<template>
    <el-table border :data="tableData" height='400px'>
        <el-table-column type="selection" width="35" />
        <el-table-column prop="filename" label="文件名">
            <template #default="scope">
                <span>{{ scope.row.filename }}</span>
            </template>
        </el-table-column>
        <el-table-column prop="templateId" label="模板">
            <template #default="scope">
                <template-selector :templates="templateOptions"
                    @handle-preview="(row) => handlePreview(row.templateId)"></template-selector>
            </template>
        </el-table-column>
        <el-table-column prop="fillStrategy" label="数据填充方式">
            <template #default="scope">
                <el-select v-model="scope.row.dataFillStrategy">
                    <el-option label="数据库" value="db"></el-option>
                    <el-option label="自定义脚本" value="script"></el-option>
                </el-select>
            </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
            <template #default="scope">
                <el-button :link="true">填充</el-button>
                <el-button :link="true" @click="templateParamTableRef.show()">模板参数</el-button>
                <el-button :link="true">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-button class="mt-4" style="width: 100%" @click="onAddItem">新增</el-button>
    <template-viewer ref="templatePreviewRef"></template-viewer>

    <simple-modal ref="templateParamTableRef" title="模板参数表">
        <template-data-table></template-data-table>
    </simple-modal>
</template>
  
<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import TemplateSelector from './TemplateSelector.vue';
import SimpleModal from '@/components/dialog/SimpleModal.vue';
import TemplateDataTable from './TemplateDataTable.vue';
import { ElOption } from 'element-plus';
import { apiGetTemplateById, apiListSelectableTemplates } from '@/api/template';
import TemplateViewer from '@/views/template/TemplateViewer.vue';

const templateParamTableRef = ref()
const templatePreviewRef = ref()
const templateOptions = ref<TemplateInfo[]>()

function handlePreview(templateId: number) {
    apiGetTemplateById(templateId).then((res) => {
        templatePreviewRef.value.init(res.data?.templateName, res.data?.content)
    })
}

onMounted(() => {
    apiListSelectableTemplates().then((res) => {
        templateOptions.value = res.data
    })
})


const tableData = ref<TargetGenFileItem[]>([

])


const onAddItem = () => {
    tableData.value.push({
        filename: "1.txt",
        templateId: 1,
        dataFillStrategy: "db"
    })
}

</script>
  