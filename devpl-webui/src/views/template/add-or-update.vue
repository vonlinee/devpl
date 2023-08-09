<template>
    <el-dialog v-model="visible" draggable :title="!dataForm.templateId ? '新增' : '修改'" :close-on-click-modal="false"
               @closed="onClosed">
        <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules"
                 @keyup.enter="submitHandle()">
            <el-row>
                <el-col span="12">
                    <el-form-item label="模板名称" prop="templateName">
                        <el-input v-model="dataForm.templateName"></el-input>
                    </el-form-item>
                </el-col>
                <el-col span="8">
                    <el-form-item prop="generatorType" style="padding-left: 30px">
                        <el-radio-group v-model="dataForm.type">
                            <el-radio :label="1">字符串模板</el-radio>
                            <el-radio :label="2">文件模板</el-radio>
                            <el-upload v-if="dataForm.type === 2"
                                       :auto-upload="false">
                                <template #trigger>
                                    <el-button type="primary">选择模板文件</el-button>
                                </template>
                            </el-upload>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="模板内容" prop="content">
                <div style="height: 400px; width: 100%">
                    <monaco-editor language="plain" :text="dataForm.content"></monaco-editor>
                </div>
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
import {ElButton, ElDialog, ElMessage, UploadFile} from 'element-plus/es'
import {apiUploadSingleFile} from "@/api/fileupload";
import {apiAddTemplate, apiUpdateTemplate} from "@/api/template";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

const visible = ref(false)
const dataFormRef = ref()

const emit = defineEmits(['refreshDataList'])

/**
 * 模板信息
 */
interface TemplateInfo {
    templateId?: Number,
    templateName: String,
    templatePath: String,
    content: String,
    type: Number
}

/**
 * 表单数据
 */
const dataForm = reactive<TemplateInfo>({
    templateId: undefined,
    templateName: '',
    templatePath: '',
    content: '',
    type: 1
})

/**
 * 初始化函数
 * @param id 行ID
 */
const init = (id?: number) => {
    visible.value = true
    if (id) {
        dataForm.templateId = id
    } else {
        // 重置表单数据
        if (dataFormRef.value) {
            dataFormRef.value.resetFields()
        }
    }
}

function onClosed() {
    dataForm.templateId = undefined
}

let fileUpload = ref()
// 设置请求头
const headers = {
    'Content-Type': 'multipart/form-data'
}

// 选择文件时被调用，将他赋值给fileUpload
const handleChange = (file: UploadFile) => {
    fileUpload.value = file
    dataForm.templateName = file.name
}

// 确定上传
const uploadBtn = async () => {
    apiUploadSingleFile("template", fileUpload.value.raw).then((res) => {
        if (res.code === 200) {
            ElMessage.info({
                message: '上传成功',
                duration: 500,
                onClose: () => {
                    dataForm.templatePath = res.data.pathList[0];
                }
            })
        }
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

        if (dataForm.templateId) {
            apiUpdateTemplate(toRaw(dataForm)).then((res) => {
                // @ts-ignore
                if (res.code === 200) {
                    ElMessage.info({
                        message: '修改成功',
                        duration: 500,
                        onClose: () => {
                            visible.value = false
                            emit('refreshDataList')
                        }
                    })
                }
            })
        } else {
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
        }
    })
}

defineExpose({
    init
})
</script>
