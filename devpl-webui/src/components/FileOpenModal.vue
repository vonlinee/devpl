<!-- 
  参考Idea的Open File Or Project窗口
 -->
<template>
  <vxe-modal title="选择文件或者项目" width="60%" v-model="visible" @close="onClose(true)" show-footer :draggable="false">
    <div style="display: flex; flex-direction: column; ">
      <el-select v-model="path" clearable filterable allow-create>
        <el-option v-for="hs in historySelections" :label="hs" :value="hs"></el-option>
      </el-select>
      <div style="flex: 1; display: block; overflow-y: scroll; min-height: 400px; height: 400px; max-height: 400px;">
        <FileTreeView ref="fileTreeView" lazy :load="loadNode" @node-click="handleNodeClick"></FileTreeView>
      </div>
    </div>
    <template #footer>
      <el-button type="primary" @click="onClose(false)">确定</el-button>
      <el-button @click="onClose(true)">取消</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListFiles } from '@/api/fileupload';
import { ref, toRaw } from 'vue';
import FileTreeView from "@/components/FileTreeView.vue"
import type Node from 'element-plus/es/components/tree/src/model/node'
import { addAll } from '@/utils/tool';
const visible = ref()
const fileTreeView = ref()

const emits = defineEmits([
  "selected"
])

const path = ref('')

const historySelections = ref<string[]>([])

const onClose = (cancel: boolean) => {
  if (!cancel) {
    const selectedPath = toRaw(path.value)
    historySelections.value.push(selectedPath)
    emits("selected", selectedPath)
  }
  visible.value = false
}

const handleNodeClick = (data: FileNode) => {
  path.value = data.path
}

const loadNode = (node: Node, resolve: (data: FileNode[]) => void) => {
  apiListFiles(node.data.path).then((res) => {
    return resolve(res.data)
  })
}

defineExpose({
  show(paths?: string[]) {
    if (paths) {
      historySelections.value = addAll(historySelections.value, toRaw(paths))
    }
    visible.value = true
  }
})

</script>