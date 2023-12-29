<!--
  输入为Schema类，输出可以是SQL或者其他Schema
-->
<template>
  <div>
    <el-form>
      <el-form-item label="类名">
        <el-input></el-input>
      </el-form-item>
    </el-form>
    <div>
      <el-row>
        <el-col :span="12">
          <monaco-editor
            ref="inputEditor"
            language="java"
            height="600px"
            minimap
          />
        </el-col>
        <el-col :span="12">
          <monaco-editor
            ref="outputEditor"
            language="sql"
            height="600px"
            minimap
          />
        </el-col>
      </el-row>
    </div>
    <el-card>
      <el-button @click="convert">转换</el-button>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { ref } from "vue"
import { apiModel2Ddl } from "@/api/devtools"

const inputEditor = ref()
const outputEditor = ref()

const convert = () => {
  apiModel2Ddl(inputEditor.value.getText()).then((res) => {
    outputEditor.value.setText(res)
  })
}
</script>

<style scoped></style>
