<template>
    <el-button @click="dialogVisible = true">单击打开弹框</el-button>
    <el-dialog
        title="标题XX"
        v-model="dialogVisible"
        width="75%"
        height="75%"
        draggable
        top="0px"
        :append-to-body="appenToBody">
        <div slot="default">
            <slot name="body">

            </slot>
        </div>
        <span slot="footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
    </el-dialog>
</template>

<script>
import {defineComponent, ref} from "vue";
import {ElDialog} from "element-plus";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

export default defineComponent({
    components: {MonacoEditor, ElDialog},
    props: {
        appenToBody: {
            type: Boolean,
            default: false
        }
    },
    setup(props, context) {
        const visiable = ref()

        const dialogVisible = ref(false)

        function init() {
            visiable.value = true
        }

        return {
            dialogVisible,
            visiable,
            init
        }
    }
})

</script>

<style scoped>

</style>
