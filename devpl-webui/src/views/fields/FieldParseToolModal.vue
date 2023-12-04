<script setup lang="ts">
import { reactive, ref, toRaw } from "vue";
import { ElMessage, ElTableColumn, type TabsPaneContext } from "element-plus";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { apiParseFields} from "@/api/fields";
import { isBlank } from "@/utils/tool";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

const activeTabName = ref("java");
const modalVisible = ref();

const handleTabClicked = (tab: TabsPaneContext, event: Event) => {

};

const init = () => {
  modalVisible.value = true;
};

defineExpose({
  init: init
});

const fields = ref<FieldInfo[]>();

type MonacoEditorType = typeof MonacoEditor;

const javaEditorRef = ref<MonacoEditorType>();
// 仅支持mysql
const sqlEditorRef = ref<MonacoEditorType>();
// 支持json5
const jsonEditorRef = ref<MonacoEditorType>();
// TS/JS
const tsOrJsEditorRef = ref<MonacoEditorType>();
// html文本解析
const html1EditorRef = ref<MonacoEditorType>();
// html dom文本解析
const html2EditorRef = ref<MonacoEditorType>();

const parseFields = () => {
  const inputType: string = activeTabName.value;
  let text = "";
  switch (inputType) {
    case "java":
      text = javaEditorRef.value?.getText();
      break;
    case "sql":
      text = sqlEditorRef.value?.getText();
      break;
    case "json":
      text = jsonEditorRef.value?.getText();
      break;
    case "ts/js":
      text = tsOrJsEditorRef.value?.getText();
      break;
    case "html1":
      text = html1EditorRef.value?.getText();
      break;
    case "html2":
      text = html2EditorRef.value?.getText();
      break;
    default:
      break;
  }
  if (isBlank(text)) {
    ElMessage("输入文本为空");
    return;
  }
  apiParseFields({
    type: inputType, content: text,
    ...toRaw(columnMappingForm)
  }).then((res) => {
    const existed = fields.value || [];
    res.data?.forEach(i => {
      if (!existed.find(f => f.fieldKey == i.fieldKey)) {
        existed?.push(i);
      }
    });
    fields.value = existed;
  });
};

const emits = defineEmits([
  // 完成
  "finished"
]);

const onModalClose = () => {
  emits("finished", fields.value);
};

/**
 * 删除行
 * @param row
 */
const removeRow = (row: FieldInfo) => {
  fields.value = fields.value?.filter(f => f.fieldKey != row.fieldKey);
};

/**
 * 字段映射规则
 * 指定列的索引号(从1开始)或者列名称与字段含义的对应关系
 */
const columnMappingForm = reactive({
  fieldNameColumn: "1",
  fieldTypeColumn: "2",
  fieldDescColumn: "3"
});

</script>

<template>
  <vxe-modal v-model="modalVisible" width="80%" height="80%" title="字段解析" show-footer :mask-closable="false"
             :z-index="2000"
             @close="onModalClose">
    <template #default>
      <Splitpanes>
        <Pane>
          <el-tabs v-model="activeTabName" class="demo-tabs" @tab-click="handleTabClicked" height="600px">
            <el-tab-pane label="Java" name="java">
              <monaco-editor ref="javaEditorRef" language="java" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="SQL" name="sql">
              <monaco-editor ref="sqlEditorRef" language="sql" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="JSON" name="json">
              <monaco-editor ref="jsonEditorRef" language="json" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="TS/JS" name="ts/js">
              <monaco-editor ref="jsonEditorRef" language="json" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="HTML文本" name="html1">
              <monaco-editor ref="html1EditorRef" language="text" height="480px"></monaco-editor>
              <el-card>
                <el-form :form="columnMappingForm" label-width="150" label-position="left">
                  <el-form-item label="字段名称列">
                    <el-input v-model="columnMappingForm.fieldNameColumn"></el-input>
                  </el-form-item>
                  <el-form-item label="字段数据类型列">
                    <el-input v-model="columnMappingForm.fieldTypeColumn"></el-input>
                  </el-form-item>
                  <el-form-item label="字段描述信息列">
                    <el-input v-model="columnMappingForm.fieldDescColumn"></el-input>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-tab-pane>
            <el-tab-pane label="HTML Dom" name="html2">
              <monaco-editor ref="html2EditorRef" language="html" height="480px"></monaco-editor>
              <el-card>
                <el-form :form="columnMappingForm" label-width="150" label-position="left">
                  <el-form-item label="字段名称列">
                    <el-input v-model="columnMappingForm.fieldNameColumn"></el-input>
                  </el-form-item>
                  <el-form-item label="字段数据类型列">
                    <el-input v-model="columnMappingForm.fieldTypeColumn"></el-input>
                  </el-form-item>
                  <el-form-item label="字段描述信息列">
                    <el-input v-model="columnMappingForm.fieldDescColumn"></el-input>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-tab-pane>
          </el-tabs>
        </Pane>
        <Pane>
          <el-table :data="fields" border height="100%">
            <ElTableColumn label="名称" prop="fieldKey" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="数据类型" prop="dataType" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="默认值" prop="defaultValue" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="描述信息" prop="description" show-overflow-tooltip></ElTableColumn>
            <el-table-column label="操作" fixed="right" align="center">
              <template #default="scope">
                <el-button link @click="removeRow(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </Pane>
      </Splitpanes>
    </template>
    <template #footer>
      <vxe-button status="primary" @click="parseFields">解析</vxe-button>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss">
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>