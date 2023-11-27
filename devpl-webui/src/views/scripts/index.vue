<template>
  <div style="height: 600px;">
    <MonacoEditor ref="editorRef" language="java" minimap></MonacoEditor>
  </div>
  <el-card>
    <el-button @click="handleCompileButtonClicked">编译</el-button>
    <el-button @click="onClickHandler">获取示例文本</el-button>
    <el-button @click="parseCode">解析</el-button>
  </el-card>
</template>
<script setup lang='ts'>
import { apiCompile, apiGetCompileSampleCode, apiParseCode } from '@/api/code';
import MonacoEditor from '@/components/editor/MonacoEditor.vue';
import { hasText } from '@/utils/tool';
import { ElMessage } from 'element-plus';
import { ref } from 'vue';

const editorRef = ref()

const handleCompileButtonClicked = () => {
  const code = editorRef.value.getText()
  if (hasText(code)) {
    apiCompile(code).then((res) => {
      alert(res.data)
    })
  } else {
    ElMessage({
      type: 'warning',
      message: "文本为空",
      duration: 500
    })
  }
}

const onClickHandler = () => {
  apiGetCompileSampleCode().then((res) => {
    editorRef.value.setText(res.data)
  })
}

const parseCode = () => {
  const code = editorRef.value.getText()
  if (hasText(code)) {
    apiParseCode(code).then((res) => {
      alert(res.data)
    })
  } else {
    ElMessage({
      type: 'warning',
      message: "文本为空",
      duration: 500
    })
  }
}

</script>
<style lang='scss' scoped>

</style>