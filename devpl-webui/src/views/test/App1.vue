<template>
    <el-button size="default" type="primary" @click=" showDlg = true ">打开对话框</el-button>

    <popup-window :model-value="showDlg" title="标题1111" :close-on-click-modal="false" @updateVisible="updateVisible"
                  ref="windowRef"
                  @close="close" @toggleScreen="toggleScreen">

        <template #content>
            <div> 内容开始</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 这是一个可以最大化及还原的 对话框</div>
            <div> 内容结束</div>
        </template>

        <template #footer>
            <el-button type="primary" @click=" showDlg = false ">关闭</el-button>
            <el-button @click="setText()">设置111</el-button>
        </template>
    </popup-window>

</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import PopupWindow from "@/components/dialog/PopupWindow.vue";

export default defineComponent({
    name: 'App1',
    components: {PopupWindow},
    setup() {
        const showDlg = ref()
        showDlg.value = false

        const windowRef = ref()

        const updateVisible = () => {
            showDlg.value = false
        }

        const setText = () => {
            windowRef.value.setTitle("11111111111")
        }

        const close = (): void => {
            console.log('dialog closed!')
        }

        const toggleScreen = (is_max: boolean): void => {
            console.log('dialog is max:', is_max)
        }

        return {
            setText,
            showDlg,
            windowRef,
            updateVisible,
            close,
            toggleScreen
        }
    }
});
</script>


