<template>
	<div class="table_box card">
		<data-table class="table_cont" ref="singleTableRef" :options="options" :form-object="formObject"
			:convert-row-to-form="convert"
			:api-config="apiConfig" :columns="columns" :dataSource="luckList">
			<template #modal="scope">
				<el-form ref="ruleFormRef" :model="scope.form" status-icon label-width="120px" class="demo-ruleForm">
					<el-form-item label="ID" prop="id">
						<el-input v-model="scope.form.id" />
					</el-form-item>
					<el-form-item label="字段Key" prop="fieldKey">
						<el-input v-model="scope.form.fieldKey" />
					</el-form-item>
					<el-form-item label="字段名称" prop="fieldName">
						<el-input v-model="scope.form.fieldName" />
					</el-form-item>
					<el-form-item label="数据类型" prop="dataType">
						<el-input v-model="scope.form.dataType" />
					</el-form-item>
					<el-form-item label="默认值" prop="defaultValue">
						<el-input v-model="scope.form.defaultValue" />
					</el-form-item>
					<el-form-item label="描述信息" prop="description">
						<el-input v-model="scope.form.description" />
					</el-form-item>
				</el-form>
			</template>
		</data-table>

	</div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { DataTableColumnProps, DataTableOptions } from './interface'
import DataTable, { DataTableApiConfig } from "@/components/table/DataTable.vue";
import { apiListFields, apiSaveOrUpdateField } from "@/api/fields"

// 表单对象，绑定表单数据
const formObject = {
	id: 1,
	fieldKey: '',
	fieldName: '',
	dataType: '',
	defaultValue: '',
	description: ''
}

const convert = (row: any, form: any) : any => {
	form.id = row.id
	form.fieldKey = row.fieldKey
	form.dataType = row.dataType
	form.fieldName = row.fieldName
	form.defaultValue = row.defaultValue
	form.description = row.description
	return form
} 

// 活动列表每一列的配置信息
const columns: DataTableColumnProps[] = [
	{
		prop: 'id',
		label: 'ID',
		align: 'center',
		width: 100
	},
	{
		prop: 'fieldKey',
		label: '字段Key',
		align: 'center'
	},
	{
		prop: 'fieldName',
		label: '字段名称',
		align: 'center'
	},
	{
		prop: 'dataType',
		label: '数据类型',
		align: 'center'
	},
	{
		prop: 'defaultValue',
		label: '默认值',
		align: 'center'
	},
	{
		prop: 'description',
		label: '描述信息',
		align: 'center'
	}
]
const options = reactive<DataTableOptions>({
	stripe: true,
	border: true,
	fit: true,
	'highlight-current-row': true,
	size: 'large',
	loading: false,
	height: 600
})

// 福袋列表数据
const luckList = ref([
	{
		id: 1,
		name: '老李',
		sex: 0
	},
	{
		id: 2,
		name: '老王',
		sex: 1
	}
])

const singleTableRef = ref()

const apiConfig: DataTableApiConfig = {
	queryPage: apiListFields,
	deleteOne: function (row: { [x: string]: any; }): Promise<any> {

		console.log("删除行", row);

		return new Promise((resolve) => {
			resolve({
				code: 122
			})
		});
	},
	update: function (row: { [x: string]: any; }): Promise<any> {
		console.log("更新行", row);
		return new Promise((resolve) => {

		});
	}
}

</script>

<style scoped lang="scss">
.table_box {
	display: flex;
	flex-direction: column;
	margin-top: 10px;
	overflow-y: auto;

	.table_btn {
		margin-bottom: 20px;
		width: 200px;
	}

	.table_cont {
		flex: 1;
	}
}

.card {
	padding: 15px;
	background-color: #ffffff;
	border-radius: 5px;
	box-shadow: 3px 3px 3px #e1e0e0;
}
</style>

