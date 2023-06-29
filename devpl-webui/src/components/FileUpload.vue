<template>
	<el-upload
		:show-file-list="true"
		:limit="1"
		accept="accept"
		:on-change="(file, fileList) => uploadForm.fileList = fileList"
		:on-remove="(file, fileList) => uploadForm.fileList = fileList"
		:auto-upload="false"
		:http-request="uploadFile">
		<el-button type="primary">选择文件</el-button>
	</el-upload>
</template>

<script setup lang="ts">
import {ElButton} from "element-plus";
import {apiUploadSingleFile} from "@/api/fileupload";
import {reactive} from "vue";

defineExpose({
	accept: {
		type: String,
		required: false
	}
})

/**
 * 上传表单参数
 */
let uploadForm = reactive({
	fileList: []
})

function uploadFile(params: any) {
	const file = params.file;
	// 调用封装好的上传方法，传给后台FormData
	apiUploadSingleFile(file).then(resp => {
		if (resp && resp.status == 200) {
			console.log("上传成功")
		}
	})
}
</script>

<style lang="scss" scoped>
.down > > > a {
	color: #409EFF;
}
</style>

