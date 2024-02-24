<!-- 
  文件目录树形结构，带文件图标，配合后端接口使用
 -->
<template>
  <el-tree :data="treeData" :props="defaultProps" @node-click="handleFileTreeNodeClick" :load="load" :lazy="lazy">
    <template #default="{ node, data }">
      <svg-icon :icon="getIconName(data)" />
      <span style="padding-left: 4px">{{ node.label }}</span>
    </template>
  </el-tree>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { getIconName } from "@/utils/tool"
import { LoadFunction } from 'element-plus/es/components/tree/src/tree.type';

const treeData = ref<FileNode[]>([])

// 返回数据结构取的字段
const defaultProps = {
  children: "children",
  label: "label",
  id: "key",
  isLeaf: "leaf"
}

/**
 * 懒加载不要配合default-expand-all使用，否则会调多次接口
 */

const { lazy, load } = withDefaults(defineProps<{
  lazy?: boolean,
  load?: LoadFunction
}>(), {
  lazy: false,
  load: undefined
})

const emits = defineEmits<{
  /**
   * 节点单击事件
   * @param node 文件节点
   */
  (e: 'node-click', node: FileNode): void
}>()

defineExpose({
  setData(data: FileNode[]) {
    treeData.value = data
  }
})

/**
 * 树节点点击事件
 * 如果是文件节点，调用后台接口获取文件内容
 * 如果是目录节点，直接展开该目录
 * @param fileNode
 * @see FileNode#isLeaf
 */
const handleFileTreeNodeClick = (fileNode: FileNode) => {
  emits("node-click", fileNode)
}

</script>