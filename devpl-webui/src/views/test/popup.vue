<template>
	<div v-show="isVisible" class="popup-container">
		<div class="popup-content">
			<slot></slot>
			<!-- 使用插槽来插入弹窗内容 -->
		</div>
		<button @click="closePopup">关闭</button>
	</div>
</template>

<script>
export default {
	data() {
		return {
			isVisible: true
		}
	},
	methods: {
		closePopup() {
			this.isVisible = false
		}
	}
}
</script>

<style scoped>
.popup-container {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: white;
	border: 1px solid #ccc;
	padding: 20px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
.popup-content {
	margin-top: 10px;
}
</style>
