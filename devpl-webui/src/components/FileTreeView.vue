<template>
    <div style="padding: 0 24px;">
        <el-row>
            <el-col :lg="6">
                <el-tree node-key="key" :data="treeData" :props="defaultProps" style="height: 600px"
                         @node-click="handleFileTreeNodeClick"></el-tree>
            </el-col>
            <el-col :lg="18">
                <div style="display: flex; height: 200px">
                    <monaco-editor></monaco-editor>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {apiGetFileContent, apiGetFileTree} from '@/api/factory'
import {onMounted, ref} from "vue";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";

export default {
    components: {MonacoEditor},
    setup() {
        let treeData = ref([])
        let currentFileText = ref("")
        // 返回数据结构取的字段
        const defaultProps = {
            children: 'children',
            label: 'label',
            id: 'key'
        }

        let handleFileTreeNodeClick = (fileNode) => {
            apiGetFileContent(fileNode.path).then(res => {
                currentFileText.value = res.data;
            })
        }

        onMounted(() => {
            apiGetFileTree("D:/Temp").then(res => {
                treeData.value = res.data
            });
        })

        return {
            treeData, defaultProps,
            handleFileTreeNodeClick, currentFileText
        }
    }
}
</script>
