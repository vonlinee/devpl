<script setup lang="ts">
import { ref } from "vue";
import { ElTableColumn, type TabsPaneContext } from "element-plus";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { apiParseFields } from "@/api/fields";
import { isBlank } from "@/utils/tool";
import { Splitpanes, Pane } from "splitpanes";
import 'splitpanes/dist/splitpanes.css'
const activeTabName = ref("java");
const modalVisible = ref();

const handleClick = (tab: TabsPaneContext, event: Event) => {

};

const init = () => {
  modalVisible.value = true;
};

defineExpose({
  init: init
});

const fields = ref<FieldInfo[]>();

const javaEditorRef = ref<typeof MonacoEditor>();
// 仅支持mysql
const sqlEditorRef = ref<typeof MonacoEditor>();
// 支持json5
const jsonEditorRef = ref<typeof MonacoEditor>();
const submit = () => {
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
    default:
      break;
  }
  if (isBlank(text)) {
    alert("输入文本为空");
  }
  apiParseFields({ type: inputType, content: text }).then((res) => {
    fields.value = res.data;
  });
};
</script>

<template>
  <vxe-modal v-model="modalVisible" width="80%" title="导入字段" show-footer :mask-closable="false" :z-index="2000">
    <template #default>
      <Splitpanes>
        <Pane>
          <el-tabs v-model="activeTabName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="Java" name="java">
              <monaco-editor ref="javaEditorRef" language="java" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="SQL" name="sql">
              <monaco-editor ref="sqlEditorRef" language="sql" height="480px"></monaco-editor>
            </el-tab-pane>
            <el-tab-pane label="JSON" name="json">
              <monaco-editor ref="jsonEditorRef" language="json" height="480px"></monaco-editor>
            </el-tab-pane>
          </el-tabs>
        </Pane>
        <Pane>
          <el-table :data="fields" border height="100%">
            <ElTableColumn label="名称" prop="fieldKey" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="数据类型" prop="dataType" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="默认值" prop="defaultValue" show-overflow-tooltip></ElTableColumn>
            <ElTableColumn label="描述信息" prop="description" show-overflow-tooltip></ElTableColumn>
          </el-table>
        </Pane>
      </Splitpanes>
    </template>
    <template #footer>
      <vxe-button @click="modalVisible = false">取消</vxe-button>
      <vxe-button status="primary" @click="submit">确定</vxe-button>
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