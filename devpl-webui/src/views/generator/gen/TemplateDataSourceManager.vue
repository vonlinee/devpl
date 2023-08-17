<!--
 * @ 模板数据源选择
 某种文件类型
 * @author Von
 * @date 2023/8/4 11:35
-->
<script setup lang="ts">

import {ref} from "vue";

let dialogVisiable = ref()

function init() {
    dialogVisiable.value = true
}

defineExpose({
    init
})

const value = ref('')
const options = [
    {
        value: 'db',
        label: '数据库',
    },
    {
        value: 'custom',
        label: '自定义',
    }
]

const dataSourceProvider = ref([
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

</script>

<template>
    <el-dialog v-model="dialogVisiable" title="生成目标">
        <el-select v-model="value" clearable placeholder="选择数据源">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"/>
        </el-select>

        <el-table :data="dataSourceProvider" height="600px">
            <el-table-column fixed prop="date" label="名称" width="150"/>
            <el-table-column prop="name" label="数据源类型" width="120"/>
            <el-table-column prop="city" label="数据填充脚本" width="120"/>
            <el-table-column prop="zip" label="Zip" width="120"/>
        </el-table>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>
