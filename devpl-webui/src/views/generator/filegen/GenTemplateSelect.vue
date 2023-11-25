<template>
  <vxe-modal width="60%" v-model="visible" title="选择模板" :draggable="false" :mask-closable="false" :z-index="2000"
             show-footer @onclose="handleModalClose">
    <el-table ref="tableRef" :data="templateOptions" height="500px" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" header-align="center" align="center" width="40"></el-table-column>
      <el-table-column prop="templateName" label="模板名称" header-align="center" align="center"
                       width="300"></el-table-column>
      <el-table-column prop="remark" label="描述信息" header-align="center" align="center"></el-table-column>
    </el-table>
    <template #footer="scope">
      <el-button type="primary" @click="handleSubmit()">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListSelectableTemplates } from "@/api/template";
import { ElMessage } from "element-plus";
import { onMounted, ref, toRaw } from "vue";

const visible = ref(false);
const tableRef = ref();
const templateOptions = ref<TemplateSelectVO[]>();
onMounted(() => {
  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data || [];
  });
});

const selectedTemplates = ref<number[]>([]);

const handleSelectionChange = (selections: any[]) => {
  selectedTemplates.value = selections;
};

defineExpose({
  show: () => {
    visible.value = true;
  }
});

const emits = defineEmits([
  "selection-callback"
]);

const handleSubmit = () => {
  if (selectedTemplates.value.length == 0) {
    ElMessage.warning("未选择模板");
    return;
  }
  visible.value = false;
  emits("selection-callback", selectedTemplates.value.map((item) => toRaw(item)));
};

const handleModalClose = () => {
  tableRef.value.clearSelection();
};
</script>
