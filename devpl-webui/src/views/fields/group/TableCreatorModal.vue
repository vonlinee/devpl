<template>
  <vxe-modal title="新建表" v-model="visible" height="80%" width="80%" show-footer transfer>
    <div>
      <el-button type="primary" @click="addColumn">添加字段</el-button>
    </div>

    <el-tabs v-model="activeTabName" @tab-change="handleTabChanged">
      <el-tab-pane label="字段" name="field">
        <el-table :loading="loading" height="400px" :data="columns" border style="width: 100%" show-overflow-tooltip>
          <el-table-column prop="columnName" label="列名" width="180">
            <template #default="scope">
              <el-input v-model="scope.row.columnName"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="dataType" label="数据类型" width="150" show-overflow-tooltip>
            <template #default="scope">
              <el-select v-model="scope.row.dataType" clearable filterable>
                <el-option :label="dt" :value="dt" :key="dt" v-for="dt in dataTypes"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="columnSize" label="长度" width="130">
            <template #default="scope">
              <el-input-number style="width: 100%" controls-position="right" v-model="scope.row.columnSize" :min="1"
                :max="500" />
            </template>
          </el-table-column>
          <el-table-column prop="decimalDigits" label="小数点" width="100">
            <template #default="scope">
              <el-input-number style="width: 100%" controls-position="right" v-model="scope.row.decimalDigits" :min="1"
                :max="10" />
            </template>
          </el-table-column>
          <el-table-column prop="nullable" label="可null" width="80">
            <template #default="scope">
              <el-switch v-model="scope.row.nullable" />
            </template>
          </el-table-column>
          <el-table-column prop="virtual" label="虚拟" width="70">
            <template #default="scope">
              <el-switch v-model="scope.row.virtual" />
            </template>
          </el-table-column>
          <el-table-column prop="primaryKey" label="主键" width="70">
            <template #default="scope">
              <el-switch v-model="scope.row.primaryKey" />
            </template>
          </el-table-column>
          <el-table-column prop="defaultValue" label="默认值" width="120">
            <template #default="scope">
              <el-input v-model="scope.row.defaultValue"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="注释">
            <template #default="scope">
              <el-input v-model="scope.row.remarks"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" fixed="right">
            <template #default="scope">
              <vxe-button type="text" status="danger" lick="removeRow(scope.row)">删除</vxe-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="选项" name="option">
        <el-form>
          <el-form-item label="表名">
            <el-input v-model="formData.tableName"></el-input>
          </el-form-item>
          <el-form-item label="字符集">
            <el-select v-model="formData.charset">
              <el-option label="utf8mb4" value="utf8mb4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否包裹表名和列名">
            <el-switch :value="formData.wrapIdentifier"></el-switch>
          </el-form-item>
          <el-form-item label="是否生成DROP Table语句">
            <el-switch :value="formData.dropTable"></el-switch>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="索引" name="index"></el-tab-pane>

      <el-tab-pane label="SQL预览" name="ddl">
        <monaco-editor ref="ddlEditorRef" language="sql" height="440px"></monaco-editor>
      </el-tab-pane>
    </el-tabs>
  </vxe-modal>
</template>


<script setup lang="ts">

import { apiGetTableCreatorColumns, apiGetTableCreatorDDL } from "@/api/devtools";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import { TabPaneName } from "element-plus";
import { ref } from "vue";

const activeTabName = ref('field')
const ddlEditorRef = ref()
const columns = ref<ColumnInfo[]>([]);

const visible = ref();
const loading = ref();

defineExpose({
  show(fieldGroupId: number) {
    visible.value = true;

    loading.value = true
    apiGetTableCreatorColumns(fieldGroupId).then((res) => {
      setColumns(res.data)
      loading.value = false
    })
  }
});

const addColumn = () => {
  columns.value?.push({
    columnName: "",
    dataType: "varchar",
    columnSize: 0,
    decimalDigits: 0,
    nullable: true,
    virtual: false,
    primaryKey: false,
    remarks: ""
  });
};

const removeRow = (row: ColumnInfo) => {
  let index = columns.value.indexOf(row)
  if (index >= 0) {
    columns.value.splice(index, 1);
  }
}

type ColumnInfo = {
  columnName: string
  dataType: string
  columnSize: number
  decimalDigits: number
  nullable: boolean
  virtual: boolean
  primaryKey: boolean
  remarks: string
  dataTypes?: string[]
}

const dataTypes = ref<string[]>([])

/**
 * 创建表的参数
 */
const formData = ref<{
  tableName?: string,
  charset?: string,
  /**
   * 是否使用引号包裹表名和列名
   */
  wrapIdentifier?: boolean,
  /**
   * 是否生成DROP Table语句
   */
  dropTable?: boolean
  columns?: ColumnInfo[]
}>({
  wrapIdentifier: true,
  dropTable: true
})

/**
 * https://element-plus.gitee.io/zh-CN/component/tabs.html#tabs-%E4%BA%8B%E4%BB%B6
 */
const handleTabChanged = (name: TabPaneName) => {
  if (name == 'ddl') {
    formData.value.columns = columns.value
    apiGetTableCreatorDDL(formData.value).then((res) => {
      ddlEditorRef.value.setText(res.data)
    })
  }
}

function addColumns(fields: FieldInfo[]) {
  if (fields) {
    const _columns: ColumnInfo[] = [];
    for (let i = 0; i < fields.length; i++) {
      const field = fields[i];
      _columns.push({
        columnName: field.fieldName || "",
        dataType: (field.dataType || "varchar") as string,
        columnSize: 0,
        decimalDigits: 0,
        nullable: true,
        virtual: false,
        primaryKey: false,
        remarks: ""
      });
    }
    columns.value = _columns;
  }
}

function setColumns(columnsToSet: ColumnInfo[]) {
  columns.value = columnsToSet;
  dataTypes.value = columnsToSet[0].dataTypes || []
}
</script>

<style scoped lang="scss">
.toolbar-container {
  display: flex;
  flex-direction: row;
}
</style>
