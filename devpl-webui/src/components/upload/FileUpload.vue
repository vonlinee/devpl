<template>
  <el-upload
    :show-file-list="true"
    :limit="limit"
    :accept="accept"
    :on-change="onChange"
    :on-remove="onRemove"
    :auto-upload="true"
    :http-request="uploadFile"
  >
    <el-button type="primary">选择文件</el-button>
  </el-upload>
</template>

<script lang="ts">
import { ElButton, UploadFiles, UploadRequestOptions } from "element-plus"
import { apiUploadSingleFile } from "@/api/fileupload"
import { defineComponent, reactive } from "vue"
import { ElMessage, UploadFile } from "element-plus/es"

/**
 * 上传表单参数
 */
interface UploadForm {
  [key: string]: any //自定义key 任意值
  fileList: UploadFiles | undefined
}

export default defineComponent({
  name: "FileUpload",
  components: { ElButton },
  props: {
    accept: {
      type: String,
      default: "",
      required: false,
    },
    limit: {
      type: Number,
      default: 1,
      required: false,
    },
  },
  setup() {
    let uploadForm = reactive<UploadForm>({
      fileList: undefined,
    })

    /**
     * 文件列表改变
     * @param uploadFile
     * @param uploadFiles
     */
    function onChange(uploadFile: UploadFile, uploadFiles: UploadFiles) {
      uploadForm.fileList = uploadFiles
    }

    function onRemove(uploadFile: UploadFile, uploadFiles: UploadFiles) {
      uploadForm.fileList = uploadFiles
    }

    /**
     * 文件上传方法
     * @param options
     */
    function uploadFile(options: UploadRequestOptions) {
      const file = options.file
      // 调用封装好的上传方法，传给后台FormData
      apiUploadSingleFile("", file).then((resp) => {
        if (resp && resp.status == 200) {
          ElMessage.info({
            message: "上传成功",
          })
        }
      })
    }
    return {
      onChange,
      onRemove,
      uploadFile,
    }
  },
})
</script>

<style lang="scss" scoped>
.down > a {
  color: #409eff;
}
</style>
