<template>
    <el-table :data="tableData" style="width: 100%" max-height="250">
        <el-table-column fixed prop="date" label="文件类型" width="150"/>
        <el-table-column prop="name" label="Name" width="120"/>
        <el-table-column prop="state" label="使用模板" width="120"/>
        <el-table-column prop="city" label="City" width="120"/>
        <el-table-column prop="address" label="Address" width="600"/>
        <el-table-column prop="zip" label="Zip" width="120"/>
        <el-table-column fixed="right" label="Operations" width="120">
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
    templateName: string
}

const now = new Date()

const tableData = ref([
    {
        date: '2016-05-01',
        name: 'Tom',
        state: 'California',
        city: 'Los Angeles',
        address: 'No. 189, Grove St, Los Angeles',
        zip: 'CA 90036',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        state: 'California',
        city: 'Los Angeles',
        address: 'No. 189, Grove St, Los Angeles',
        zip: 'CA 90036',
    },
    {
        date: '2016-05-03',
        name: 'Tom',
        state: 'California',
        city: 'Los Angeles',
        address: 'No. 189, Grove St, Los Angeles',
        zip: 'CA 90036',
    },
])

const deleteRow = (index: number) => {
    tableData.value.splice(index, 1)
}

const onAddItem = () => {
    now.setDate(now.getDate() + 1)
    tableData.value.push({
        date: dayjs(now).format('YYYY-MM-DD'),
        name: 'Tom',
        state: 'California',
        city: 'Los Angeles',
        address: 'No. 189, Grove St, Los Angeles',
        zip: 'CA 90036',
    })
}
</script>
