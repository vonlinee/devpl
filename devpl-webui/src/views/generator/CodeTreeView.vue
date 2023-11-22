<template>
  <splitpanes class="default-theme">
    <pane max-size="35">
      <div class="tree-container">
        <el-tree :data="treeData" :props="defaultProps" default-expand-all @node-click="handleFileTreeNodeClick">
          <template v-slot="{ node, data }">
            <svg-icon v-if="!data.isLeaf" icon="directory" />
            <span style="padding-left: 4px;">{{ node.label }}</span>
          </template>
        </el-tree>
      </div>
    </pane>
    <pane>
      <div style="height: 600px;">
        <monaco-editor ref="editorRef" language="java"></monaco-editor>
      </div>
    </pane>
  </splitpanes>
</template>

<script lang="ts" setup>
import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import { nextTick, onMounted, ref, toRefs } from "vue";
import { getLanguage } from "@/components/editor/monaco-editor";
import { apiGetFileContent, apiGetFileTree } from "@/api/factory";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import svgIcon from "@/components/svg-icon";

/**
 * 递归展开所有非叶子节点
 * @param fileNode 当前节点
 * @param defaultExpandedKeys 保存展开节点的key，递归遍历节点数，填充key到此数组
 */
function expandAllParentNode(fileNode: FileNode, defaultExpandedKeys: string[]) {
  if (!fileNode.isLeaf) {
    return;
  }
  defaultExpandedKeys.push(fileNode.key);
  for (let i = 0; i < fileNode.children.length; i++) {
    expandAllParentNode(fileNode.children[i], defaultExpandedKeys);
  }
}

type Props = {
  // 展示该目录下的所有文件
  dir: string
}

const { dir } = defineProps<Props>()

const editorRef = ref();
const treeData = ref<FileNode[]>([]);

// 返回数据结构取的字段
const defaultProps = {
  children: "children",
  label: "label",
  id: "key"
};

/**
 * 树节点点击事件
 * 如果是文件节点，调用后台接口获取文件内容
 * 如果是目录节点，直接展开该目录
 * @param fileNode
 * @see FileNode#isLeaf
 */
let handleFileTreeNodeClick = (fileNode: FileNode) => {
  if (fileNode && fileNode.isLeaf) {
    const lang = getLanguage(fileNode.extension);
    apiGetFileContent(fileNode.path).then(res => {
      editorRef.value.setLanguage(lang);
      editorRef.value.setText(res.data);
    });
  }
};

/**
 * 找到第一个文件节点
 * @param fileNode
 */
let findFirstLeafNode = function (fileNode: FileNode): FileNode | undefined {
  if (fileNode.isLeaf) {
    return fileNode;
  }
  let firstNode: FileNode | undefined;
  for (let i = 0; i < fileNode.children.length; i++) {
    firstNode = findFirstLeafNode(fileNode.children[i]);
    if (fileNode) {
      break;
    }
  }
  return firstNode;
};

/**
 * 手动模拟选中第一个文件
 * @param fileNodes
 */
let onClickFirstFile = async (fileNodes: FileNode[]) => {
  let node = findFirstLeafNode(fileNodes[0]);
  if (node) {
    handleFileTreeNodeClick(node);
  }
};

// 加载文件树
onMounted(() => {
  apiGetFileTree(dir).then(res => {
    treeData.value = res.data;
    nextTick(() => onClickFirstFile(treeData.value));
  });
});
</script>

<style scoped lang="scss">
.tree-container {
  height: 600px;
  overflow-y: scroll;
}
</style>
