<template>
  <vxe-modal v-model="visible" draggable :title="!dataForm.templateId ? '新增' : '修改'" :mask-closable="false" width="75%"
    :z-index="2000" show-footer @close="onClosed">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" @keyup.enter="submitHandle()">
      <el-row>
        <el-col :span="12">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="dataForm.templateName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item prop="typeName" label="模板类型" style="padding-left: 30px">
            <el-select v-model="dataForm.typeName" @change="templateTypeChange">
              <el-option label="字符串模板" value="1"></el-option>
              <el-option label="文件模板" value="2"></el-option>
            </el-select>
          </el-form-item>
          <input ref="inputRef" type="file" style="display: none" accept=".txt,.ftl.vm"
            @change="onFileListChange($event)" />
        </el-col>
        <el-col :span="6">
          <el-form-item prop="provider" label="技术类型" style="padding-left: 30px">
            <el-select v-model="dataForm.provider">
              <el-option label="Velocity" value="Velocity"></el-option>
              <el-option label="FreeMarker" value="FreeMarker"></el-option>
              <el-option label="JFinal Enjoy" value="Enjoy"></el-option>
              <el-option label="String Template" value="ST"></el-option>
              <el-option label="Beetl" value="Beetl"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="模板内容" prop="content">
        <div style="height: 400px; width: 100%">
          <monaco-editor ref="monacoEditorRef" language="plain" :text="dataForm.content"></monaco-editor>
        </div>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="dataForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref, toRaw } from "vue"
import { ElButton, ElMessage, UploadFile } from "element-plus/es"
import { apiUploadSingleFile } from "@/api/fileupload"
import { apiAddTemplate, apiUpdateTemplate } from "@/api/template"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { isBlank } from "@/utils/tool"

const visible = ref(false)
const dataFormRef = ref()

const emit = defineEmits(["refreshDataList"])

const inputRef = ref()
const monacoEditorRef = ref()

/**
 * 打开文件选择弹窗
 */
async function showFileChooserDialog(v: boolean) {
  if (v) {
    inputRef.value.click()
  }
}

function assignTemplateTypeName(val: Number): string {
  if (val == 1) {
    return "字符串模板"
  }
  if (val == 2) {
    return "文件模板"
  }
  return ""
}

/**
 * 模板类型变更，默认字符串模板
 * @param val
 */
function templateTypeChange(val: any) {
  dataForm.typeName = assignTemplateTypeName(val)
  showFileChooserDialog(val == 2)
}

/**
 * 表单数据
 */
const dataForm = reactive<TemplateInfo>({
  templateId: undefined,
  templateName: "",
  templatePath: "",
  content: "",
  type: 1,
  typeName: "字符串模板",
  provider: "Velocity",
  remark: "",
  internal: false,
})

/**
 * 模板文件选择
 * @param event
 */
function onFileListChange(event: Event) {
  const files: File[] = inputRef.value.files
  if (files && files.length > 0) {
    let file = files[0]
    dataForm.templateName = file.name
    let fileReader = new FileReader()
    fileReader.readAsText(file, "UTF-8")
    // 读取文件，得到文件内容
    fileReader.onload = function (e: ProgressEvent) {
      monacoEditorRef.value.setText(fileReader.result)
    }
  }
}

/**
 * 初始化函数
 * @param row
 */
const init = (row?: TemplateInfo) => {
  visible.value = true
  if (row != undefined && row.templateId != undefined) {
    dataForm.templateId = row.templateId
    dataForm.content = row.content
    dataForm.type = row.type
    dataForm.typeName = assignTemplateTypeName(row.type)
    dataForm.templatePath = row.templatePath
    dataForm.templateName = row.templateName
    dataForm.remark = row.remark
    monacoEditorRef.value.setText(row.content)
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

const fileUpload = ref()

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
        message: "上传成功",
        duration: 500,
        onClose: () => {
          dataForm.templatePath = res.data.pathList[0]
        },
      })
    }
  })
}

const dataRules = ref({
  templateName: [
    { required: true, message: "必填项不能为空", trigger: "blur" },
  ],
  projectCode: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  projectPackage: [
    { required: true, message: "必填项不能为空", trigger: "blur" },
  ],
  projectPath: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
})

// 表单提交
const submitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false
    }
    if (isBlank(dataForm.content)) {
      dataForm.content = monacoEditorRef.value.getText()
    }
    if (dataForm.templateId) {
      // 编辑
      apiUpdateTemplate(toRaw(dataForm)).then((res) => {
        // @ts-ignore
        if (res.code === 2000) {
          ElMessage.info({
            message: "修改成功",
            duration: 500,
            onClose: () => {
              visible.value = false
              emit("refreshDataList")
            },
          })
        }
      })
    } else {
      // 新增模板
      apiAddTemplate(toRaw(dataForm)).then((res) => {
        // @ts-ignore
        if (res.code === 200) {
          ElMessage.info({
            message: "保存成功",
            duration: 500,
            onClose: () => {
              visible.value = false
              emit("refreshDataList")
            },
          })
        }
      })
    }
  })
}

defineExpose({
  init,
})
</script>
