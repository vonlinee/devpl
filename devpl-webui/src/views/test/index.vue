<script setup lang="ts">
import { ref } from 'vue'
import FileSelector from '@/components/FileSelector.vue'
import App1 from '@/views/test/App1.vue'
import App3 from '@/views/test/App3.vue'
import TestMyTable from './TestMyTable.vue'
import VxeDataTable from '@/components/datatable/VxeDataTable.vue'
import { VDTColumnOptions } from '@/components/datatable/type'
let file = ref()

let fileSelectorRef = ref()

function click() {
	let files = fileSelectorRef.value.getFiles()
	console.log(files)
}

let columns: VDTColumnOptions[] = [
        { type: 'checkbox', width: 50 },
        { type: 'seq', width: 60 },
        { field: 'name', title: 'Name', editRender: { autofocus: '.vxe-input--inner' }, slots: { edit: 'name_edit' } },
        { field: 'nickname', title: 'Nickname', editRender: {}, slots: { edit: 'nickname_edit' } },
        { field: 'role', title: 'Role', editRender: {}, slots: { edit: 'role_edit' } },
        { field: 'address', title: 'Address', showOverflow: true, editRender: {}, slots: { edit: 'address_edit' } }
    ]

let options = {
	columns: columns
}

</script>

<template>
	<app3></app3>

	<app1></app1>

	<file-selector ref="fileSelectorRef"></file-selector>
	<el-button @click="click">显示</el-button>

	<vxe-data-table :options="options"></vxe-data-table>
</template>

<style lang="scss"></style>
