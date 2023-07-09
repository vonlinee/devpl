<template>
    <el-button @click="dialogVisible = true">单击打开弹框</el-button>
    <el-dialog
        title="标题XX"
        v-model="dialogVisible"
        width="75%"
        top="0vh"
        draggable
        class="center-dialog">
        <div style="height: 80vh;">dialog内容...</div>
        <span slot="footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
    </el-dialog>
</template>

<script>
import {defineComponent, ref, useSlots} from "vue";
import {ElDialog} from "element-plus";

export default defineComponent({
    components: {ElDialog},
    setup(props, context) {
        const visiable = ref()
        const slots = useSlots()

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
/*
    控制的dialog样式需要穿透到底层，所以这里不能写scoped属性
    如果不想影响全局，可以给el-dialog的父级定义一个class,再把.center-dialog置于这个class的嵌套里面
*/
.center-dialog {
    top: 50%;
    transform: translateY(-50%)
}
</style>
