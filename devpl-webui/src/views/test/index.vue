<script setup lang="ts">

import {ref} from "vue";
import AdaptiveDialog from "@/components/AdaptiveDialog.vue";
import EleUploadFile from "@/components/upload/FileUpload.vue";
import FileUpload from "@/components/upload/FileUpload.vue";

let dialogRef = ref()

function show() {
    dialogRef.value.init()
}

const editorRef = ref()
const languageRef = ref()

const inputRef = ref()

function updateLanguage() {
    languageRef.value = editorRef.value.getLanguage()
}

function setLanguage() {
    editorRef.value.setLanguage(inputRef.value)
}

defineProps({
    // 值
    value: [String, Object, Array],
    // 必选参数，上传的地址
    // 同 element-ui upload 组件
    action: {
        type: String,
        required: true
    },
    // 大小限制(MB)
    fileSize: Number,
    // 响应处理函数
    responseFn: Function,
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: Array,
    // 提示
    placeholder: String,
    // 是否禁用
    disabled: Boolean,
    // 是否显示文件大小
    isShowSize: {
        type: Boolean,
        default: true
    },
    // 是否显示下载
    isCanDownload: {
        type: Boolean,
        default: true
    },
    // 是否可删除
    isCanDelete: {
        type: Boolean,
        default: true
    },
    // 是否可上传相同文件
    isCanUploadSame: {
        type: Boolean,
        default: true
    },
    // 是否显示提示
    isShowTip: {
        type: Boolean,
        default: true
    },
    // 是否显示上传成功的提示
    isShowSuccessTip: {
        type: Boolean,
        default: true
    },
    // 删除前的操作
    // 同 element-ui upload 组件
    beforeRemove: Function,
    // 设置上传的请求头部
    // 同 element-ui upload 组件
    headers: Object,
    // 是否支持多选文件
    // 同 element-ui upload 组件
    multiple: {
        type: Boolean,
        default: true
    },
    // 上传时附带的额外参数
    // 同 element-ui upload 组件
    data: Object,
    // 上传的文件字段名
    // 同 element-ui upload 组件
    name: {
        type: String,
        default: 'file'
    },
    // 支持发送 cookie 凭证信息
    // 同 element-ui upload 组件
    withCredentials: {
        type: Boolean,
        default: false
    },
    // 接受上传的文件类型
    // 同 element-ui upload 组件
    accept: String,
    // 最大允许上传个数
    // 同 element-ui upload 组件
    limit: Number
})

function handleResponse() {
    console.log("handleResponse")
}

let file = ref()

</script>

<template>
    <button @click="show">展示弹窗</button>
    <button @click="updateLanguage()">更新语言</button>
    <button @click="setLanguage()">设置语言</button>

    <el-input v-model="inputRef"></el-input>
    <adaptive-dialog ref="dialogRef"></adaptive-dialog>

    <file-upload
        :disabled="false"
        :file-type="['doc', 'pdf', 'png', 'jpeg', 'jpg']"
        :fileSize="2"
        :isCanDelete="true"
        :isCanDownload="true"
        :isCanUploadSame="true"
        :isShowSize="true"
        :isShowTip="true"
        :limit="6"
        :multiple="true"
        :responseFn="handleResponse"
        action="https://jsonplaceholder.typicode.com/posts/"
        placeholder="上传附件"
        v-model="file"
    />
</template>

<style scoped lang="scss">

</style>
