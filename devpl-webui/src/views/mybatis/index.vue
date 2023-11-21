<template>
  <el-row>
    <el-col :span="12">
      <splitpanes horizontal>
        <pane min-size="20"><monaco-editor ref="inputRef" language="xml" height="400px" /></pane>
        <pane min-size="20"><monaco-editor ref="sqlRef" language="sql" height="400px" /></pane>
      </splitpanes>
    </el-col>
    <el-col :span="12">
      <splitpanes horizontal>
        <pane>
          <el-button @click="fillSampleMapperStatement">填充样例</el-button>
          <el-button @click="showDialog()">导入</el-button>
          <param-import ref="importModalRef"></param-import>
          <vxe-table show-overflow ref="msParamTable" :border="true" height="400px" row-key header-align="center"
            :data="mapperParams" :checkbox-config="{ checkStrictly: true }" :tree-config="{ transform: true }"
            :edit-config="editConfig">
            <vxe-column field="name" title="参数名" tree-node></vxe-column>
            <vxe-column field="value" title="参数值" :edit-render="{ name: 'input' }"></vxe-column>
            <vxe-column field="dataType" title="类型" :edit-render="{}" :width="130" align="center">
              <template #default="{ row }">
                <span>{{ row.type }}</span>
              </template>
              <template #edit="{ row }">
                <vxe-select v-model="row.type" clearable transfer>
                  <vxe-option v-for="item in javaDataTypes" :key="item.value" :value="item.name"
                    :label="item.label"></vxe-option>
                </vxe-select>
              </template>
            </vxe-column>
          </vxe-table>
          <el-button type="primary" @click="getParams()">解析参数</el-button>
          <el-button type="primary" @click="getSqlOfMapperStatement(false)">获取预编译sql</el-button>
          <el-button type="primary" @click="getSqlOfMapperStatement(true)">获取实际sql</el-button>
        </pane>
        <pane>
          <el-card>
            <el-checkbox v-model="options.enableTypeInference" label="开启类型推断" size="large" />
          </el-card>
        </pane>
      </splitpanes>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { hasText } from "@/utils/tool";
import { computed, nextTick, onMounted, reactive, ref, toRaw } from "vue";
import { Splitpanes, Pane } from 'splitpanes'

import { apiGetDataTypes, apiGetSampleXmlText, apiGetSql, getMapperStatementParams } from "@/api/mybatis";
import { ElButton, ElMessage } from "element-plus";
import ParamImport from "@/views/mybatis/ParamImport.vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { appStore } from "@/store";
import { VxeGridConstructor, VxeTableConstructor, VxeTableDefines, VxeTablePrivateMethods, VxeTablePropTypes } from "vxe-table/types/all";

// 数据
const inputRef = ref();
const sqlRef = ref();
const importModalRef = ref();

// 表格实例
const msParamTable = ref();
const javaDataTypes = ref();

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
    return params.row.leaf != null && params.row.leaf;
  }
});

onMounted(() => {
  apiGetDataTypes().then((res) => {
    javaDataTypes.value = res.data;
  });
});

const store = appStore();

const ms = computed(() => store.ms);

/**
 * 参数节点
 */
interface MsParamNode {
  id: number
  leaf: boolean,
  name: string,
  parentId: number | undefined,
  type: string,
  value: string,
  valueType: string
}

/**
 * 类型推断规则
 */
interface MyBatisToolOptions {
  // 开启自动类型推断，不一定准确
  enableTypeInference: true
}

// mybatis mapper语句参数
const mapperParams = ref<MsParamNode[]>();

const options = ref<MyBatisToolOptions>({
  enableTypeInference: true
})

const expandAll = () => {
  const $table = msParamTable.value;
  if ($table) {
    $table.setAllTreeExpand(true);
  }
};

// 获取参数
function getParams() {
  let code = inputRef.value.getText();
  if (hasText(code)) {
    getMapperStatementParams(code, toRaw(options.value)).then(value => {
      nextTick(() => mapperParams.value = value.data)

      msParamTable.value.loadData(toRaw(value.data));

    }).then(() => expandAll());
  } else {
    ElMessage.warning("输入文本为空!");
  }
}

// 获取sql
function getSqlOfMapperStatement(real: boolean) {
  const code = inputRef.value.getText();
  if (!hasText(code)) {
    ElMessage.warning("输入文本为空!");
    return;
  }
  apiGetSql(code, mapperParams.value || [], real).then(res => {
    sqlRef.value.setText(res.data);
  });
}

function showDialog() {
  importModalRef.value.init()
}

const fillSampleMapperStatement = () => {
  apiGetSampleXmlText().then((res) => {
    if (res.code == 2000) {
      inputRef.value.setText(res.data)
    }
  })
}

</script>

<style lang="scss">
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
