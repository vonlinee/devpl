<template>
    <div style="overflow: hidden">
        <el-dialog
            class="dialog-vertical pub_dialog"
            title="标题"
            v-model="dialogVisible"
            draggable
            :close-on-click-modal="false"
            :append-to-body="appenToBody">
            <!-- body 区域 -->
            <div slot="default" class="dialog__body_box">
                <slot name="body">
                    <div style="width: 1000px; height: 1080px;"></div>
                </slot>
            </div>
            <span slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
            <div class="dialog__footer_box">
                <!-- 底部使用命名插槽 -->
                <slot name="footer"></slot>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {defineComponent, ref} from "vue";
import {ElDialog} from "element-plus";

export default defineComponent({
    components: {ElDialog},
    props: {
        appenToBody: {
            type: Boolean,
            default: true
        }
    },
    setup(props, context) {
        const dialogVisible = ref(false)

        function init() {
            dialogVisible.value = true
        }

        return {
            dialogVisible,
            init
        }
    }
})

</script>

<style lang="scss" scoped>
.dialog__body_box {
    // 必须有高度 overflow 为自动
    overflow: auto;
    height: 434px;
    border-top: 1px solid #eff1f4;
    border-bottom: 1px solid #eff1f4;
    padding: 0 30px 11px 27px;

    // 滚动条的样式,宽高分别对应横竖滚动条的尺寸
    &::-webkit-scrollbar {
        width: 3px;
        height: 3px;
    }

    // 滚动条里面默认的小方块,自定义样式
    &::-webkit-scrollbar-thumb {
        background: #8798AF;
        border-radius: 2px;
    }

    // 滚动条里面的轨道
    &::-webkit-scrollbar-track {
        background: transparent;
    }
}

.dialog-vertical {
    display: flex;
    padding: 20px;

    .el-dialog {
        margin: auto !important;
    }
}


.pub_dialog {
    display: flex;
    justify-content: center;
    align-items: Center;
    overflow: hidden;

    .el-dialog {
        margin: 0 auto !important;
        height: 90%;
        overflow: hidden;

        .el-dialog__body {
            position: absolute;
            left: 0;
            top: 54px;
            bottom: 0;
            right: 0;
            padding: 0;
            z-index: 1;
            overflow: hidden;
            overflow-y: auto;
        }
    }
}
</style>
