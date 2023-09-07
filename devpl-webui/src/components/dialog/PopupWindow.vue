<!-- eslint-disable vue/no-v-html -->
<!--弹窗组件-->
<template>
	<el-dialog
		ref="dialogRef"
		destroy-on-close
		:center="center"
		:modal="showModal"
		:show-close="showClose"
		:close-on-press-escape="closeOnPressEscape"
		:close-on-click-modal="closeOnClickModal"
		:class="customClass"
		:fullscreen="fullscreen"
		:draggable="false"
		:width="width"
		:top="top"
		:title="titleRef"
		:append-to-body="appendToBody"
		:model-value="dialogVisible"
		@close="close"
		@closed="dlgClosed"
		@opened="opened"
		@open="open"
	>
		<template #header>
			<div class="popup-window__title" v-html="title"></div>
		</template>
		<!-- 内容区域 -->
		<div :style="contentAreastyle">
			<slot name="content"></slot>
		</div>
		<!-- 底部操作栏 -->
		<template #footer>
			<slot name="footer"></slot>
		</template>
	</el-dialog>
</template>

<script lang="ts">
import { computed, defineComponent, ref, toRefs } from 'vue'

export default defineComponent({
	name: 'PopupWindow',
	props: {
		// 对话框标题
		title: {
			type: String,
			default: ''
		},
		// 内容区域样式
		contentAreastyle: {
			type: Object,
			required: false,
			default: () => {
				return {
					overflowY: 'auto',
					height: '600px'
				}
			}
		},
		// 是否全屏显示
		maxScreen: {
			type: Boolean,
			default: false
		},
		// 对话框的ref
		dlgRef: {
			type: String,
			default: ''
		},
		// 对话框宽度
		width: {
			type: String,
			default: '80%'
		},
		// 对话框顶部相对于窗口的偏移距离
		top: {
			type: String,
			default: '15vh'
		},
		// 是否显示对话框
		modelValue: {
			type: Boolean,
			default: false
		},
		// Dialog 的自定义类名
		customClass: {
			type: String,
			default: ''
		},
		// 是否可以通过点击 modal 关闭 Dialog
		closeOnClickModal: {
			type: Boolean,
			default: false
		},
		// 是否可以通过按下 ESC 关闭 Dialog
		closeOnPressEscape: {
			type: Boolean,
			default: true
		},
		// 是否显示关闭按钮
		showClose: {
			type: Boolean,
			default: true
		},
		// 是否让 Dialog 的 header 和 footer 部分居中排列
		center: {
			type: Boolean,
			default: false
		},
		// 每次打开弹窗都滚动到头部位置
		scrollTop: {
			type: Boolean,
			default: true,
      required: false
		},
		// 是否显示遮罩层
		modal: {
			type: Boolean,
			default: true,
      required: false
		},
		// Dialog 自身是否插入至 body 元素上。 嵌套的 Dialog 必须指定该属性并赋值为 true
		appendToBody: {
			type: Boolean,
			default: false
		}
	},
	emits: ['updateVisible', 'close', 'closed', 'toggleScreen', 'opened', 'open'],
	setup(props, ctx) {


		// 最大化与还原设置
		const { maxScreen, modelValue, modal, title } = toRefs(props)

		const dialogRef = ref()
		const showModal = ref<boolean>(modal.value) // 是否显示遮罩层
		const fullscreen = ref<boolean>(maxScreen.value)
		const titleRef = ref<string>(title.value)  // 标题

		// 弹窗是否可见
		const dialogVisible = computed(() => modelValue.value)

		// 弹窗关闭时，将父组件中重置为弹窗不显示
		const dlgClosed = (): void => {
			ctx.emit('updateVisible', false)
			ctx.emit('closed')
		}

		const open = (): void => {
			ctx.emit('open')
		}

		// 弹窗关闭时的回调
		const close = (): void => {
			ctx.emit('close')
		}

		// 设置对话框body高度
		const updateDialogHeight = (): void => {
			showModal.value = true
		}

		// 弹窗打开后重置dialog body 高度
		const opened = () => {
			updateDialogHeight()
			ctx.emit('opened')
		}

		return {
			dialogRef,
			titleRef,
			fullscreen,
			dialogVisible,
			showModal,
			dlgClosed,
			close,
			open,
			opened
		}
	},
	methods: {
		setTitle(title: string) {
			this.titleRef = title
		},
		getTitle() {
			return this.titleRef;
		},
		show(x: number, y: number, w: number, h: number) {
			console.log("show ", x, y , w, h);
		}
	}
})
</script>

<style scoped lang="scss">
.el-dialog__header {
	padding: 10px 15px 0 !important;
	position: relative !important;
	margin-right: 0 !important;
}

.el-dialog__headerbtn {
	top: 8px !important;
	right: 10px !important;
	width: 22px !important;
	height: 22px !important;
	font-size: 18px !important;
	text-align: center;
}

.el-dialog__title {
	font-size: 15px !important;
	line-height: 16px !important;
}

.popup-window__title {
	width: 90%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.el-dialog__body {
	padding: 0 15px !important;
	margin: 12px 0 !important;
	overflow-y: auto;
}
</style>
