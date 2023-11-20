<script setup lang="ts">
import ParamTable from '@/components/ParamTable.vue';
import { ref } from 'vue';

let mapperParams = ref<ParamItem[]>([{
	id: 1,
	parentId: null,
	name: 'param',
	value: null,
	dataType: null,
	leaf: false
}, {
	id: 11,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 11,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 12,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 13,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 14,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 15,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 16,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 17,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}, {
	id: 18,
	parentId: 1,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}

	, {
	id: 2,
	parentId: null,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: false
}
	, {
	id: 21,
	parentId: 2,
	name: 'userId',
	value: '102120',
	dataType: 'String',
	leaf: true
}
])

const dataTypes = ref<DataTypeItem[]>([
	{
		name: "int",
		value: 1
	},
	{
		name: "string",
		value: 2
	},
	{
		name: "list",
		value: 3
	}
])

const showing = ref()

</script>

<template>
	<button @click="showing = true">showing</button>
	<vxe-modal :model-value="showing" width="70%">
		<ParamTable :rows="mapperParams" :data-types="dataTypes"></ParamTable>
	</vxe-modal>
</template>

<style lang="scss">
.demo-form-inline .el-input {
	--el-input-width: 220px;
}
</style>
