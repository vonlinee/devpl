<template>
  <div class="table_box card">
    <data-table ref="singleTableRef" class="table_cont" :config="config">
      <template #modal="scope">
        <el-form
          ref="ruleFormRef"
          :model="scope.form"
          status-icon
          label-width="120px"
          class="demo-ruleForm"
        >
          <el-form-item label="ID" prop="id">
            <el-input v-model="scope.form.id" />
          </el-form-item>
          <el-form-item label="字段Key" prop="fieldKey">
            <el-input v-model="scope.form.fieldKey" />
          </el-form-item>
          <el-form-item label="字段名称" prop="fieldName">
            <el-input v-model="scope.form.fieldName" />
          </el-form-item>
          <el-form-item label="数据类型" prop="dataType">
            <el-input v-model="scope.form.dataType" />
          </el-form-item>
          <el-form-item label="默认值" prop="defaultValue">
            <el-input v-model="scope.form.defaultValue" />
          </el-form-item>
          <el-form-item label="描述信息" prop="description">
            <el-input v-model="scope.form.description" />
          </el-form-item>
        </el-form>
      </template>
    </data-table>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue"
import { DataTableColumnProps, DataTableOptions } from "./interface"
import DataTable, { DataTableConfig } from "@/components/table/DataTable.vue"
import { apiListFields } from "@/api/fields"

const config: DataTableConfig = {
  tableData: [],
  options: reactive<DataTableOptions>({
    stripe: true,
    fit: true,
    loading: false,
  }),
  api: {
    queryPage: apiListFields,
    deleteOne: function (row: { [x: string]: any }): Promise<any> {
      console.log("删除行", row)
      return new Promise((resolve) => {
        resolve({
          code: 122,
        })
      })
    },
    update: function (row: { [x: string]: any }): Promise<any> {
      console.log("更新行", row)
      return new Promise((resolve) => {
        resolve({
          code: 200,
        })
      })
    },
  },
  form: {
    formData: {
      id: 1,
      fieldKey: "",
      fieldName: "",
      dataType: "",
      defaultValue: "",
      description: "",
    },
    editConverter: (row: any, form: any): any => {
      form.id = row.id
      form.fieldKey = row.fieldKey
      form.dataType = row.dataType
      form.fieldName = row.fieldName
      form.defaultValue = row.defaultValue
      form.description = row.description
      return form
    },
    resetConverter: (form) => {
      form.id = 2000
      form.fieldKey = ""
      form.dataType = ""
      form.fieldName = ""
      form.defaultValue = ""
      form.description = ""
      return form
    },
  },
  columns: [
    {
      prop: "id",
      label: "ID",
      align: "center",
      width: 100,
    },
    {
      prop: "fieldKey",
      label: "字段Key",
      align: "center",
    },
    {
      prop: "fieldName",
      label: "字段名称",
      align: "center",
    },
    {
      prop: "dataType",
      label: "数据类型",
      align: "center",
    },
    {
      prop: "defaultValue",
      label: "默认值",
      align: "center",
    },
    {
      prop: "description",
      label: "描述信息",
      align: "center",
    },
  ] as DataTableColumnProps[],
  pageable: false,
  toolbar: false,
  tools: [
    {
      type: "primary",
      label: "新增",
      onClick: (event) => {
        console.log(event)
      },
    },
  ],
}

const singleTableRef = ref()
</script>

<style scoped lang="scss">
.table_box {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  overflow-y: auto;

  .table_btn {
    margin-bottom: 20px;
    width: 200px;
  }

  .table_cont {
    flex: 1;
  }
}

.card {
  padding: 15px;
  background-color: #ffffff;
  border-radius: 5px;
  box-shadow: 3px 3px 3px #e1e0e0;
}
</style>
