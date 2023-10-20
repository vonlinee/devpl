<template>
	<div class="table_box card">
		<el-button class="table_btn" @click="setCurrent()">Clear selection</el-button>
		<data-table class="table_cont" ref="singleTableRef" @current-change="handleCurrentChange" :config="config"
			:columns="columns" :dataSource="luckList">

			<!-- 单独某列的插槽 -->
			<template #sex="scope">
				{{ scope.row.sex == 0 ? '男' : '女' }}
			</template>
			<template #operation="scope">
				<el-button @click="setCurrent(luckList[1])">Select second row</el-button>
			</template>

			<template #modal="scope">

				<el-form ref="ruleFormRef" :model="scope.form" status-icon label-width="120px" class="demo-ruleForm">
					<el-form-item label="ID" prop="id">
						<el-input v-model="scope.form.id" />
					</el-form-item>
					<el-form-item label="Name" prop="name">
						<el-input v-model="scope.form.name"/>
					</el-form-item>
					<el-form-item label="Sex" prop="sex">
						<el-input v-model="scope.form.sex" />
					</el-form-item>
				</el-form>
			</template>

		</data-table>
	</div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { DataTableColumnProps, DataTableOptions } from './interface'
import DataTable from "@/components/table/DataTable.vue";

// 活动列表每一列的配置信息
const columns: DataTableColumnProps[] = [
	{
		prop: 'name',
		label: '用户',
		align: 'center',
		width: 100
	},
	{
		slot: 'sex',
		label: '性别',
		align: 'center'
	}
]
const config = reactive<DataTableOptions>({
	stripe: true,
	border: true,
	fit: true,
	'highlight-current-row': true,
	size: 'large',
	loading: false
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
const currentRow = ref()
const singleTableRef = ref()

const setCurrent = (row?: any) => {

	console.log(row);

	// table实例上方法的调用
	singleTableRef.value!.setCurrentRow(row)
}
// Table 事件的使用
const handleCurrentChange = (val: any) => {
	currentRow.value = val
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

