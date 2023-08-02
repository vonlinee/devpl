<template>
    <div id="app">
        <my-dialog
            :visiable.sync="isShowDialog"
            title="设置标题"
            :showCloseIcon="true"
            @beforeClose="beforeClose"
            :mask="true"
            :clickMaskClose="true"
        >
            <!-- 要与组件的具名插槽对应 -->
            <template slot="header"> 具名插槽</template>
            <template>默认插槽
                <div style="height: 1080px"></div>
            </template>
            <!-- 要与子组件的插槽对应 -->
            <template slot="footer">
                <el-button size="small" @click="isShowDialog = false">取消</el-button>
                <el-button type="primary" size="small" @click="isShowDialog = false"
                >确认
                </el-button
                >
            </template>
        </my-dialog>
        <br/>
        <br/>
        <br/>
        <el-button @click="isShowDialog = true" type="primary">
            打开弹框
        </el-button>
    </div>
</template>

<script>

import MyDialog from "@/components/dialog/MyDialog.vue";

export default {
    name: "TestDialog",
    data() {
        return {
            isShowDialog: false,
        };
    },
    components: {
      MyDialog
    },
    methods: {
        // 关闭前的回调
        beforeClose(flag) {
            //   this.$confirm("是否关闭", "提示", {
            //     confirmButtonText: "确认关闭",
            //     cancelButtonText: "取消",
            //     type: "warning",
            //   })
            //     .then(() => {
            this.isShowDialog = flag;
            // })
            // .catch(() => {
            //   // 不关闭
            // });
        },
    },
};
</script>

<style lang="scss" scoped>
#app {
    width: 100%;
    min-height: 100vh;
    box-sizing: border-box;
    padding: 50px;
}
</style>
