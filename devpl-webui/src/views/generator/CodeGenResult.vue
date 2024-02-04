<!-- 
  代码生成结果展示
 -->
<template>
  <vxe-modal
    ref="modalRef"
    v-model="dialogVisiableRef"
    show-footer
    title="生成结果"
    width="80%"
    height="90%"
    transfer
    :mask-closable="false"
    destroy-on-close
    draggable
    show-zoom
    :z-index="5000"
  >
    <Splitpanes>
      <Pane min-size="20" size="35">
        <div
          class="tree-container"
          :style="{
            height: '100%',
            overflowY: 'scroll',
          }"
        >
          <el-tree
            :data="treeData"
            :props="defaultProps"
            default-expand-all
            @node-click="handleFileTreeNodeClick"
          >
            <template #default="{ node, data }">
              <svg-icon :icon="getIconName(data)" />
              <span style="padding-left: 4px">{{ node.label }}</span>
            </template>
          </el-tree>
        </div>
      </Pane>
      <pane>
        <div
          :style="{
            height: '100%',
            overflowY: 'scroll',
          }"
        >
          <monaco-editor ref="editorRef" language="java"></monaco-editor>
        </div>
      </pane>
    </Splitpanes>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { nextTick, ref } from "vue"
import { getLanguage } from "@/components/editor/monaco-editor-wrapper"
import { apiGetFileContent, apiGetFileTree } from "@/api/factory"
import MonacoEditor from "@/components/editor/MonacoEditor.vue"
import SvgIcon from "@/components/svg-icon"
import { Splitpanes, Pane } from "splitpanes"
import "splitpanes/dist/splitpanes.css"

import { getIconName } from "@/utils/tool"

const dialogVisiableRef = ref()
const modalRef = ref()
// 根目录列表
const rootDirRef = ref<string | undefined>()

/**
 * 初始化
 * @param dirs 需要展示的文件夹
 */
function init(dirs: string[]) {
  if (dirs[0] == undefined) {
    return
  }
  rootDirRef.value = dirs[0] || ""
  dialogVisiableRef.value = true
  // 加载文件树
  apiGetFileTree(rootDirRef.value).then((res) => {
    treeData.value = res.data
    nextTick(() => onClickFirstFile(treeData.value))
  })
}

/**
 * 递归展开所有非叶子节点
 * @param fileNode 当前节点
 * @param defaultExpandedKeys 保存展开节点的key，递归遍历节点数，填充key到此数组
 */
function expandAllParentNode(
  fileNode: FileNode,
  defaultExpandedKeys: string[]
) {
  if (!fileNode.isLeaf) {
    return
  }
  defaultExpandedKeys.push(fileNode.key)
  for (let i = 0; i < fileNode.children.length; i++) {
    expandAllParentNode(fileNode.children[i], defaultExpandedKeys)
  }
}

const editorRef = ref()
const treeData = ref<FileNode[]>([])

// 返回数据结构取的字段
const defaultProps = {
  children: "children",
  label: "label",
  id: "key",
}

/**
 * 树节点点击事件
 * 如果是文件节点，调用后台接口获取文件内容
 * 如果是目录节点，直接展开该目录
 * @param fileNode
 * @see FileNode#isLeaf
 */
const handleFileTreeNodeClick = (fileNode: FileNode) => {
  if (fileNode && fileNode.isLeaf) {
    if (fileNode.path !== "") {
      const lang = getLanguage(fileNode.extension)
      apiGetFileContent(fileNode.path).then((res) => {
        if (editorRef.value) {
          editorRef.value.setLanguage(lang)
          editorRef.value.setText(res.data)
        }
      })
    }
  }
}

/**
 * 找到第一个文件节点
 * @param fileNode
 */
let findFirstLeafNode = function (fileNode: FileNode): FileNode | undefined {
  if (fileNode.isLeaf) {
    return fileNode
  }
  let firstNode: FileNode | undefined
  for (let i = 0; i < fileNode.children.length; i++) {
    firstNode = findFirstLeafNode(fileNode.children[i])
    if (fileNode) {
      break
    }
  }
  return firstNode
}

/**
 * 手动模拟选中第一个文件
 * @param fileNodes
 */
let onClickFirstFile = async (fileNodes: FileNode[]) => {
  let node = findFirstLeafNode(fileNodes[0])
  if (node) {
    handleFileTreeNodeClick(node)
  }
}

defineExpose({
  init,
})
</script>

<style lang="scss" scoped></style>
