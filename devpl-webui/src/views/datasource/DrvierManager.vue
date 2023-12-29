<template>
  <file-selector ref="fileSelector"></file-selector>
  <el-button @click="upload">上传</el-button>
</template>

<script setup lang="ts">
import FileSelector from "@/components/FileSelector.vue"
import { ref } from "vue"
import { apiUploadDriverJar } from "@/api/datasource"

let fileSelector = ref()

function upload() {
  let files = fileSelector.value.getRawFiles()
  apiUploadDriverJar(files[0]).then((res) => {
    console.log(res)
  })
}
</script>
