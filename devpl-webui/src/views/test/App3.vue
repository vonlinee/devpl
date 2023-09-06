<template>
	<div id="app">
		<button @click="showPopup">显示弹窗</button>
		<popup v-if="isPopupVisible">
			<div>111111111</div>
		</popup>
	</div>
</template>

<script>
import Popup from './popup.vue' // 导入弹窗组件

export default {
	name: 'App3',
	components: {
		Popup // 在Vue组件中使用弹窗组件
	},
	data() {
		return {
			isPopupVisible: false // 控制弹窗的可见性
		}
	},
	methods: {
		showPopup() {
			this.isPopupVisible = true // 显示弹窗
		}
	}
}
</script>
