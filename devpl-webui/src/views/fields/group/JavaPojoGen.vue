<!--
  生成Java pojo类
-->
<template>
  <vxe-modal v-model="visible" title="生成Java Pojo类" width="80%" height="80%">
    <div style="height: 100%;">
      <el-row>
        <el-col :span="8">
          <FieldTree :fields="fields"></FieldTree>
        </el-col>
        <el-col :span="4">
          <div>
            <LanguageSelect />
            <el-form label-position="top">
              <el-form-item label="类名">
                <el-input></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col :span="12">
          <monaco-editor language="java"></monaco-editor>
        </el-col>
      </el-row>
    </div>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListGroupFieldsById } from "@/api/fields";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { nextTick, ref } from "vue";
import FieldTree from "@/components/fields/FieldTree.vue";
import LanguageSelect from "@/components/LanguageSelect.vue";

const tableData = ref();

const visible = ref();

const fields = ref<FieldInfo[]>([]);

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