<template>
	<el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
		<el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
			<el-form-item label="类型分组" prop="typeGroupId">
				<div>
					<el-select v-model="dataForm.typeGroupId" class="m-2" placeholder="选择类型组">
						<el-option v-for="item in typeGroupOptions" :key="item.typeGroupId" :label="item.typeGroupId"
							:value="item.typeGroupName" />
					</el-select>
					<el-button :icon="Plus" @click="typeGroupModalVisiable = true"></el-button>
				</div>
			</el-form-item>
			<el-form-item label="类型Key" prop="typeKey">
				<el-input v-model="dataForm.typeKey" placeholder="类型Key"></el-input>
			</el-form-item>
			<el-form-item label="属性类型" prop="attrType">
				<el-input v-model="dataForm.attrType" placeholder="属性类型"></el-input>
			</el-form-item>
			<el-form-item label="属性包名" prop="packageName">
				<el-input v-model="dataForm.packageName" placeholder="属性包名"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="visible = false">取消</el-button>
			<el-button type="primary" @click="submitHandle()">确定</el-button>
		</template>
	</el-dialog>

	<vxe-modal v-model="typeGroupModalVisiable" :draggable="false" :loading="loading">
		<vxe-form :data="formData" size="medium" title-align="right" title-width="100" @submit="submitEvent"
			:rules="formRules">
			<vxe-form-item title="分组ID" field="typeGroupId" span="24">
				<template #default="{ data }">
					<vxe-input v-model="data.typeGroupId" placeholder="请输入类型分组ID" clearable></vxe-input>
				</template>
			</vxe-form-item>
			<vxe-form-item title="分组名称" field="typeGroupName" span="24">
				<template #default="{ data }">
					<vxe-input v-model="data.typeGroupName" placeholder="请输入类型分组名称" clearable></vxe-input>
				</template>
			</vxe-form-item>
			<vxe-form-item align="right" :span="24">
				<template #default>
					<vxe-button type="submit" status="primary" content="保存"></vxe-button>
				</template>
			</vxe-form-item>
		</vxe-form>
	</vxe-modal>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, toRaw } from 'vue'
import { ElButton, ElDialog, ElMessage } from 'element-plus/es'
import { useFieldTypeSubmitApi } from '@/api/fieldType'
import { apiListAllDataTypeGroups, apiSaveDataTypeGroup } from '@/api/datatype'
import { Plus } from '@element-plus/icons-vue'
import { VxeFormEvents, VxeFormPropTypes, VXETable } from 'vxe-table'
const emit = defineEmits(['refreshDataList'])

const typeGroupOptions = ref<any[]>([])

onMounted(() => {
	apiListAllDataTypeGroups().then((res) => {
		if (res.code == 200) {
			typeGroupOptions.value = res.list
		}
	})
})



const visible = ref(false)
const typeGroupModalVisiable = ref(false)
const dataFormRef = ref()

interface FormVO {
	id: number | string | undefined,
	typeGroupId: string,
	typeKey: string,
	columnType: string,
	attrType: string,
	packageName: string,
	createDate: string
}

const dataForm = reactive<FormVO>({
	id: undefined,
	typeGroupId: '',
	columnType: '',
	attrType: '',
	packageName: '',
	createDate: '',
	typeKey: ''
})

interface TypeGroupVO {
	typeGroupId: string
	typeGroupName: string
}

const formData = reactive<TypeGroupVO>({
	typeGroupId: '',
	typeGroupName: ''
})

const loading = ref(false)

const submitEvent: VxeFormEvents.Submit = () => {
	loading.value = true
	apiSaveDataTypeGroup(toRaw(formData)).then((res) => {
		if (res.code == 200) {
			loading.value = false

			typeGroupModalVisiable.value = false

			apiListAllDataTypeGroups().then((res) => {
				if (res.code == 200) {
					typeGroupOptions.value = res.list
				}
			})
		}
	})
}

function isLetters(str: string) {
	var re = /^[A-Za-z]+$/;
	if (str.match(re) == null)
		return false;
	else
		return true;
}

const formRules = ref<VxeFormPropTypes.Rules>({
	typeGroupId: [
		{ required: true, message: '请输入类型分组ID' }, {
			validator({ itemValue }) {
				// 自定义校验
				if (!isLetters(itemValue)) {
					return new Error('分组ID为英文字符')
				}
			}
		}
	],
	typeGroupName: [
		{ required: true, message: '请输入类型名称' }
	]
})


const init = (row?: any) => {
	visible.value = true
	dataForm.id = row.id

	// 重置表单数据
	if (dataFormRef.value) {
		dataFormRef.value.resetFields()
	}

	// id 存在则为修改
	if (row.id) {
		dataForm.typeKey = row.typeKey
		dataForm.typeGroupId = row.typeGroupId
	}
}

const dataRules = ref({
	columnType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
	attrType: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
})

// 表单提交
const submitHandle = () => {
	dataFormRef.value.validate((valid: boolean) => {
		if (!valid) {
			return false
		}

		useFieldTypeSubmitApi(dataForm).then(() => {
			ElMessage.success({
				message: '操作成功',
				duration: 500,
				onClose: () => {
					visible.value = false
					emit('refreshDataList')
				}
			})
		})
	})
}

defineExpose({
	init
})
</script>
