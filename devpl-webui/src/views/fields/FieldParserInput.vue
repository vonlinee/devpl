<!-- 
    字段解析输入组件
 -->
<script setup lang="ts">
import { reactive, ref } from "vue";
import { TabsPaneContext } from "element-plus";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import LanguageSelector from "@/components/LanguageSelector.vue";

const activeTabName = ref("pl");
const modalVisible = ref();

const handleTabClicked = (tab: TabsPaneContext, event: Event) => {
  activeTabName.value = tab.paneName as string;
};

type MonacoEditorType = typeof MonacoEditor

const plInputType = ref('java')
const htmlInputType = ref('table-dom')
const otherInputType = ref('url')
const jsonInputType = ref('json')

const plEditorRef = ref<MonacoEditorType>();
// 仅支持mysql
const sqlEditorRef = ref<MonacoEditorType>();
// 支持json5
const jsonEditorRef = ref<MonacoEditorType>();
// html文本解析
const htmlEditorRef = ref<MonacoEditorType>();
// 其他
const otherEditorRef = ref<MonacoEditorType>();

/**
 * 解析选项
 */
const jsonOptions = reactive({
  /**
   * 是否解析多层JSON结构
   */
  recursive: false,
  /**
   * 多层JSON结构如何返回 flat(平铺结构返回), tree(树形结构返回)
   */
  resultType: 'flat'
})

/**
 * SQL输入解析选项
 */
const sqlOptions = reactive({
  /**
   * sql语法类型，数据库类型
   */
  dbType: 'mysql',
  /**
   * sql的类型
   */
  sqlType: 'ddl'
})

/**
 * 字段映射规则
 * 指定列的索引号(从1开始)或者列名称与字段含义的对应关系
 */
const htmlParserOptions = reactive({
  fieldNameColumn: "1",
  fieldTypeColumn: "2",
  fieldDescColumn: "3"
})

const emits = defineEmits([
  // 完成
  "finished"
]);

/**
 * 暴露API
 */
defineExpose({
  init: () => {
    modalVisible.value = true;
  },
  /**
   * 获取待解析的文本
   */
  getParseableText() {
    let inputType: string = activeTabName.value;
    let text = "";
    switch (inputType) {
      case "pl":
        text = plEditorRef.value?.getText();
        break;
      case "sql":
        text = sqlEditorRef.value?.getText();
        break;
      case "json":
        text = jsonEditorRef.value?.getText();
        break;
      case "html":
        text = htmlEditorRef.value?.getText();
        break;
      case "other":
        text = otherEditorRef.value?.getText();
        break;
      default:
        break;
    }
    return text;
  },
  /**
   * 输入类型
   * 二级分类:第一级标签名称，第二级每个标签内的选项值
   */
  getInputType() {
    let tabName: string = activeTabName.value;
    let categoryName = undefined
    if (tabName == 'pl') {
      categoryName = plInputType.value;
    } else if (tabName == 'html') {
      categoryName = htmlInputType.value;
    } else if (tabName == 'other') {
      categoryName = otherInputType.value;
    } else if (tabName == 'sql') {
      categoryName = tabName;
    } else if (tabName == 'json') {
      categoryName = jsonInputType.value
    }
    return tabName + ">" + categoryName;
  },
  /**
   * 获取对应输入类型的选项
   */
  getOptions() {
    let tabName: string = activeTabName.value;
    if (tabName == 'pl') {
      return {}
    } else if (tabName == 'html') {
      return htmlParserOptions
    } else if (tabName == 'json') {
      return jsonOptions
    } else if (tabName == 'sql') {
      return sqlOptions
    }
    return {};
  }
});

</script>

<template>
  <el-tabs v-model="activeTabName" class="input-tabs" @tab-click="handleTabClicked">
    <el-tab-pane label="Language" name="pl">
      <div style="display: flex; flex-direction: column; height: 100%;">
        <LanguageSelector @selection-change="(val) => plInputType = val"></LanguageSelector>
        <div style="flex-grow: 1;">
          <monaco-editor ref="plEditorRef" language="java" />
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="SQL" name="sql">
      <div style="display: flex; flex-direction: column; height: 100%;">
        <el-form :form="sqlOptions" inline>
          <el-form-item>
            <el-select v-model="sqlOptions.dbType">
              <!-- 暂时只支持MySQL -->
              <el-option label="MySQL" value="mysql"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="sqlOptions.sqlType">
              <el-option label="DDL(建表SQL)" value="ddl"></el-option>
              <el-option label="QML(查询SQL)" value="qml"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div style="flex-grow: 1;">
          <monaco-editor ref="sqlEditorRef" language="sql"></monaco-editor>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="JSON" name="json">
      <div style="display: flex; flex-direction: column; height: 100%;">
        <el-card>
          <el-form :form="jsonOptions" label-width="100px" label-position="left">
            <el-form-item label="语法格式">
              <el-select v-model="jsonInputType">
                <el-option label="JSON5" value="json5"></el-option>
                <el-option label="JSON" value="json"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="嵌套解析">
              <el-switch v-model="jsonOptions.recursive" size="small" />
            </el-form-item>
            <el-form-item label="结果类型">
              <el-radio-group v-model="jsonOptions.resultType" class="ml-4">
                <el-radio label="flat">平铺</el-radio>
                <el-radio label="tree">树形结构</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-card>
        <div style="flex-grow: 1;">
          <monaco-editor ref="jsonEditorRef" language="json"></monaco-editor>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="HTML" name="html">
      <div style="display: flex; flex-direction: column; height: 100%;">
        <el-select v-model="htmlInputType">
          <el-option label="HTML Table Dom" value="table-dom"></el-option>
          <el-option label="HTML Table Text" value="table-text"></el-option>
        </el-select>
        <el-card>
          <el-form :form="htmlParserOptions" label-width="150" inline label-position="left">
            <el-form-item label="字段名称列">
              <el-input v-model="htmlParserOptions.fieldNameColumn"></el-input>
            </el-form-item>
            <el-form-item label="字段数据类型列">
              <el-input v-model="htmlParserOptions.fieldTypeColumn"></el-input>
            </el-form-item>
            <el-form-item label="字段描述信息列">
              <el-input v-model="htmlParserOptions.fieldDescColumn"></el-input>
            </el-form-item>
          </el-form>
        </el-card>
        <div style="flex-grow: 1;">
          <monaco-editor ref="htmlEditorRef" language="text"></monaco-editor>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="其他" name="other">
      <div style="display: flex; flex-direction: column; height: 100%;">
        <el-select v-model="otherInputType">
          <el-option label="URL" value="url"></el-option>
        </el-select>
        <div style="flex-grow: 1;">
          <monaco-editor ref="otherEditorRef" language="html"></monaco-editor>
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<style  lang="scss">
.input-tabs {
  height: 100%;

  .el-tabs__content {
    height: calc(100% - 55px);
    overflow-y: auto;
  }

  .el-tab-pane {
    height: 100%;
  }

  .el-card-define {
    min-height: 100%;
    height: 100%;
  }

  .el-card-define>>>.el-card__body {
    height: 100%;
  }
}
</style>
