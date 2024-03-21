<template>
  <el-card>
    <el-form>
      <el-form-item label="选择项目">
        <el-select v-model="state.queryForm.projectId">
          <el-option v-for="project in projects" :key="project.projectId" :value="project.projectId"
            :label="project.projectName"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.tableName" placeholder="表名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="importHandle()">导入</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="downloadBatchHandle()">生成代码</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-table v-loading="state.dataListLoading" :data="state.dataList" border height="500px"
    @selection-change="selectionChangeHandle">
    <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
    <el-table-column prop="connectionName" label="连接名" header-align="center" align="center"
      width="150"></el-table-column>
    <el-table-column prop="databaseName" label="数据库名" header-align="center" align="center" width="150"
      show-overflow-tooltip></el-table-column>
    <el-table-column prop="tableName" label="表名" header-align="center" align="center" width="200"
      show-overflow-tooltip></el-table-column>
    <el-table-column prop="tableComment" label="表说明" header-align="center" align="center"
      show-overflow-tooltip></el-table-column>
    <el-table-column label="操作" fixed="right" header-align="center" align="center" width="170">
      <template #default="scope">
        <el-button type="primary" link @click="editHandle(scope.row.id)">编辑</el-button>
        <el-button type="primary" link @click="syncHandle(scope.row)">同步</el-button>
        <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination :current-page="state.page" :page-sizes="state.pageSizes" :page-size="state.limit" :total="state.total"
    layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle">
  </el-pagination>

  <gen-table-import ref="importRef" @handle-selection="handTableSelection"></gen-table-import>
  <table-config ref="editRef" @refresh-data-list="getDataList"></table-config>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, h } from "vue"
import { DataTableOption } from "@/hooks/interface"
import { useCrud } from "@/hooks"
import GenTableImport from "./GenTableImport.vue"
import TableConfig from "./TableConfig.vue"
import {
  apiImportTables,
  apiListGenTables,
  apiRemoveGenTableByIds,
  apiSyncTable,
} from "@/api/table"
import { ElMessage, ElMessageBox } from "element-plus"
import { useDownloadApi } from "@/api/generator"
import { apiListSelectableProjects } from "@/api/project"
import { useRouter } from "vue-router"
import { Message } from "@/hooks/message"

const state: DataTableOption = reactive({
  queryForm: {
    tableName: "",
    projectId: undefined
  },
  queryPage: apiListGenTables,
  removeByIds: apiRemoveGenTableByIds,
} as DataTableOption)

const router = useRouter()
const importRef = ref()
const editRef = ref()
const generatorRef = ref()

const importHandle = (id?: number) => {
  if (project.value == undefined) {
    const message = ElMessage({
      duration: 3000,
      message: h("p", null, [
        h("span", null, "请先选择项目，如果没有，先"),
        h(
          "a",
          {
            style: "color: blue",
            onClick: function () {
              message.close()
              router.push({ path: "/codegen/project" })
            },
          },
          "创建项目"
        ),
      ]),
    })
    return
  }
  importRef.value.show(null, state.queryForm.projectId)
}

const editHandle = (id?: number) => {
  editRef.value.init(id)
}

const generatorHandle = (id?: number) => {
  generatorRef.value.init(id)
}

const downloadBatchHandle = () => {
  const tableIds = state.dataListSelections ? state.dataListSelections : []
  if (tableIds.length === 0) {
    ElMessage.warning("请选择生成代码的表")
    return
  }
  useDownloadApi(tableIds)
}

/**
 * 可选择的项目列表
 */
const projects = ref<ProjectSelectVO[]>()

/**
 * 当前选择的项目信息
 */
const project = ref<ProjectSelectVO>()

onMounted(() => {
  // 获取可选择的项目列表
  apiListSelectableProjects().then((res) => {
    projects.value = res.data
    if (res.data && res.data.length > 0) {
      // 默认选中第一个
      project.value = res.data[0]
    }
  })
})

const syncHandle = (row: any) => {
  ElMessageBox.confirm(`确定同步数据表${row.tableName}吗?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      apiSyncTable(row.id).then(() => {
        ElMessage.success("同步成功")
      })
    })
    .catch(() => { })
}

/**
 * 导入表生成配置数据
 * @param dataSourceId 数据源ID 
 * @param tableNames 导入的表名称
 */
const handTableSelection = (datasourceId: number, tables: TableImportInfo[], databaseName?: string) => {
  if (tables != undefined) {
    apiImportTables(datasourceId, tables, databaseName, project.value?.projectId)
      .then((res) => {
        Message.info("导入" + res.data + "个表")
      })
      .then(() => getDataList())
  }
}

const {
  getDataList,
  selectionChangeHandle,
  sizeChangeHandle,
  currentChangeHandle,
  deleteBatchHandle,
} = useCrud(state)
</script>
