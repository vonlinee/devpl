<template>
	<vxe-table height="auto" :min-height="height" :maxHeight="height" show-overflow ref="tableRef" :border="true" row-key
		:data="rows" :checkbox-config="{ checkStrictly: true }" :column-config="{
			resizable: true
		}" :row-config="{
	height: 40
}" :scroll-y="{ enabled: true }" :tree-config="{ transform: true }" :edit-config="editConfig">
		<vxe-column type="checkbox" title="#" width="45" header-align="center" align="center" :resizable="false"></vxe-column>
		<vxe-column field="name" title="名称" tree-node></vxe-column>
		<vxe-column field="value" title="值" :edit-render="{ name: 'input' }"></vxe-column>
		<vxe-column field="type" title="数据类型" :edit-render="{}">
			<template #default="{ row }">
				<span>{{ row.type }}</span>
			</template>
			<template #edit="{ row }">
				<vxe-select v-model="row.dataType" clearable transfer>
					<vxe-option v-for="item in dataTypes" :value="item.value" :label="item.name"></vxe-option>
				</vxe-select>
			</template>
		</vxe-column>
		<vxe-column title="操作">
			<template #default="{ row, rowIndex }">
				<vxe-button type="text" icon="vxe-icon-add" @click="insertNextRow(row, 'current')"></vxe-button>
				<vxe-button type="text" icon="vxe-icon-minus" @click="removeRow(row)"></vxe-button>
			</template>
		</vxe-column>
	</vxe-table>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from 'vue'
import { VxeTablePropTypes } from 'vxe-table/types/all';

type ParamTableProps = {
	height?: number,
	rows: ParamItem[],
	dataTypes: DataTypeItem[]
}

const { height, rows } = withDefaults(defineProps<ParamTableProps>(), {
	height: 400
})

// 表格实例
const tableRef = ref()

/**
 * 表格编辑配置
 */
const editConfig = reactive<VxeTablePropTypes.EditConfig>({
	trigger: 'click',
	mode: 'cell',
	// row: any, rowIndex: number, column: any, columnIndex: number
	beforeEditMethod: (params) => {
		// 只有叶子结点可编辑
		const row = params.row
		return row.leaf != null && row.leaf
	}
})

const insertNextRow = async (currRow: ParamItem, locat: string) => {
	const $table = tableRef.value
	if ($table) {
		const date = new Date()
		// 如果 null 则插入到目标节点顶部
		// 如果 -1 则插入到目标节点底部
		// 如果 row 则有插入到效的目标节点该行的位置
		if (locat === 'current') {
			const record = {
				name: 'unknown',
				id: Date.now(),
				parentId: currRow.parentId, // 父节点必须与当前行一致
			}
			const { row: newRow } = await $table.insertNextAt(record, currRow)
			await $table.setEditRow(newRow) // 插入子节点
		}
	}
}


/**
 * 展开所有节点
 */
const expandAll = () => {
	const $table = tableRef.value
	if ($table) {
		$table.setAllTreeExpand(true)
	}
}

const removeRow = async (row: ParamItem) => {
	const $table = tableRef.value
	if ($table) {
		await $table.remove(row)
	}
}

onMounted(() => {
	//nextTick(() => expandAll())
})

</script>

<style lang="scss"></style>
