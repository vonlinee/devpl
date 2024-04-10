<template>
  <vxe-modal title="调试Mapper Statement" v-model="visible" width="80%" fullscreen show-zoom @show="onModalShown">
    <div style="height: 100% !important; display: flex; flex-direction: row">
      <div style="display: flex; flex-direction: column; height: 100%; width: 800px">
        <div style="flex-grow: 1; height: 400px">
          <monaco-editor ref="inputRef" language="xml" />
        </div>
        <div style="height: 450px">
          <monaco-editor ref="outputRef" language="sql" />
        </div>
      </div>

      <div style="flex-grow: 1">
        <el-button @click="fillSampleMapperStatement">填充样例</el-button>
        <el-button @click="showDialog()">导入</el-button>

        <param-import ref="importModalRef" @submit="handleParamImport"></param-import>

        <FieldValueTreeTable ref="msParamTable" :data-types="msParamValueTypes"></FieldValueTreeTable>

        <div>
          <el-card>
            <el-button-group class="ml-4">
              <el-button type="primary" @click="getParams()">解析参数</el-button>
              <el-button type="primary" @click="getSqlOfMapperStatement(false)">获取预编译sql</el-button>
              <el-button type="primary" @click="getSqlOfMapperStatement(true)">获取实际sql</el-button>
            </el-button-group>

            <el-dropdown>
              <el-button type="primary">
                Mapper Snippet<el-icon class="el-icon--right">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleMsForeach">Foreach</el-dropdown-item>
                  <el-dropdown-item @click="handleMsStringNotEmpty">Test字符串不为空</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-card>
          <el-card>
            <el-checkbox v-model="options.enableTypeInference" label="开启类型推断" size="large" />
            <el-checkbox v-model="options.inferByParamName" label="根据名称推断类型" size="large" />
          </el-card>
        </div>
      </div>
    </div>
  </vxe-modal>
</template>

<script setup lang="ts">
import { getSubStrings, hasText, isBlank } from "@/utils/tool"
import { nextTick, onMounted, ref, resolveDirective, toRaw } from "vue"

import {
  apiGetMapperStatementValueTypes,
  apiGetSampleXmlText,
  apiGetSql,
  getMapperStatementParams,
} from "@/api/mybatis"
import { ElButton, ElMessage } from "element-plus"

import ParamImport from "./ParamImport.vue"
import FieldValueTreeTable from "./FieldValueTreeTable.vue"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"

import { foreachSnippet, stringSnippet } from "@/utils/mybatis"
import { ArrowDown } from "@element-plus/icons"
import { apiParseFields } from "@/api/fields"
import { Message } from "@/hooks/message"

// 数据
const inputRef = ref()
const outputRef = ref()
const visible = ref()

const importModalRef = ref()


const editorHeight = ref("400")

// 表格实例
const msParamTable = ref()
const msParamValueTypes = ref()

/**
 * 参数导入
 * @param type 
 * @param input 
 */
const handleParamImport = (type: string, input: string) => {

  if (type == 'url') {
    type = 'other>' + type
  } else if (type == 'json') {
    type = 'json>' + type
  } else {
    Message.error("不支持的输入类型 " + type)
    return;
  }

  apiParseFields({
    type: type,
    content: input
  }).then((res) => {
    if (res.data?.failed) {
      Message.error("解析失败" + res.data?.errorMsg)
    } else {
      msParamTable.value.addFields(res.data?.fields)
    }
  })
}

onMounted(() => {
  const layoutCard: any = document.querySelector(".layout-card")
  if (layoutCard) {
    editorHeight.value = Math.floor(layoutCard.clientHeight / 2) + "px"
  }

  apiGetMapperStatementValueTypes().then((res) => {
    msParamValueTypes.value = res.data
  })
})

function output(handler: (input: string) => string) {
  const input = inputRef.value.getText()
  if (!isBlank(input)) {
    const result = handler(input)
    outputRef.value.setText(result)
  }
}

const handleMsForeach = () => {
  output((input: string) => {
    const result = getSubStrings(input)
    return foreachSnippet(result[0], result[1] || "列名")
  })
}

const handleMsStringNotEmpty = () => {
  output((input: string) => {
    const result = getSubStrings(input)
    return stringSnippet(result[0], result[1] || "列名")
  })
}

/**
 * 类型推断规则
 */
interface MyBatisToolOptions {
  // 开启自动类型推断，不一定准确
  enableTypeInference: boolean
  inferByParamName: boolean
}

const options = ref<MyBatisToolOptions>({
  enableTypeInference: true,
  inferByParamName: true,
})

// 获取参数
function getParams() {
  let code = inputRef.value.getText()
  if (hasText(code)) {
    getMapperStatementParams(code, toRaw(options.value)).then((value) => {
      msParamTable.value.setFields(toRaw(value.data))
    })
  } else {
    ElMessage.warning("输入文本为空!")
  }
}

// 获取sql
function getSqlOfMapperStatement(real: boolean) {
  const code = inputRef.value.getText()
  if (!hasText(code)) {
    Message.warn("输入文本为空!")
    return
  }
  apiGetSql(code, msParamTable.value.getFields() || [], real).then((res) => {
    outputRef.value.setText(res.data)
  })
}

function showDialog() {
  importModalRef.value.init()
}

const fillSampleMapperStatement = () => {
  apiGetSampleXmlText().then((res) => {
    inputRef.value.setText(res.data)
  })
}

const inputText = ref<string>('')

const onModalShown = () => {
  nextTick(() => inputRef.value.setText(inputText.value))
}

defineExpose({
  show(content?: string) {
    if (content) {
      inputText.value = content
      visible.value = true
    }
  }
})

</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
}

.el-card {
  min-width: 100%;
  height: 100%;
  // 高度要设置百分比才可以
  margin-right: 20px;
  transition: all 0.5s;
}
</style>
