<script setup lang="ts">
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import MonacoEditor from '@/components/editor/MonacoEditor.vue';
import SimpleModal from '@/components/dialog/SimpleModal.vue';

const activeTabName = ref('java')
const modal = ref<typeof SimpleModal>()
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}

const init = () => {
  modal.value?.show()
}

defineExpose({
  init: init
})

</script>

<template>
  <simple-modal ref="modal" title="导入字段">
    <el-tabs v-model="activeTabName" class="demo-tabs" @tab-click="handleClick">
    <el-tab-pane label="Java" name="java">
        <monaco-editor language="java" height="400px"></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="SQL" name="sql">
      <monaco-editor language="sql"></monaco-editor>
    </el-tab-pane>
    <el-tab-pane label="JSON" name="json">JSON</el-tab-pane>
  </el-tabs>
  </simple-modal>
</template>

<style scoped lang="scss">
.demo-tabs>.el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>