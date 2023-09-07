<template>
	<el-dialog
		v-model="dialogVisiableRef"
		title="生成结果"
		width="80%"
		:close-on-click-modal="false"
		destroy-on-close
		:on-close="dispose"
		append-to-body
	>
		<el-collapse ref="collapseRef" v-model="activeName" accordion>
			<el-collapse-item v-for="(item, index) in rootDirsRef" :key="index" :title="item" :name="index">
				<component :is="CodeTreeView" style="height: 600px" :dir="item"></component>
			</el-collapse-item>
		</el-collapse>
	</el-dialog>
</template>

<script lang="ts" setup>
import { nextTick, ref } from 'vue'
import CodeTreeView from '@/views/generator/CodeTreeView.vue'
import { ElDialog, ElMessage } from 'element-plus'

const dialogVisiableRef = ref()
const collapseRef = ref()
// 根目录列表
const rootDirsRef = ref<string[]>()
const activeName = ref<string | undefined>('0')

/**
 * 初始化
 * @param dirs 需要展示的文件夹
 */
function init(dirs: string[]) {
	dialogVisiableRef.value = true
	rootDirsRef.value = dirs
	activeName.value = '0'
	// 默认展开第一个折叠框
	if (dirs.length > 0) {
		nextTick(() => collapseRef.value.setActiveNames('0'))
	}
}

function dispose(): void {
	activeName.value = undefined
}

defineExpose({
	init
})
</script>
