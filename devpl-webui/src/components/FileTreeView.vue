<template>
    <splitpanes class="tree-view-container">
        <pane min-size="40" style="height: 600px; width: 30%">
            <el-tree node-key="key" :data="treeData" :props="defaultProps"
                     default-expand-all
                     @node-click="handleFileTreeNodeClick"></el-tree>
        </pane>
        <pane style="width: 70%; height: 600px">
            <monaco-editor ref="editorRef" language="json" value=""></monaco-editor>
        </pane>
    </splitpanes>
</template>

<script setup lang="ts">
import {apiGetFileContent, apiGetFileTree} from '@/api/factory'
import {ref} from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import {Pane, Splitpanes} from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

let editorRef = ref()
// 返回数据结构取的字段
const defaultProps = {
    children: 'children',
    label: 'label',
    id: 'key'
}

let defaultExpandedKeys = ref<string[]>([])

/**
 * 文件节点
 */
interface FileNode {
    path: string,
    key: string,
    label: string,
    isLeaf: boolean,
    selectable: boolean,
    children: FileNode[]
}

let treeData = ref<FileNode[]>([])

/**
 * 树节点点击事件
 * @param fileNode
 */
let handleFileTreeNodeClick = (fileNode: FileNode) => {
    if (fileNode && fileNode.isLeaf) {
        apiGetFileContent(fileNode.path).then(res => {
            editorRef.value.setText(res.data)
        })
    }
}

/**
 * 递归展开所有父节点
 * @param fileNode 当前节点
 * @param defaultExpandedKeys 保存展开节点的key，递归遍历节点数，填充key到此数组
 */
function expandAllParentNode(fileNode: FileNode, defaultExpandedKeys: string[]) {
    if (!fileNode.isLeaf) {
        return
    }
    defaultExpandedKeys.push(fileNode.key)
    for (let i = 0; i < fileNode.children.length; i++) {
        expandAllParentNode(fileNode.children[i], defaultExpandedKeys)
    }
}

/**
 * Emit函数无返回值
 */
interface EmitFunction {
    load: [dir: string]
}

defineEmits<EmitFunction>()

function loadFileTree(rootDir: string) {
    apiGetFileTree(rootDir).then(res => {
        treeData.value = res.data
        let keys: string[] = []

        for (let i = 0; i < treeData.value.length; i++) {
            expandAllParentNode(treeData.value[i], keys)
        }
        defaultExpandedKeys.value = keys
        console.log(keys)
    });
}

defineExpose({
    load: loadFileTree
})
</script>

<style lang="scss" scoped>
.tree-view-container {
    display: flex;
    align-items: center;
    font-family: Helvetica, Arial, sans-serif;
    color: rgba(255, 255, 255, 0.6);
    font-size: 5em;
    width: 80%;
    height: 80%;
}
</style>
