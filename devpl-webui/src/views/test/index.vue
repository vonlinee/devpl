<template>
  <!-- <HeightFixedRegion :minus="200"></HeightFixedRegion> -->

  <button @click="selectFile">选择目录</button>

  <el-icon :size="20">
    <Edit />
  </el-icon>
</template>

<script lang="ts" setup>
import { listDirectories } from '@/utils/file';


const selectFile = async () => {
  // 请求用户选择一个目录  
  const directoryHandle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
  const res : string[] = []
  await listDirectories(directoryHandle, [], res)
}

</script>

<style lang="scss"></style>