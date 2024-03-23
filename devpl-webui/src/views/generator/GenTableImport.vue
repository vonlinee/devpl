<template>
  <vxe-modal v-model="visible" width="70%" height="80%" title="导入数据库表" :mask-closable="false" :draggable="false"
    :z-index="2000" show-footer>
    <div style="display: flex; height: 100%; flex-direction: column">
      <div style="display: flex; flex-direction: row">
        <div>
          <el-form ref="dataFormRef" :model="dataForm" inline>
            <el-form-item label="数据源" prop="datasourceId">
              <el-select v-model="dataForm.datasourceId" style="width: 100%" placeholder="请选择数据源"
                @change="getTableList(true)">
                <el-option v-for="ds in dataForm.datasourceList" :key="ds.id" :label="ds.connectionName"
                  :value="ds.id"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="数据库" prop="datasourceId">
              <el-select v-model="dataForm.databaseName" style="width: 100%" @change="getTableList" filterable
                clearable>
                <el-option v-for="databaseName in dataForm.databaseNames" :key="databaseName" :label="databaseName"
                  :value="databaseName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="表名称" prop="tableNamePattern">
              <el-row>
                <el-input v-model="dataForm.tableNamePattern" clearable @keyup.enter="() => getTableList()"></el-input>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
        <el-button type="primary" @click="() => getTableList(true)"
          style="margin-left: auto; margin-right: 20px">查询</el-button>
      </div>
      <div style="flex: 1; min-height: 0">
        <el-table v-loading="loading" :data="dataForm.tableList" height="100%" border
          @selection-change="selectionChangeHandle">
          <el-table-column type="selection" header-align="center" align="center" width="40"></el-table-column>
          <el-table-column prop="databaseName" label="数据库名" header-align="center" align="center"
            width="200"></el-table-column>
          <el-table-column prop="tableName" label="表名" header-align="center" align="center"
            width="300"></el-table-column>
          <el-table-column prop="tableType" label="表类型" header-align="center" align="center"
            width="150"></el-table-column>
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
import { nextTick, reactive, ref } from "vue"
import { apiGetDatabaseNamesById, useDataSourceListApi } from "@/api/datasource"
import { apiListDataSourceTables } from "@/api/datasource"
import { Message } from "@/hooks/message"

const emit = defineEmits(["handleSelection"])

const visible = ref(false)
const dataFormRef = ref()

const loading = ref(false)

type FormData = {
  /**
   * 选择的表信息
   */
  selectedTables: TableImportInfo[]

  tableNameListSelections: any[]
  datasourceId?: number
  /**
   * 数据库名称
   */
  databaseName?: string
  tableNamePattern: undefined
  datasourceList: any[]
  databaseNames: string[]
  tableList: any[]
  table: {
    tableName?: string
  }
}

/**
 * 表单
 */
const dataForm = reactive<FormData>({
  selectedTables: [],
  tableNameListSelections: [] as any,
  databaseName: "",
  datasourceId: 0,
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
  dataForm.selectedTables = selections.map((item: any) => {
    return {
      dataSourceId: item["datasourceId"],
      tableName: item["tableName"],
      databaseName: item["databaseName"],
    } as TableImportInfo
  })
}

const getDataSourceList = () => {
  loading.value = true
  useDataSourceListApi()
    .then((res) => {
      dataForm.datasourceList = res.data
      loading.value = false
    })
    .finally(() => {
      loading.value = false
    })
}

/**
 * 获取表格数据
 * @param refreshDataBaseNames 是否刷新数据库名称，会默认选择第一个数据库
 */
const getTableList = (refreshDataBaseNames?: boolean) => {
  if (dataForm.datasourceId === undefined) {
    return
  }
  if (refreshDataBaseNames == true) {
    apiGetDatabaseNamesById(dataForm.datasourceId).then((res) => {
      dataForm.databaseNames = res.data
      if (!dataForm.databaseName) {
        // 取第一个
        dataForm.databaseName = dataForm.databaseNames[0]
      }
      // 切换数据源时每次仅查单个数据库的表
      nextTick(() => {
        loading.value = true
        apiListDataSourceTables(
          dataForm.datasourceId!,
          dataForm.databaseName,
          dataForm.tableNamePattern
        )
          .then((res) => {
            dataForm.tableList = res.data
            loading.value = false
          })
          .finally(() => {
            loading.value = false
          })
      })
    })
  } else {
    loading.value = true
    apiListDataSourceTables(
      dataForm.datasourceId!,
      dataForm.databaseName,
      dataForm.tableNamePattern
    )
      .then((res) => {
        dataForm.tableList = res.data
        loading.value = false
      })
      .finally(() => {
        loading.value = false
      })
  }
}

// 表单提交
const submitHandle = () => {
  if (dataForm.selectedTables.length === 0) {
    Message.warn("请选择记录")
    return
  }
  if (dataForm.datasourceId != undefined) {
    visible.value = false
    emit(
      "handleSelection",
      dataForm.datasourceId,
      dataForm.selectedTables,
      dataForm.databaseName
    )
  }
}
defineExpose({
  show: (id?: number) => {
    visible.value = true
    if (id) {
      dataForm.datasourceId = id
    }
    // 重置表单数据
    if (dataFormRef.value) {
      dataFormRef.value.resetFields()
    }
    dataForm.tableList = []
    getDataSourceList()

    getTableList(true)
  },
})
</script>
