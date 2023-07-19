<template>
    <splitpanes class="default-theme">
        <pane max-size="35" style="overflow: auto">
            <el-tree :data="treeData" :props="defaultProps" default-expand-all @node-click="handleFileTreeNodeClick"
                     style="width: 100%; height: 100%; display: inline-block; min-width: 100%;"/>
        </pane>
        <pane>
            <monaco-editor ref="editorRef" language="java"></monaco-editor>
        </pane>
    </splitpanes>
</template>

<script lang="ts">
import {Pane, Splitpanes} from "splitpanes";
import 'splitpanes/dist/splitpanes.css'
import {onMounted, ref, toRefs} from 'vue';
import {getLanguage} from "@/components/editor/monaco-editor";
import {apiGetFileContent, apiGetFileTree} from "@/api/factory";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

/**
 * 文件节点
 */
interface FileNode {
    path: string,
    key: string,
    label: string,
    isLeaf: boolean,
    selectable: boolean,
    extension: string, // 文件后缀名
    children: FileNode[]
}

/**
 * 递归展开所有非叶子节点
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

export default {
    name: 'CodeTreeVoew',
    components: {MonacoEditor, Splitpanes, Pane},
    props: {
        // 展示该目录下的所有文件
        dir: {
            type: String
        }
    },
    setup(props: any) {
        const editorRef = ref()
        const treeData = ref<FileNode[]>([])
        const {dir} = toRefs(props)

        // 返回数据结构取的字段
        const defaultProps = {
            children: 'children',
            label: 'label',
            id: 'key'
        }

        /**
         * 树节点点击事件
         * 如果是文件节点，调用后台接口获取文件内容
         * 如果是目录节点，直接展开该目录
         * @param fileNode
         * @see FileNode#isLeaf
         */
        let handleFileTreeNodeClick = (fileNode: FileNode) => {
            if (fileNode && fileNode.isLeaf) {
                const lang = getLanguage(fileNode.extension)
                apiGetFileContent(fileNode.path).then(res => {
                    editorRef.value.setLanguage(lang)
                    editorRef.value.setText(res.data)
                })
            }
        }

        // 加载文件树
        onMounted(() => {
            apiGetFileTree(dir.value).then(res => {
                treeData.value = res.data
            });
        });

        return {
            editorRef,
            treeData,
            defaultProps,
            handleFileTreeNodeClick
        };
    },
};

</script>

<style scoped lang="scss">
.splitpanes__pane {
    display: flex;
    justify-content: center;
    align-items: center;
    font-family: Helvetica, Arial, sans-serif;
    color: rgba(85, 91, 82, 0.6);
    font-size: 5em;
}

.splitpanes__splitter {
    width: 200px;
}

.el-tree {
    width: 100%;
    overflow-x: auto;
}

.el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block !important;
}
</style>
