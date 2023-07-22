<template>
    <el-row class="el-row">
        <el-col :span="16" style="height: 500px">
            <codemirror
                v-model="code"
                placeholder=""
                :style="style"
                :mode="mode"
                :spellcheck="spellcheck"
                :autofocus="autofocus"
                :indent-with-tab="indentWithTab"
                :tab-size="tabSize"
                :extensions="extensions"
            />
        </el-col>
        <el-col :span="8">
            <param-import></param-import>
            <vxe-table
                show-overflow
                ref="msParamTable"
                border
                row-key
                :data="mapperParams"
                :checkbox-config="{ checkStrictly: true }"
                :tree-config="{transform: true}"
                :edit-config="editConfig"
            >
                <vxe-column field="name" title="参数名" tree-node></vxe-column>
                <vxe-column field="value" title="参数值" :edit-render="{ name: 'input' }"></vxe-column>
                <vxe-column field="type" title="类型" :edit-render="{}">
                    <template #default="{ row }">
                        <span>{{ row.type }}</span>
                    </template>
                    <template #edit="{ row }">
                        <vxe-select v-model="row.type" clearable transfer>
                            <vxe-option v-for="item in javaDataTypes" :key="item.value" :value="item.name"
                                        :label="item.label"></vxe-option>
                        </vxe-select>
                    </template>
                </vxe-column>
            </vxe-table>
        </el-col>
    </el-row>
    <el-button type="primary" @click="getParams()">解析参数</el-button>
    <el-button type="primary" @click="getSqlOfMapperStatement(false)">获取预编译sql</el-button>
    <el-button type="primary" @click="getSqlOfMapperStatement(true)">获取实际sql</el-button>

    <el-dialog v-model="dialogVisiable">
        <template #default>
            <code-editor ref="dialogEditorRef"></code-editor>
        </template>
    </el-dialog>

    <codemirror
        v-model="sqlRef"
        :style="style"
        mode="text/sql"
        :spellcheck="spellcheck"
        :autofocus="autofocus"
        :indent-with-tab="indentWithTab"
        :tab-size="tabSize"
        :extensions="extensions"
    />
</template>

<script>
// Vue Codemirror 文档：https://github.com/surmon-china/vue-codemirror
import {Codemirror} from 'vue-codemirror'
// 语言支持
import {sql} from '@codemirror/lang-sql'
import {hasText} from '@/utils/tool'
// import "codemirror/addon/hint/show-hint.css";
import {onMounted, reactive, ref, toRefs} from 'vue'

import {apiGetDataTypes, apiGetSql, getMapperStatementParams} from '@/api/mybatis'
import {ElButton, ElDialog, ElMessage} from 'element-plus'
import CodeEditor from "@/components/CodeEditor.vue";
import ParamImport from "@/views/mybatis/ParamImport.vue";

export default {
    components: {
        ParamImport,
        CodeEditor,
        ElDialog,
        ElButton,
        Codemirror
    },
    setup(props, context) {
        // 数据
        const code = ref('')
        let dialogVisiable = ref(false)
        let dialogEditorRef = ref(null)
        let editorRef = ref(null)

        let sqlRef = ref(``)

        // 表格实例
        let msParamTable = ref()

        let javaDataTypes = ref()

        const editConfig = reactive({
            trigger: 'click',
            mode: 'cell',
            beforeEditMethod: (row, rowIndex, column, columnIndex) => {
                // 只有叶子结点可编辑
                return row.row.leaf != null && row.row.leaf
            }
        })

        onMounted(() => {
            apiGetDataTypes().then((res) => {
                javaDataTypes.value = res.data
            })
        })

        // codemirror 编辑器选项
        const options = reactive({
            style: {
                height: '500px'
            },
            mode: 'text/x-mysql',
            spellcheck: false, // 拼写检查
            autofocus: true, // Focus editor immediately after mounted.
            indentWithTab: true,
            tabSize: 2,
            placeholder: 'hello world',
            extensions: [sql()] //传递给CodeMirror EditorState。创建({扩展})
        })

        // mybatis mapper语句参数
        let mapperParams = ref([])

        const expandAll = () => {
            const $table = msParamTable.value
            if ($table) {
                $table.setAllTreeExpand(true)
            }
        }

        // 获取参数
        function getParams() {
            if (hasText(code.value)) {
                getMapperStatementParams(code.value).then(value => {
                    mapperParams.value = value.data
                }).then(() => expandAll())
            } else {
                ElMessage.warning('输入文本为空!')
            }
        }

        // 获取sql
        function getSqlOfMapperStatement(real) {
            if (!hasText(code.value)) {
                ElMessage.warning('输入文本为空!')
                return
            }
            apiGetSql(code.value, mapperParams.value, real).then(res => {
                sqlRef.value = res.data
            })
        }

        // 返回
        return {
            code,
            ...toRefs(options),
            getParams,
            mapperParams,
            msParamTable,
            editConfig,
            javaDataTypes,
            dialogVisiable,
            editorRef,
            sqlRef,
            dialogEditorRef,
            getSqlOfMapperStatement
        }
    }
}
</script>

<style lang="scss">
// Code Mirror 字体配置
.cm-content {
    font-family: Consolas, serif;
}

.el-row {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
}

.el-card {
    min-width: 100%;
    height: 100%;
    // 高度要设置百分比才可以
    margin-right: 20px;
    transition: all 0.5s;
}
</style>
