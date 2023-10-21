<template>
	<el-button size="default" type="primary" @click="showDialog">打开对话框</el-button>
	<popup-window ref="windowRef" :model-value="showDlg" title="标题1111" :close-on-click-modal="false"
		@updateVisible="updateVisible" @close="close" @toggleScreen="toggleScreen">
		<template #content>
			<div>
				<el-button @click="getText">getText</el-button>
				<el-button @click="setText">setText</el-button>
				<div id="container"></div>
			</div>
		</template>

		<template #footer>
			<el-button type="primary" @click="showDlg = false">关闭</el-button>
			<el-button @click="setText()">设置111</el-button>
		</template>
	</popup-window>
</template>

<script setup lang="ts">
import { defineComponent, ref } from 'vue'
import PopupWindow from '@/components/dialog/PopupForm.vue'


const showDlg = ref<boolean>(false)
	const windowRef = ref()

function showDialog() {
	console.log(windowRef);
	
	showDlg.value = true
}


const updateVisible = () => {
	showDlg.value = false
}

const setText = () => {
	windowRef.value.setTitle('1222222222')
}

const close = (): void => {
	console.log('dialog closed!')
}

const toggleScreen = (is_max: boolean): void => {
	console.log('dialog is max:', is_max)
}

const getText = () => {
	console.log(windowRef.value.getTitle());
}

const addElement = () => {
	// let div = document.getElementById('container')
	// if (div) {
	// 	let paragraph = document.createElement('p');
	// 	paragraph.innerText = '11111111';
	// 	div.appendChild(paragraph);
	// } else {
	// 	console.log("容器为null")
	// }
}
</script>
