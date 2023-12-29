<!--
  生成Java pojo类
-->
<template>
  <vxe-modal v-model="visible" title="生成Java Pojo类" width="80%" height="80%" show-footer>
    <div style="height: 100%;">
      <el-row>
        <el-col :span="8">
          <FieldTree :fields="fields"></FieldTree>
        </el-col>
        <el-col :span="4">
          <div>
            <LanguageSelect />
            <el-form :form="formData" label-position="top">
              <el-form-item label="包名">
                <el-input v-model="formData.packageName"></el-input>
              </el-form-item>
              <el-form-item label="类名">
                <el-input v-model="formData.className"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col :span="12">
          <monaco-editor ref="outputEditorRef" language="java"></monaco-editor>
        </el-col>
      </el-row>
    </div>
    <template #footer>
      <el-button type="primary" @click="gen">生成</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListGroupFieldsById } from "@/api/fields";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { toRaw, reactive, ref } from "vue";
import FieldTree from "@/components/fields/FieldTree.vue";
import LanguageSelect from "@/components/LanguageSelect.vue";
import { apiCodeGenJavaPojo } from "@/api/generator";

const tableData = ref();
const outputEditorRef = ref();

const visible = ref();

const fields = ref<FieldInfo[]>([]);

const formData = reactive({
  type: 3,
  packageName: "io.devpl.test",
  className: "Test"
});

const gen = () => {
  apiCodeGenJavaPojo({
    ...toRaw(formData),
    fields: fields.value
  }).then((res) => {
    outputEditorRef.value.setText(res.data);
  });
};

defineExpose({
  show(groupId: number) {
    apiListGroupFieldsById(groupId).then((res) => {
      fields.value = res.data;
      visible.value = true;
    });
  }
});

</script>

<style lang="scss" scoped></style>