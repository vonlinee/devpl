<template>
	<div>
		<el-button @click="dialogVisible = true">打开弹窗</el-button>
		<el-dialog
			:model-value="dialogVisible"
			title="提示"
			width="60%"
			height="50%"
			:append-to-body="true"
			position="fixed"
			top="50%"
			left="50%"
			transform="translate(-50%, -50%)"
		>
			<span>这是一段信息</span>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogVisible = false">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
export default {
	data() {
		return {
			dialogVisible: false
		}
	}
}
</script>
