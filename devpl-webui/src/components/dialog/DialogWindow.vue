<template>
    <div ref="dialogRef">
        <el-dialog destroy-on-close :center="center" :modal="show_modal" :show-close="show_close"
                   :close-on-press-escape="closeOnPressEscape" :close-on-click-modal="closeOnClickModal"
                   :class="custom_class" :fullscreen="fullscreen" :draggable="true" :width="width" :top="top"
                   :append-to-body="append_to_body" :model-value="dialogVisible" @close="close" @closed="dlgClosed"
                   @opened="opened" @open="open">
            <template #header>
                <div v-html="title" class="popup-window__title"></div>
                <el-button class="el-dialog__headerbtn min_btn" title="最小化" @click="minScreen">—</el-button>
                <el-button class="el-dialog__headerbtn screen_btn" @click="toggleScreen">
                    <el-icon title="最大化" v-if=" fullscreen === false ">
                        <full-screen/>
                    </el-icon>
                    <el-icon title="还原" v-else>
                        <copy-document/>
                    </el-icon>
                </el-button>
            </template>

            <el-scrollbar ref="scrollbarRef">
                <slot name="content"></slot>
            </el-scrollbar>

            <template #footer>
                <slot name="footer"></slot>
            </template>
        </el-dialog>
    </div>

</template>

<script lang="ts">
import {computed, defineComponent, ref, toRefs} from 'vue'

// 引入element-plus图标
import {CopyDocument, FullScreen,} from '@element-plus/icons-vue'

export default defineComponent({
    name: 'DialogWindow',
    components: {
        FullScreen, CopyDocument
    },
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
            default: '50%'
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

        }
    },
    setup(props, ctx) {
        const dialogRef = ref()
        const scrollbarRef = ref()
        const dlg_width = ref()  // 弹窗初始宽度
        const dlg_height = ref() // 弹窗初始高度
        const is_min = ref(false) // 弹窗是否为最小化
        const show_modal = ref()  // 是否显示遮罩层

        // 最大化与还原设置
        const {max_screen, modelValue, scrollTop, modal} = toRefs(props)

        const fullscreen = ref()
        fullscreen.value = max_screen.value
        show_modal.value = modal.value

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
            // 弹窗Dom
            const dialogDom = dialogRef.value
            const dlg = dialogDom.querySelector('.el-dialog')
            const dlg_header = dialogDom.querySelector('.el-dialog__header')
            const dlg_body = dialogDom.querySelector('.el-dialog__body')
            const dlg_footer = dialogDom.querySelector('.el-dialog__footer')

            // 若是先前是最小化时
            if (is_min.value) {

                console.log("最小化")

                dlg.style.width = dlg_width.value
                dlg.style.height = dlg_height.value
                dlg_body.style.display = 'block'
                dlg_footer.style.display = 'block'
                dialogDom.querySelector('.min_btn').style.display = ''
                dialogDom.querySelector('.popup-window__title').style.width = '90%'
                const overlay_dlg = dialogDom.querySelector('.el-overlay-dialog')
                overlay_dlg.style.width = '100%'
                overlay_dlg.style.height = '100%'
                overlay_dlg.parentNode.style.width = '100%'
                overlay_dlg.parentNode.style.height = '100%'
            }

            const dlg_header_h = dlg_header != null ? dlg_header.clientHeight : 0
            const dlg_footer_h = dlg_footer != null ? dlg_footer.clientHeight : 0

            dlg_body.style.height = (document.documentElement.clientHeight - dlg.offsetTop * 2 - dlg_header_h - dlg_footer_h - 24) + 'px'

            is_min.value = false
            show_modal.value = true
        }

        // 最大化与还原操作事件
        const toggleScreen = (): void => {
            fullscreen.value = !fullscreen.value
            setTimeout(() => {
                updateDialogHeight()
                scrollbarRef.value.update()
                ctx.emit('toggleScreen', fullscreen.value)
            }, 100)
        }

        // 最小化操作事件
        const minScreen = (): void => {
            let dialogHtmlDom = dialogRef.value;
            const dlg = dialogHtmlDom.querySelector('.el-dialog')
            const dlg_body = dialogHtmlDom.querySelector('.el-dialog__body')
            const dlg_footer = dialogHtmlDom.querySelector('.el-dialog__footer')

            //保存弹窗初始宽度及高度
            dlg_width.value = dlg.style.width
            dlg_height.value = dlg.style.height

            dlg.style.width = '150px'
            dlg.style.height = '40px'

            dlg_body.style.display = 'none'
            dlg_footer.style.display = 'none'
            dialogHtmlDom.querySelector('.min_btn').style.display = 'none'
            dialogHtmlDom.querySelector('.popup-window__title').style.width = '70px'

            is_min.value = true
            show_modal.value = false
            fullscreen.value = true

            const overlay_dlg = dialogHtmlDom.querySelector('.el-overlay-dialog')
            overlay_dlg.style.width = '150px'
            overlay_dlg.style.height = '40px'
            overlay_dlg.parentNode.style.width = '150px'
            overlay_dlg.parentNode.style.height = '40px'
            overlay_dlg.style.top = 'auto'
            overlay_dlg.style.bottom = 0
            overlay_dlg.parentNode.style.top = 'auto'
            overlay_dlg.parentNode.style.bottom = 0

        }

        // 弹窗打开后重置dialog body 高度
        const opened = () => {
            updateDialogHeight()
            if (scrollTop.value) {
                scrollbarRef.value.setScrollTop(0)
            }
            ctx.emit('opened')
        }

        return {
            dialogRef,
            scrollbarRef,
            fullscreen,
            dialogVisible,
            show_modal,
            toggleScreen,
            minScreen,
            dlgClosed,
            close,
            open,
            opened
        }
    }
});
</script>

<style lang="scss">

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

.min_btn, .screen_btn {
    border: 0 !important;
    color: var(--el-color-info) !important;
    position: absolute;
    cursor: pointer;
    font-size: 14px !important;
    top: 6px !important;
}

.min_btn {
    right: 56px !important;
    top: 5px !important;
}

.screen_btn {
    right: 32px !important;
}

.min_btn:hover, .screen_btn:hover {
    color: var(--el-color-primary) !important;
    background: #fff !important;
}

.screen_btn:active, .screen_btn:link, .screen_btn:visited, .screen_btn:focus {
    background: #fff !important;
    color: var(--el-color-info) !important;
}

.el-dialog__body {
    padding: 0 15px !important;
    margin: 12px 0 !important;
    overflow-y: auto;
}
</style>
