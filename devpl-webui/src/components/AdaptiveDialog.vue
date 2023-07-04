<template>
    <el-dialog class="dialog">
    </el-dialog>
</template>

<script>

import {defineComponent} from "vue";
import {ElDialog} from "element-plus";

export default defineComponent({
    components: {ElDialog},
    setup(props, context) {

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
