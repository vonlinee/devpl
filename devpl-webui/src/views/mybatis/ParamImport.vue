<script setup lang="ts">
import SelectiveImport from "@/components/SelectiveImport.vue";
import { ref } from "vue";
import { ElButton, ElDialog } from "element-plus";

let showRef = ref(false);

function showDialog() {
  showRef.value = true;
}

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

let importTabPane = ref();

function submit() {
  let tabName = importTabPane.value.getSelectedTab();
  let content = importTabPane.value.getContent();

  console.log(tabName, content);
}

</script>

<template>
  <el-button @click="showDialog()">导入</el-button>

  <vxe-modal v-model="showRef">
    <template #default>
      <selective-import ref="importTabPane"></selective-import>
    </template>
    <template #footer>
             <span style="display: flex">
             <el-text class="mx-1">覆盖模式</el-text>
                <el-select v-model="overrideMode" class="m-2" placeholder="Select" size="large">
                    <el-option
                      v-for="item in overrideModeOptions"
                      :key="item.label"
                      :label="item.label"
                      :value="item.value"
                    />
                </el-select></span>
      <el-button type="primary" @click="showRef = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss">

</style>
