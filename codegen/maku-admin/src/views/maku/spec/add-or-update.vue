<template>
    <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
        <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px"
                 @keyup.enter="submitHandle()">
                    </el-form>
        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="submitHandle()">确定</el-button>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
    import {reactive, ref} from 'vue'
    import {ElMessage} from 'element-plus/es'

    const emit = defineEmits(['refreshDataList'])

    const visible = ref(false)
    const dataFormRef = ref()

    const dataForm = reactive({
    })

    const init = (id
    ? : number
    ) =>
    {
        visible.value = true
        dataForm.id = ''

        // 重置表单数据
        if (dataFormRef.value) {
            dataFormRef.value.resetFields()
        }

        if (id) {
            getSpec(id)
        }
    }

    const getSpec = (id: number) => {
        useSpecApi(id).then(res => {
            Object.assign(dataForm, res.data)
        })
    }

    const dataRules = ref({
    })

    // 表单提交
    const submitHandle = () => {
        dataFormRef.value.validate((valid: boolean) => {
            if (!valid) {
                return false
            }

            useSpecSubmitApi(dataForm).then(() => {
                ElMessage.success({
                    message: '操作成功',
                    duration: 500,
                    onClose: () => {
                        visible.value = false
                        emit('refreshDataList')
                    }
                })
            })
        })
    }

    defineExpose({
        init
    })
</script>
