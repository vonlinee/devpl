<template>
  <vxe-modal v-model="visible" width="70%" height="80%" title="导入数据库表" :mask-closable="false" :draggable="false"
    :z-index="2000" show-footer>

    <div style="display: flex; height: 100%; flex-direction: column;">

      <el-form ref="dataFormRef" :model="dataForm" inline>
        <el-form-item label="数据源" prop="datasourceId">
          <el-select v-model="dataForm.datasourceId" style="width: 100%" placeholder="请选择数据源"
            @change="getTableList(true)">
            <el-option label="默认数据源" :value="-1"></el-option>
            <el-option v-for="ds in dataForm.datasourceList" :key="ds.id" :label="ds.connName" :value="ds.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="数据库" prop="datasourceId">
          <el-select v-model="dataForm.databaseName" style="width: 100%" @change="getTableList">
            <el-option v-for="databaseName in dataForm.databaseNames" :key="databaseName" :label="databaseName"
              :value="databaseName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表名称" prop="tableNamePattern">
          <el-row>
            <el-input v-model="dataForm.tableNamePattern" clearable></el-input>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button @click="() => getTableList()">查询</el-button>
        </el-form-item>
      </el-form>
      <div style="flex: 1; min-height: 0">
        <el-table :data="dataForm.tableList" height="100%" border @selection-change="selectionChangeHandle">
          <el-table-column type="selection" header-align="center" align="center" width="40"></el-table-column>
          <el-table-column prop="databaseName" label="数据库名" header-align="center" align="center"
            width="200"></el-table-column>
          <el-table-column prop="tableName" label="表名" header-align="center" align="center" width="300"></el-table-column>
          <el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
        </el-table>
      </div>
    </div>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue"
import { ElMessage } from "element-plus/es"
import { apiGetDatabaseNamesById, useDataSourceListApi } from "@/api/datasource"
import { useDataSourceTableListApi } from "@/api/datasource"

const emit = defineEmits(["handleSelection"])

const visible = ref(false)
const dataFormRef = ref()

type FormData = {
  id?: number
  tableNameListSelections: any[]
  datasourceId?: number,
  /**
   * 数据库名称
   */
  databaseName?: string,
  tableNamePattern: undefined
  datasourceList: any[],
  databaseNames: string[],
  tableList: any[]
  table: {
    tableName?: string
  }
}

const dataForm = reactive<FormData>({
  id: undefined,
  tableNameListSelections: [] as any,
  databaseName: "",
  datasourceId: undefined,
  tableNamePattern: undefined,
  databaseNames: [],
  datasourceList: [] as any,
  tableList: [] as any,
  table: {
    tableName: "",
  },
})

// 多选
const selectionChangeHandle = (selections: any[]) => {
  dataForm.tableNameListSelections = selections.map(
    (item: any) => item["tableName"]
  )
}

const getDataSourceList = () => {
  useDataSourceListApi().then((res) => {
    dataForm.datasourceList = res.data
  })
}

const getTableList = (refreshDataBaseNames?: boolean) => {
  dataForm.table.tableName = ""
  if (dataForm.datasourceId === undefined) {
    return
  }

  if (refreshDataBaseNames == true) {
    apiGetDatabaseNamesById(dataForm.datasourceId).then((res) => {
      dataForm.databaseNames = res.data
    })
  }

  useDataSourceTableListApi(
    dataForm.datasourceId,
    dataForm.databaseName,
    dataForm.tableNamePattern
  ).then((res) => {
    dataForm.tableList = res.data
  })
}

// 表单提交
const submitHandle = () => {
  const tableNameList = dataForm.tableNameListSelections
    ? dataForm.tableNameListSelections
    : []
  if (tableNameList.length === 0) {
    ElMessage.warning("请选择记录")
    return
  }
  if (dataForm.datasourceId) {
    visible.value = false
    emit("handleSelection", dataForm.datasourceId, tableNameList)
  }
}
defineExpose({
  show: (id?: number) => {
    visible.value = true
    if (id) {
      dataForm.id = id
    }
    // 重置表单数据
    if (dataFormRef.value) {
      dataFormRef.value.resetFields()
    }
    dataForm.tableList = []
    getDataSourceList()
  },
})
</script>
