<!--
  生成Java pojo类
-->
<template>
  <vxe-modal
    v-model="visible"
    title="生成Java Pojo类"
    width="80%"
    height="90%"
    show-footer
    destroy-on-close
  >
    <div style="height: 100%">
      <el-row>
        <el-col :span="8">
          <FieldTree ref="fieldTreeRef" :fields="fields" selectable></FieldTree>
        </el-col>
        <el-col :span="4">
          <el-form :form="formData" label-position="top">
            <el-form-item>
              <el-select v-model="formData.type">
                <el-option label="Java VO类型" :value="1"></el-option>
                <el-option label="Jackson响应类型" :value="2"></el-option>
                <el-option label="EasyPOI类型" :value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="包名">
              <el-input v-model="formData.packageName"></el-input>
            </el-form-item>
            <el-form-item label="类名">
              <el-input v-model="formData.className"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <CodeRegion ref="outputEditorRef" lang="java"></CodeRegion>
        </el-col>
      </el-row>
    </div>
    <template #footer>
      <el-button type="primary" @click="showInfo">测试</el-button>
      <el-button type="primary" @click="gen">生成</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListGroupFieldsById } from "@/api/fields";
import { toRaw, reactive, ref } from "vue";
import FieldTree from "@/components/fields/FieldTree.vue";
import { apiCodeGenJavaPojo } from "@/api/generator";
import CodeRegion from "@/components/CodeRegion.vue";

const tableData = ref();
const outputEditorRef = ref();

const visible = ref();
const fieldTreeRef = ref();

const fields = ref<FieldInfo[]>([]);

const formData = reactive({
  type: 1,
  packageName: "io.devpl.test",
  className: "Test"
});

const showInfo = () => {
  console.log(fields.value);
};

const gen = () => {
  apiCodeGenJavaPojo({
    ...toRaw(formData),
    fields: fieldTreeRef.value.getFields()
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
