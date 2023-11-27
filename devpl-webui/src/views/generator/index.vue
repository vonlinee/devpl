<template>
  <el-card :body-style="{
    height: '35px'
  }">
    <el-form>
      <el-form-item label="选择项目">
        <el-select v-model="project">
          <el-option v-for="project in projects" :key="project.projectId" :value="project"
            :label="project.projectName"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card :body-style="{
    height: '35px'
  }">
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
      <el-form-item>
        <el-button type="danger" @click="showConfig()">生成配置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="showFileTypeManagerDialog()">文件类型管理</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-table v-loading="state.dataListLoading" :data="state.dataList" border height="500px"
    @selection-change="selectionChangeHandle">
    <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
    <el-table-column prop="tableName" label="表名" header-align="center" align="center"></el-table-column>
    <el-table-column prop="tableComment" label="表说明" header-align="center" align="center"></el-table-column>
    <el-table-column label="操作" fixed="right" header-align="center" align="center" width="250">
      <template #default="scope">
        <el-button type="primary" link @click="editHandle(scope.row.id)">参数配置</el-button>
        <el-button type="primary" link @click="generatorHandle(scope.row.id)">生成</el-button>
        <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
        <el-button type="primary" link @click="syncHandle(scope.row)">同步</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination :current-page="state.page" :page-sizes="state.pageSizes" :page-size="state.limit" :total="state.total"
    layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle">
  </el-pagination>

  <gen-table-import ref="importRef" @handle-selection="handTableSelection"></gen-table-import>
  <edit ref="editRef" @refresh-data-list="getDataList"></edit>
  <generator ref="generatorRef" @refresh-data-list="getDataList"></generator>

  <vxe-modal title="代码生成配置" v-model="configDialogRef" draggable show-footer width="70%">
    <div style="height: 600px">
      <monaco-editor ref="configEditor" language="json"></monaco-editor>
    </div>
    <template #footer>
      <el-button @click="saveConfig()">确认</el-button>
    </template>
  </vxe-modal>

  <gen-file-type-dialog ref="fileTypeManagerRef"></gen-file-type-dialog>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref, h } from "vue";
import { DataTableOptions } from "@/hooks/interface";
import { useCrud } from "@/hooks";
import GenTableImport from "./GenTableImport.vue";
import Edit from "./edit.vue";
import Generator from "./generator.vue";
import { apiImportTables, apiSyncTable } from "@/api/table";
import { ElMessage, ElMessageBox } from "element-plus";
import { apiGetGeneratorConfig, apiSaveGeneratorConfig, useDownloadApi } from "@/api/generator";
import MonacoEditor from "@/components/editor/MonacoEditor.vue";
import GenFileTypeDialog from "@/views/generator/GenFileTypeDialog.vue";
import { apiListSelectableProjects } from "@/api/project";
import { useRouter } from "vue-router";

const state: DataTableOptions = reactive({
  dataListUrl: "/gen/table/page",
  deleteUrl: "/gen/table/remove",
  queryForm: {
    tableName: ""
  }
});

const router = useRouter()
const fileTypeManagerRef = ref();
const importRef = ref();
const editRef = ref();
const generatorRef = ref();

function showFileTypeManagerDialog() {
  fileTypeManagerRef.value.init();
}

const importHandle = (id?: number) => {
  if (project.value == undefined) {
    const message = ElMessage({
      duration: 500,
      message: h('p', null, [
        h('span', null, '请先选择项目，如果没有，先'),
        h('a', {
          style: 'color: blue',
          onClick: function () {
            message.close()
            router.push({ path: '/codegen/project' })
          }
        }, '创建项目'),
      ])
    })
    return
  }
  importRef.value.show(id);
};

const editHandle = (id?: number) => {
  editRef.value.init(id);
};

const generatorHandle = (id?: number) => {
  generatorRef.value.init(id);
};

const downloadBatchHandle = () => {
  const tableIds = state.dataListSelections ? state.dataListSelections : [];
  if (tableIds.length === 0) {
    ElMessage.warning("请选择生成代码的表");
    return;
  }
  useDownloadApi(tableIds);
};

const projects = ref<ProjectSelectVO[]>();
const project = ref<ProjectSelectVO>();
onMounted(() => {
  // 获取可选择的项目列表
  apiListSelectableProjects().then((res) => projects.value = res.data);
});

const syncHandle = (row: any) => {
  ElMessageBox.confirm(`确定同步数据表${row.tableName}吗?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      apiSyncTable(row.id).then(() => {
        ElMessage.success("同步成功");
      });
    })
    .catch(() => {
    });
};

const configEditor = ref();
const configDialogRef = ref(false);

function showConfig() {
  apiGetGeneratorConfig().then((res) => {
    if (res.code == 2000) {
      configDialogRef.value = true;
      nextTick(() => configEditor.value.setText(res.data));
    }
  });
}

/**
 * 保存配置
 */
function saveConfig() {
  apiSaveGeneratorConfig(configEditor.value.getText()).then((res) => {
    if (res.data) {
      ElMessage.success("保存配置成功");
      configDialogRef.value = false;
    }
  }).catch((reason) => {
    ElMessage.error("保存配置失败", reason);
  });
}

const handTableSelection = (dataSourceId: number, tableNames: string[]) => {
  console.log("import");
  if (dataSourceId) {
    apiImportTables(dataSourceId, tableNames, project.value?.projectId).then(() => {
      ElMessage.success({
        message: "操作成功",
        duration: 500,
        onClose: () => {
          getDataList();
        }
      });
    });
  }
};


const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state);
</script>
