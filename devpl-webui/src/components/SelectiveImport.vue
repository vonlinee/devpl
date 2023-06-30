<template>
    <el-tabs v-model="activeName" class="editor-tabs">
        <el-tab-pane label="JSON" name="json">
            <div class="code-editor-box">
                <monaco-editor ref="jsonEditorRef" language="json" value=""></monaco-editor>
            </div>
        </el-tab-pane>
        <el-tab-pane label="XML" name="xml">
            <div class="code-editor-box">
                <monaco-editor ref="xmlEditorRef" language="xml" value=""></monaco-editor>
            </div
            >
        </el-tab-pane>
        <el-tab-pane label="JSON Schema" name="jsonSchema">
            <div class="code-editor-box">
                <monaco-editor ref="jsonSchemaEditorRef" language="json" value=""></monaco-editor>
            </div
            >
        </el-tab-pane>
        <el-tab-pane label="DDL" name="ddl">
            <div class="code-editor-box">
                <monaco-editor ref="ddlEditorRef" language="sql" value=""></monaco-editor>
            </div>
        </el-tab-pane>
    </el-tabs>
</template>
<script lang="ts" setup>
import {ref} from 'vue'
import MonacoEditor from '@/components/editor/MonacoEditor.vue'

const activeName = ref('json')

let jsonEditorRef = ref()
let xmlEditorRef = ref()
let jsonSchemaEditorRef = ref()
let ddlEditorRef = ref()

const editorRefMap = new Map();
editorRefMap.set('json', jsonEditorRef)
editorRefMap.set('xml', xmlEditorRef)
editorRefMap.set('jsonSchema', jsonSchemaEditorRef)
editorRefMap.set('ddl', ddlEditorRef)

defineExpose({
    getSelectedTab: function () {
        return activeName.value
    },
    getContent: function () {
        return editorRefMap.get(activeName.value).value.getText()
    }
})

</script>
<style>
.editor-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}

.code-editor-box {
    height: 400px;
}
</style>
