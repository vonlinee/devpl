<template>
    <el-dialog class="dialog" v-model="visiable">
        <monaco-editor language="ts"></monaco-editor>
    </el-dialog>
</template>

<script>

import {defineComponent, ref} from "vue";
import {ElDialog} from "element-plus";
import CodeEditor from "@/components/CodeEditor.vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

export default defineComponent({
    components: {MonacoEditor, CodeEditor, ElDialog},
    setup(props, context) {

        const visiable = ref()

        function init() {
            visiable.value = true
        }
        return {
            visiable,
            init
        }
    }
})


</script>

<style scoped>
.dialog {
    margin-top: 9vh !important;
    margin-bottom: 8vh !important;
    overflow: auto;
}
</style>
