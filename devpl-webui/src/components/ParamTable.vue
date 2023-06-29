<template>
	<vxe-toolbar ref="toolbarRef">
		<template #buttons>
			<vxe-button content="下拉按钮">
				<template #dropdowns>
					<vxe-button content="JSON"  @click="val1 = true"></vxe-button>
					<vxe-button content="JSON Schema"></vxe-button>
					<vxe-button content="按钮3"></vxe-button>
				</template>
			</vxe-button>
		</template>
	</vxe-toolbar>
	<vxe-modal v-model="val1" width="600" show-footer>
		<template #title>
			<span >自定义标题</span>
		</template>
		<template #default>
			<vxe-textarea rows="64" resize="none"></vxe-textarea>
		</template>
	</vxe-modal>
	
	<vxe-table
		show-overflow
		ref="msParamTable"
		border
		row-key
		:data="mapperParams"
		:checkbox-config="{ checkStrictly: true }"
		:tree-config="{transform: true}"
		:edit-config="editConfig"
	>
		<vxe-column field="name" title="参数名" tree-node></vxe-column>
		<vxe-column field="value" title="参数值" :edit-render="{name: 'input'}"></vxe-column>
		<vxe-column field="type" title="类型" :edit-render="{}">
			<template #default="{ row }">
				<span>{{ row.type }}</span>
			</template>
			<template #edit="{ row }">
				<vxe-select v-model="row.type" clearable transfer>
					<vxe-option v-for="item in dataTypes" :value="item.value" :label="item.name"></vxe-option>
				</vxe-select>
			</template>
		</vxe-column>
	</vxe-table>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {apiGetDataTypes} from '@/api/mybatis'

const val1 = ref(false)
const tableData = ref([])

interface DataType {
	name: string; // 数据类型名称，界面显示的值
	value: string; // 类型值
}

// 表格实例
let msParamTable = ref()

let mapperParams = ref([{
	id: 1,
	parentId: null,
	name: 'param',
	value: null,
	type: null,
	leaf: false
}, {
	id: 11,
	parentId: 1,
	name: 'userId',
	value: '102120',
	type: 'String',
	leaf: true
}])

let dataTypes = ref<DataType[]>([
	{name: 'String', value: 'String'}
])

// 编辑配置
const editConfig = reactive({
	trigger: 'click',
	mode: 'cell',
	beforeEditMethod: (row: any, rowIndex: number, column: any, columnIndex: number) => {
		// 只有叶子结点可编辑
		return row.row.leaf != null && row.row.leaf
	}
})

onMounted(() => {
	apiGetDataTypes().then((res) => {
		dataTypes.value = res.data
	}).then(() => expandAll())
})

/**
 * 展开所有节点
 */
const expandAll = () => {
	console.log("展开所有节点")
	const $table = msParamTable.value
	if ($table) {
		$table.setAllTreeExpand(true)
	}
}
</script>

<style lang="scss">

</style>
