<template>
	<vxe-modal title="编辑" v-model="visible" show-footer @close="handleClose(0)" @confirm="handleClose(1)">
		<el-input v-model="editingRowCopy.value"></el-input>
	</vxe-modal>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';

const visible = ref()
// 保存编辑的行，编辑时外面的值也会跟着变
const editingRow = ref()

const editingRowCopy = reactive<FieldInfo>({
	fieldKey: ""
})

const emits = defineEmits([
	"submit"
])

/**
 * 
 * @param type 1-确认 0-关闭
 */
const handleClose = (type: number) => {
	if (type == 1) {
		editingRow.value.value = editingRowCopy.value
	}
	emits("submit", editingRow.value)
}

defineExpose({
	open(row: FieldInfo) {
		editingRow.value = row
		Object.assign(editingRowCopy, row)
		visible.value = true
	}
})

</script>

<style lang="scss" scoped></style>