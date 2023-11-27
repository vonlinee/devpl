<script setup lang="ts">
import SelectiveImport from "@/components/SelectiveImport.vue";
import { ref } from "vue";
import { ElButton } from "element-plus";

let showRef = ref(false);

let overrideMode = ref(1);

// 覆盖模式: 1-智能合并, 2-完全覆盖
let overrideModeOptions = ref([
  {
    label: "智能合并",
    value: 1
  }, {
    label: "完全覆盖",
    value: 2
  }
]);

const emits = defineEmits([
  "onSubmit"
]);

let importTabPane = ref();

/**
 * 提交更改
 */
function submit() {
  let tabName = importTabPane.value.getSelectedTab();
  let content = importTabPane.value.getContent();
  emits('onSubmit', tabName, content)
}

defineExpose({
  init: () => {
    showRef.value = true
  }
})

</script>

<template>
  <vxe-modal size="small" title="参数导入" v-model="showRef" :mask="false" :show-footer="true" :z-index="2000"
             :width="800">
    <template #default>
      <selective-import ref="importTabPane"></selective-import>
    </template>
    <template #footer>
      <el-row align="middle" style="margin-bottom: 1px">
        <el-col :span="2">
          <el-text class="mx-1">覆盖模式</el-text>
        </el-col>
        <el-col :span="8" :offset="1">
          <span style="display: flex">
            <el-select v-model="overrideMode" class="m-2">
              <el-option v-for="item in overrideModeOptions" :key="item.label" :label="item.label"
                         :value="item.value" />
            </el-select></span>
        </el-col>
        <el-col :span="12">
          <el-button type="primary" @click="submit">确定</el-button>
        </el-col>
      </el-row>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
