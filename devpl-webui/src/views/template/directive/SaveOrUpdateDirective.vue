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
      <el-button @click="getSample">获取示例</el-button>
      <el-button @click="submit" type="primary">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref, toRaw, nextTick } from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { apiAddCustomTemplateDirective, apiGetCustomTemplateDirectiveExample } from "@/api/template";
import { Message } from "@/hooks/message";
import { base64Encode, hasText } from "@/utils/tool";

const visible = ref();
const editorRef = ref();

const formObject = reactive<CustomDirective>({
  directiveId: undefined,
  directiveName: "",
  sourceCode: "",
  remark: ""
});

const emits = defineEmits([
  "submit"
])

const submit = () => {
  if (editorRef.value) {
    formObject.sourceCode = editorRef.value.getText()
  }

  if (!hasText(formObject.sourceCode)) {
    Message.error("请输入指令实现代码")
    return;
  }
  const param = Object.assign({}, toRaw(formObject))
  param.sourceCode = base64Encode(param.sourceCode)
  apiAddCustomTemplateDirective(param).then((res) => {
    Message.info("操作成功")
    visible.value = false
    emits("submit")
  })
}

defineExpose({
  show(data?: CustomDirective) {
    if (data) {
      formObject.directiveId = data.directiveId;
      formObject.directiveName = data.directiveName;
      formObject.sourceCode = data.sourceCode;
      formObject.remark = data.remark;
      visible.value = true;
      if (hasText(data.sourceCode)) {
        nextTick(() => editorRef.value.setText(data.sourceCode));
      }
    } else {
      formObject.directiveId = undefined
      formObject.directiveName = "";
      formObject.sourceCode = "";
      formObject.remark = "";
      nextTick(() => editorRef.value.setText(""));
      visible.value = true;
    }
  }
});

const getSample = () => {
  apiGetCustomTemplateDirectiveExample().then((res) => editorRef.value.setText(res.data));
};
</script>