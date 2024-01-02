<template>
  <div style="height: 100% !important; display: flex; flex-direction: row;">
    <div style="display: flex; flex-direction: column; height: 100%; width: 800px;">
      <div style="flex-grow: 1; height: 400px;">
        <monaco-editor ref="inputRef" language="xml" />
      </div>
      <div style="height: 450px;">
        <monaco-editor ref="outputRef" language="sql" />
      </div>
    </div>

    <div style="flex-grow: 1;">
      <el-button @click="fillSampleMapperStatement">填充样例</el-button>
      <el-button @click="showDialog()">导入</el-button>
      <param-import ref="importModalRef"></param-import>
      <vxe-table ref="msParamTable" show-overflow border height="500px" row-key header-align="center"
        :data="mapperParams" :checkbox-config="{ checkStrictly: true }" :tree-config="{ transform: true }"
        :edit-config="editConfig">
        <vxe-column field="name" title="参数名" tree-node></vxe-column>
        <vxe-column field="value" title="参数值" :edit-render="{ name: 'input' }"></vxe-column>
        <vxe-column field="dataType" title="类型" :edit-render="{}" :width="130" align="center">
          <template #default="{ row }">
            <span>{{ row.dataType }}</span>
          </template>
          <template #edit="{ row }">
            <vxe-select v-model="row.dataType" clearable transfer>
              <vxe-option v-for="item in msParamValueTypes" :key="item.key" :value="item.value"
                :label="item.label"></vxe-option>
            </vxe-select>
          </template>
        </vxe-column>
      </vxe-table>
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
</template>

<script setup lang="ts">
import { getSubStrings, hasText, isBlank } from "@/utils/tool"
import { computed, nextTick, onMounted, reactive, ref, toRaw } from "vue"

import {
  apiGetMapperStatementValueTypes,
  apiGetSampleXmlText,
  apiGetSql,
  getMapperStatementParams,
} from "@/api/mybatis"
import { ElButton, ElMessage } from "element-plus"

import ParamImport from "./ParamImport.vue"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import { appStore } from "@/store"
import {
  VxeGridConstructor,
  VxeTableConstructor,
  VxeTableDefines,
  VxeTablePrivateMethods,
  VxeTablePropTypes,
} from "vxe-table/types/all"
import { foreachSnippet, stringSnippet } from "@/utils/mybatis"
import { ArrowDown } from "@element-plus/icons"

// 数据
const inputRef = ref()
const outputRef = ref()
const importModalRef = ref()

const editorHeight = ref('400')

// 表格实例
const msParamTable = ref()
const msParamValueTypes = ref()

type RowModel = any

const editConfig = reactive<VxeTablePropTypes.EditConfig<RowModel>>({
  trigger: "click",
  mode: "cell",
  beforeEditMethod: (params: {
    row: RowModel
    rowIndex: number
    column: VxeTableDefines.ColumnInfo<RowModel>
    columnIndex: number
    $table: VxeTableConstructor<RowModel> & VxeTablePrivateMethods<RowModel>
    $grid: VxeGridConstructor<RowModel> | null | undefined
  }) => {
    // 只有叶子结点可编辑
    return params.row.leaf != null && params.row.leaf
  },
})

onMounted(() => {
  const layoutCard: any = document.querySelector(".layout-card")
  if (layoutCard) {
    editorHeight.value = Math.floor(layoutCard.clientHeight / 2) + 'px'
  }

  apiGetMapperStatementValueTypes().then((res) => {
    msParamValueTypes.value = res.data
  })
})

const store = appStore()

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

const ms = computed(() => store.ms)

/**
 * 参数节点
 */
interface MsParamNode {
  id: number
  leaf: boolean
  name: string
  parentId: number | undefined
  type: string
  value: string
  valueType: string
}

/**
 * 类型推断规则
 */
interface MyBatisToolOptions {
  // 开启自动类型推断，不一定准确
  enableTypeInference: boolean
  inferByParamName: boolean
}

// mybatis mapper语句参数
const mapperParams = ref<MsParamNode[]>()

const options = ref<MyBatisToolOptions>({
  enableTypeInference: true,
  inferByParamName: true,
})

const expandAll = () => {
  const $table = msParamTable.value
  if ($table) {
    $table.setAllTreeExpand(true)
  }
}

// 获取参数
function getParams() {
  let code = inputRef.value.getText()
  if (hasText(code)) {
    getMapperStatementParams(code, toRaw(options.value))
      .then((value) => {
        nextTick(() => (mapperParams.value = value.data))
        msParamTable.value.loadData(toRaw(value.data))
      })
      .then(() => expandAll())
  } else {
    ElMessage.warning("输入文本为空!")
  }
}

// 获取sql
function getSqlOfMapperStatement(real: boolean) {
  const code = inputRef.value.getText()
  if (!hasText(code)) {
    ElMessage.warning("输入文本为空!")
    return
  }
  apiGetSql(code, mapperParams.value || [], real).then((res) => {
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
