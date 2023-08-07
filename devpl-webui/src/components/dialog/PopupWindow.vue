<template>
    <el-dialog ref="dialogRef" destroy-on-close :center="center" :modal="show_modal" :show-close="show_close"
               :close-on-press-escape="closeOnPressEscape" :close-on-click-modal="closeOnClickModal"
               :class="custom_class" :fullscreen="fullscreen" :draggable="true" :width="width" :top="top"
               :title="titleRef"
               :append-to-body="append_to_body" :model-value="dialogVisible" @close="close" @closed="dlgClosed"
               @opened="opened" @open="open">
        <template #header>
            <div v-html="title" class="popup-window__title"></div>
        </template>
        <!-- 内容区域 -->
        <div style="overflow-y: auto; height: 600px">
            <slot name="content"></slot>
        </div>
        <template #footer>
            <slot name="footer"></slot>
        </template>
    </el-dialog>
</template>

<script lang="ts">
import {computed, defineComponent, ref, toRefs} from 'vue'

export default defineComponent({
    name: 'PopupWindow',
    emits: ['updateVisible', 'close', 'closed', 'toggleScreen', 'opened', 'open'],
    props: {
        // 对话框标题
        title: {
            type: String,
            default: '',
        },
        // 是否全屏显示
        max_screen: {
            type: Boolean,
            default: false
        },
        // 对话框的ref
        dlg_ref: {
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
        custom_class: {
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
        show_close: {
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
            default: true
        },
        // 是否显示遮罩层
        modal: {
            type: Boolean,
            default: true
        },
        // Dialog 自身是否插入至 body 元素上。 嵌套的 Dialog 必须指定该属性并赋值为 true
        append_to_body: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        setTitle(title: string) {
            console.log("title=", title)
            this.titleRef = title
        }
    },
    setup(props, ctx) {
        const dialogRef = ref()
        const show_modal = ref()  // 是否显示遮罩层

        // 最大化与还原设置
        const {max_screen, modelValue, modal, title} = toRefs(props)

        const fullscreen = ref()
        fullscreen.value = max_screen.value
        show_modal.value = modal.value

        let titleRef = ref<string>(title.value)

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
            show_modal.value = true
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
            show_modal,
            dlgClosed,
            close,
            open,
            opened
        }
    }
});
</script>

<style scoped lang="scss">

// 溢出控制，影响全局
.el-overlay-dialog {
    overflow: hidden;
}

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
