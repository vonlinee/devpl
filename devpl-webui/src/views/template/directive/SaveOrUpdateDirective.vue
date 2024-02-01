<template>
  <vxe-modal v-model="visible" title="编辑或修改自定义指令" show-footer height="80%" width="80%">
    <div style="display: flex; flex-direction: column; height: 100%">
      <el-card>
        <el-form :model="formObject" label-position="left" label-width="100px">
          <el-form-item label="指令名称">
            <el-input v-model="formObject.directiveName" placeholder="输入指令名称" clearable />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="formObject.remark" placeholder="备注" clearable />
          </el-form-item>
        </el-form>
      </el-card>
      <div style="flex: 1">
        <monaco-editor ref="editorRef" language="java"></monaco-editor>
      </div>
    </div>

    <template #footer>
      <el-button @click="submit" type="primary">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

const visible = ref();
const editorRef = ref();

const formObject = reactive<CustomDirective>({
  directiveId: "",
  directiveName: "",
  sourceCode: "",
  remark: ""
});

defineExpose({
  show(data?: CustomDirective) {
    if (data) {
      formObject.directiveId = data.directiveId;
      formObject.directiveName = data.directiveName;
      formObject.sourceCode = data.sourceCode;
      formObject.remark = data.remark;
      editorRef.value.setText(data.sourceCode);
    }
    visible.value = true;
  }
});

const submit = () => {

};

</script>