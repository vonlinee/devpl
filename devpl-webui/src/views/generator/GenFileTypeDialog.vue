<!--
 * @ 文件生成类型弹窗
 * @author Von
 * @date 2023/7/20 21:08
-->
<script lang="ts" setup>
import {ref} from 'vue'
import {ElTable} from 'element-plus'

const dialogVisiableRef = ref(false)

interface User {
    date: string
    name: string
    address: string
}

const currentRow = ref()
const singleTableRef = ref<InstanceType<typeof ElTable>>()

const setCurrent = (row?: User) => {
    singleTableRef.value!.setCurrentRow(row)
}
const handleCurrentChange = (val: User | undefined) => {
    currentRow.value = val
}
const tableData: User[] = [
    {
        date: 'Entity',
        name: 'Tom',
        address: '实体类文件',
    },
    {
        date: 'Mapper',
        name: 'Tom',
        address: 'Mapper文件',
    },
    {
        date: 'Mapper Xml',
        name: 'Tom',
        address: 'Mapper XML文件',
    },
    {
        date: 'Controller',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
]

function init() {
    dialogVisiableRef.value = true
}

const tableDataRef = ref(tableData)

function addNewRow() {
    tableDataRef.value.push({
        date: '',
        name: '',
        address: ''
    })
}

const value = ref('')

const options = [
    {
        value: 'Option1',
        label: 'Option1',
    },
    {
        value: 'Option2',
        label: 'Option2',
    },
    {
        value: 'Option3',
        label: 'Option3',
    },
    {
        value: 'Option4',
        label: 'Option4',
    },
    {
        value: 'Option5',
        label: 'Option5',
    },
]

defineExpose({
    init
})

</script>

<template>
    <el-dialog v-model="dialogVisiableRef" draggable destroy-on-close>
        <el-button @click="addNewRow">新增</el-button>
        <el-table
            ref="singleTableRef"
            :data="tableDataRef"
            highlight-current-row
            style="width: 100%"
            height="500px"
            @current-change="handleCurrentChange">
            <el-table-column type="index" width="50"/>
            <el-table-column property="date" label="文件类型" width="120"/>
            <el-table-column label="模板" width="120">
                <template #default="scope">
                    <div>
                        <el-select v-model="value" class="m-2" placeholder="Select" size="small" filterable>
                            <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            />
                        </el-select>
                    </div>
                </template>
            </el-table-column>
            <el-table-column property="address" label="描述信息"/>
        </el-table>
        <div style="margin-top: 20px">
            <el-button @click="setCurrent(tableDataRef[1])">Select second row</el-button>
            <el-button @click="setCurrent()">Clear selection</el-button>
        </div>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>
