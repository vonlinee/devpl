<script setup lang="ts">
import { reactive, ref, toRaw } from "vue";
import { Action, ElMessage, ElMessageBox, ElTableColumn } from "element-plus";
import { apiParseFields } from "@/api/fields";
import { isBlank } from "@/utils/tool";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import FieldParserInput from "./FieldParserInput.vue";

const modalVisible = ref();

const init = () => {
  modalVisible.value = true;
};

defineExpose({
  init: init
});

const fields = ref<FieldInfo[]>();

const fieldParserInputRef = ref();

const parseFields = () => {
  const inputType: string = fieldParserInputRef.value.getInputType();
  let text = fieldParserInputRef.value.getParseableText();
  if (isBlank(text)) {
    ElMessage("输入文本为空");
    return;
  }
  let options = fieldParserInputRef.value.getOptions();
  apiParseFields({
    type: inputType,
    content: text,
    ...options
  }).then((res) => {
    const existed = fields.value || [];
    // 合并
    res.data?.fields.forEach((i: any) => {
      if (!existed.find((f) => f.fieldKey == i.fieldKey)) {
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
  fields.value = fields.value?.filter((f) => f.fieldKey != row.fieldKey);
};

</script>

<template>
  <vxe-modal v-model="modalVisible" title="字段解析" :draggable="false" show-footer :mask-closable="false" width="80%"
    height="80%" :z-index="2000" @close="onModalClose">
    <template #default>
      <Splitpanes>
        <Pane>
          <FieldParserInput ref="fieldParserInputRef"></FieldParserInput>
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
.demo-tabs>.el-tabs__content {
  height: 100%;
  overflow-y: scroll;
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
