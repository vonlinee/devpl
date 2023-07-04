<template>
    <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
        <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="150px"
                 @keyup.enter="submitHandle()">
            <el-form-item label="模板名称" prop="templateName">
                <el-input v-model="dataForm.templateName"></el-input>
            </el-form-item>
            <el-form-item label="模板文件路径" prop="path">
                <el-upload action="#"
                           :limit="1"
                           :auto-upload="false"
                           :headers="headers"
                           :on-change="handleChange">
                    <template #trigger>
                        <el-button type="primary">选择文件</el-button>
                    </template>
                    <template #default style="margin-left: 10px">
                        <el-button type="primary" @click="uploadBtn">确定上传</el-button>
                    </template>
                    <template #tip>
                        <div class="el-upload__tip" style="color:red;">
                            限制一个文件，下一个文件会覆盖上一个文件
                        </div>
                    </template>
                </el-upload>
            </el-form-item>
            <el-form-item label="模板内容" prop="content">
                <el-input v-model="dataForm.content" placeholder="模板内容"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="submitHandle()">确定</el-button>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import {reactive, ref, toRaw} from 'vue'
import {ElButton, ElDialog, ElMessage} from 'element-plus/es'
import {apiUploadSingleFile} from "@/api/fileupload";
import {apiAddTemplate} from "@/api/template";

const visible = ref(false)
const dataFormRef = ref()

const emit = defineEmits(['refreshDataList'])

/**
 * 表单数据
 */
const dataForm = reactive({
    id: '',
    templateName: '',
    path: '',
    content: '',
})

const init = (id?: number) => {
    visible.value = true
    dataForm.id = ''

    // 重置表单数据
    if (dataFormRef.value) {
        dataFormRef.value.resetFields()
    }
}

let fileUpload = ref()
// 设置请求头
const headers = {
    'Content-Type': 'multipart/form-data'
}

// 选择文件时被调用，将他赋值给fileUpload
const handleChange = (file: any) => {
    fileUpload.value = file
}

// 确定上传
const uploadBtn = async () => {
    apiUploadSingleFile("template", fileUpload.value.raw).then((res) => {
        dataForm.path = res.data.pathList[0];
    })
}

const dataRules = ref({
    templateName: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
    projectCode: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
    projectPackage: [{required: true, message: '必填项不能为空', trigger: 'blur'}],
    projectPath: [{required: true, message: '必填项不能为空', trigger: 'blur'}]
})

// 表单提交
const submitHandle = () => {
    dataFormRef.value.validate((valid: boolean) => {
        if (!valid) {
            return false
        }
        apiAddTemplate(toRaw(dataForm)).then((res) => {
            // @ts-ignore
            if (res.code === 200) {
                ElMessage.info({
                    message: '保存成功',
                    duration: 500,
                    onClose: () => {
                        visible.value = false
                        emit('refreshDataList')
                    }
                })
            }
        })
    })
}

defineExpose({
    init
})
</script>
