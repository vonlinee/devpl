<template>
  <el-form>
    <div style="display: flex;">
      <el-form-item label="项目根目录" style="flex: 1;">
        <template #default="scope">
          <el-input v-model="rootDir"></el-input>
        </template>
      </el-form-item>
      <div style="margin-left: 20px;">
        <el-button type="primary" @click="openSelectModal">选择项目</el-button>
        <el-button type="primary" @click="buildMsIndex">索引</el-button>
      </div>
    </div>
  </el-form>

  <el-card>
    <el-form :model="options.queryForm" inline>
      <el-form-item label="类型">
        <el-select v-model:model-value="options.queryForm.statementType" clearable>
          <el-option label="Select" value="select"></el-option>
          <el-option label="Update" value="update"></el-option>
          <el-option label="Delete" value="delete"></el-option>
          <el-option label="Insert" value="insert"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="ID">
        <el-input v-model="options.queryForm.statementId"></el-input>
      </el-form-item>
      <el-form-item label="命名空间">
        <el-input v-model="options.queryForm.namespace"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-table :data="options.dataList" border height="500px">
    <el-table-column label="命名空间" prop="namespace"></el-table-column>
    <el-table-column label="类型" prop="statementType" width="100"></el-table-column>
    <el-table-column label="ID" prop="statementId"></el-table-column>
    <el-table-column label="参数" prop="paramType"></el-table-column>
    <el-table-column label="返回值" prop="resultType"></el-table-column>
    <el-table-column label="操作" width="150">
      <template #default="scope">
        <el-button link @click="showSqlDebugTool(scope.row.statement)">SQL调试</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination layout="sizes, total, prev, pager, next" background :total="options.total" :current-page="options.page"
    :page-sizes="options.pageSizes" @current-change="currentChangeHandle" @size-change="sizeChangeHandle" />

  <FileOpenModal ref="fileOpenModalRef" @selected="handleSelected"></FileOpenModal>

  <MyBatisSqlDebugger ref="sqlDebugModalRef"></MyBatisSqlDebugger>
</template>

<script setup lang="ts">
import { apiBuildMappedStatementIndex, apiListMappedStatements } from '@/api/mybatis';
import FileOpenModal from '@/components/FileOpenModal.vue';
import { useCrud } from '@/hooks';
import { DataTableOption } from '@/hooks/interface';
import { Message } from '@/hooks/message';
import { onMounted, reactive, ref } from 'vue';
import MyBatisSqlDebugger from './MyBatisSqlDebugger.vue';
const rootDir = ref('')
const fileOpenModalRef = ref()
const sqlDebugModalRef = ref()
const buildMsIndex = () => {
  if (!window.hasText(rootDir.value)) {
    Message.info("项目根目录为空")
  } else {
    apiBuildMappedStatementIndex(rootDir.value).then((res) => {
      getDataList()
    })
  }
}

const showSqlDebugTool = (statement: string) => {
  sqlDebugModalRef.value.show(statement)
}

const openSelectModal = () => {
  fileOpenModalRef.value.show()
}

const handleSelected = (val: any) => {
  rootDir.value = val
}

const options = reactive({
  queryForm: {
    statementType: "select",
    statementId: "",
    namespace: ""
  },
  isPage: true,
  queryPage: apiListMappedStatements
} as DataTableOption)

const { getDataList, currentChangeHandle, sizeChangeHandle } = useCrud(options)

onMounted(() => {
  getDataList()
})

</script>