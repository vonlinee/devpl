<!--
  输入为SQL(支持DDL，查询SQL等)，转换为其他，比如实体类
-->
<template>
  <div>
    <el-card>
      <data-source-selector
        @selection-change="(val) => (dataSourceId = val.id)"
      ></data-source-selector>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from "vue"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import DataSourceSelector from "@/views/generator/DataSourceSelector.vue"

const dataSourceId = ref()

const options = reactive({
  inputType: "DDL",
})

const editorRef = ref()
</script>
