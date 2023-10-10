<template>
    <vxe-grid v-bind="gridOptions">
        <template #name_edit="{ row }">
            <vxe-input v-model="row.name"></vxe-input>
        </template>
        <template #nickname_edit="{ row }">
            <vxe-input v-model="row.nickname"></vxe-input>
        </template>
        <template #role_edit="{ row }">
            <vxe-input v-model="row.role"></vxe-input>
        </template>
        <template #address_edit="{ row }">
            <vxe-input v-model="row.address"></vxe-input>
        </template>
    </vxe-grid>
</template>
  
<script lang="ts" setup>
import { reactive } from 'vue'
import { VxeGridProps } from 'vxe-table'
import { VDTOptions } from './vxetable'

/**
 * 组件属性
 */
interface VxeDataTableProps  {
    options: VDTOptions
}


/**
 * vxe-table配置项
 * https://vxetable.cn/v4/#/table/grid/basic
 */
const gridOptions = reactive<VxeGridProps<RowVO>>({
    border: true,
    keepSource: true,
    height: 500,
    printConfig: {},
    importConfig: {},
    exportConfig: {},
    columnConfig: {
        resizable: true
    },
    pagerConfig: {
        enabled: true,
        perfect: true,
        pageSize: 15
    },
    editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true
    },
    toolbarConfig: {
        buttons: [
            { code: 'insert_actived', name: '新增', status: 'perfect', icon: 'vxe-icon-add' },
            { code: 'mark_cancel', name: '标记/取消', status: 'perfect', icon: 'vxe-icon-delete' },
            { code: 'save', name: '保存', status: 'perfect', icon: 'vxe-icon-save' }
        ],
        perfect: true,
        refresh: {
            icon: 'vxe-icon-refresh',
            iconLoading: 'vxe-icon-refresh roll'
        },
        import: {
            icon: 'vxe-icon-cloud-upload'
        },
        export: {
            icon: 'vxe-icon-cloud-download'
        },
        print: {
            icon: 'vxe-icon-print'
        },
        zoom: {
            iconIn: 'vxe-icon-fullscreen',
            iconOut: 'vxe-icon-minimize'
        },
        custom: {
            icon: 'vxe-icon-menu'
        }
    },
    proxyConfig: {
        props: {
            result: 'result',
            total: 'page.total'
        },
        ajax: {
            // 接收 Promise
            query: ({ page }) => {
                return fetchApi(page.currentPage, page.pageSize)
            },
            // body 对象： { removeRecords }
            delete: ({ body }) => {
                return delApi(body.removeRecords)
            },
            // body 对象： { insertRecords, updateRecords, removeRecords, pendingRecords }
            save: ({ body }) => {
                return saveApi(body.insertRecords)
            }
        }
    },
    columns: options.columns
})

interface RowVO {
    id: number
    name: string
    nickname: string
    role: string
    sex: string
    age: number
    address: string
}

// 模拟后台接口
const fetchApi = (currentPage: number, pageSize: number) => {
    return new Promise(resolve => {
        setTimeout(() => {
            const list = [
                { id: 10001, name: 'Test1', nickname: 'T1', role: 'Develop', sex: 'Man', age: 28, address: 'Shenzhen' },
                { id: 10002, name: 'Test2', nickname: 'T2', role: 'Test', sex: 'Women', age: 22, address: 'Guangzhou' },
                { id: 10003, name: 'Test3', nickname: 'T3', role: 'PM', sex: 'Man', age: 32, address: 'Shanghai' },
                { id: 10004, name: 'Test4', nickname: 'T4', role: 'Designer', sex: 'Women', age: 23, address: 'Shenzhen' },
                { id: 10005, name: 'Test5', nickname: 'T5', role: 'Develop', sex: 'Women', age: 30, address: 'Shanghai' },
                { id: 10006, name: 'Test6', nickname: 'T6', role: 'Designer', sex: 'Women', age: 21, address: 'Shenzhen' },
                { id: 10007, name: 'Test7', nickname: 'T7', role: 'Test', sex: 'Man', age: 29, address: 'test abc' },
                { id: 10008, name: 'Test8', nickname: 'T8', role: 'Develop', sex: 'Man', age: 35, address: 'Shenzhen' },
                { id: 10009, name: 'Test9', nickname: 'T9', role: 'Develop', sex: 'Man', age: 35, address: 'Shenzhen' },
                { id: 100010, name: 'Test10', nickname: 'T10', role: 'Develop', sex: 'Man', age: 35, address: 'Guangzhou' },
                { id: 100011, name: 'Test11', nickname: 'T11', role: 'Test', sex: 'Women', age: 26, address: 'test abc' },
                { id: 100012, name: 'Test12', nickname: 'T12', role: 'Develop', sex: 'Man', age: 34, address: 'Guangzhou' },
                { id: 100013, name: 'Test13', nickname: 'T13', role: 'Test', sex: 'Women', age: 22, address: 'Shenzhen' }
            ]
            resolve({
                page: {
                    total: list.length
                },
                result: list.slice((currentPage - 1) * pageSize, currentPage * pageSize)
            })
        }, 100)
    })
}

// 模拟后台接口
const delApi = (removeRecords: RowVO[]) => {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                result: [],
                msg: `delete，${removeRecords.length}条`
            })
        }, 100)
    })
}

// 模拟后台接口
const saveApi = (insertRecords: RowVO[]) => {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                result: [],
                msg: `succcess, ${insertRecords.length}条`
            })
        }, 100)
    })
}

</script>
  