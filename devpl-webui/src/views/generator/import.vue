<template>
	<vxe-modal height="80%" width="60%" v-model="visible" title="导入数据库表" :mask-closable="false" draggable :z-index="2000"
		show-footer>
		<el-form ref="dataFormRef" :model="dataForm">
			<el-form-item label="数据源" prop="datasourceId">
				<el-select v-model="dataForm.datasourceId" style="width: 100%" placeholder="请选择数据源" @change="getTableList">
					<el-option label="默认数据源" value="0"></el-option>
					<el-option v-for="ds in dataForm.datasourceList" :key="ds.id" :label="ds.connName" :value="ds.id"> </el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="表名称" prop="tableNamePattern">
				<el-row>
					<el-col :span="20">
						<el-input v-model="dataForm.tableNamePattern"></el-input>
					</el-col>
					<el-col :span="4">
						<el-button @click="getTableList">查询</el-button>
					</el-col>
				</el-row>
			</el-form-item>
		</el-form>
		<el-table :data="dataForm.tableList" style="width: 100%" border @selection-change="selectionChangeHandle">
			<el-table-column type="selection" header-align="center" align="center" width="40"></el-table-column>
			<el-table-column prop="tableName" label="表名" header-align="center" align="center" width="300"></el-table-column>
			<el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
		</el-table>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElButton, ElInput, ElRow, ElCol, ElMessage, ElForm, ElFormItem, ElSelect, ElOption, ElTable, ElTableColumn } from 'element-plus/es'
import { useDataSourceListApi } from '@/api/datasource'
import { useTableImportSubmitApi } from '@/api/table'
import { useDataSourceTableListApi } from '@/api/datasource'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref()

const dataForm = reactive({
	id: '',
	tableNameListSelections: [] as any,
	datasourceId: '',
	tableNamePattern: null,
	datasourceList: [] as any,
	tableList: [] as any,
	table: {
		tableName: ''
	}
})

// 多选
const selectionChangeHandle = (selections: any[]) => {
	dataForm.tableNameListSelections = selections.map((item: any) => item['tableName'])
}

const init = () => {
	visible.value = true
	dataForm.id = ''

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	dataForm.tableList = []

	getDataSourceList()
}

const getDataSourceList = () => {
	useDataSourceListApi().then(res => {
		dataForm.datasourceList = res.data
	})
}

const getTableList = () => {
	dataForm.table.tableName = ''
	if (!dataForm.datasourceId) {
		return
	}
	useDataSourceTableListApi(dataForm.datasourceId, dataForm.tableNamePattern).then(res => {
		dataForm.tableList = res.data
	})
}

// 表单提交
const submitHandle = () => {
	const tableNameList = dataForm.tableNameListSelections ? dataForm.tableNameListSelections : []
	if (tableNameList.length === 0) {
		ElMessage.warning('请选择记录')
		return
	}

	useTableImportSubmitApi(dataForm.datasourceId, tableNameList).then(() => {
		ElMessage.success({
			message: '操作成功',
			duration: 500,
			onClose: () => {
				visible.value = false
				emit('refreshDataList')
			}
		})
	})
}

defineExpose({
	init
})
</script>
