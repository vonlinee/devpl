<!-- 
  参考Idea的Open File Or Project窗口
 -->
<template>
  <vxe-modal title="选择文件或者项目" width="60%" v-model="visible" @show="init" @close="onClose(true)" show-footer
    destroy-on-close>
    <div style="display: flex; flex-direction: column; ">
      <el-select v-model="path" clearable>
        <el-option v-for="hs in historySelections" :label="hs" :value="hs"></el-option>
      </el-select>
      <div style="flex: 1; display: block; overflow-y: scroll; min-height: 400px; height: 400px; max-height: 400px;">
        <el-tree :data="data" :load="loadNode" :props="defaultProps" lazy @node-click="handleNodeClick">
        </el-tree>
      </div>
    </div>
    <template #footer>
      <el-button type="primary" @click="onClose(false)">确定</el-button>
      <el-button @click="onClose(true)">取消</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiGetFileSystemTree } from '@/api/fileupload';
import { ref, toRaw } from 'vue';
import type Node from 'element-plus/es/components/tree/src/model/node'
const defaultProps = {
  children: 'children',
  label: 'name',
  isLeaf: 'leaf',
}

const visible = ref()

const data = ref()

interface FileItem {
  name: string
  absolutePath: string
  type: string
  children?: FileItem[]
  leaf?: boolean
}

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

const handleNodeClick = (data: FileItem) => {
  path.value = data.absolutePath
}

const loadNode = (node: Node, resolve: (data: FileItem[]) => void) => {
  apiGetFileSystemTree(node.data.absolutePath).then((res) => {
    return resolve(res.data)
  })
}

/**
 * 初始化
 */
const init = () => {
  apiGetFileSystemTree().then((res) => {
    data.value = res.data
  })
}

defineExpose({
  show() {
    init()
    visible.value = true
  }
})

</script>