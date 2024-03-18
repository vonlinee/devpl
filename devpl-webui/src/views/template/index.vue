<template>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.templateName" placeholder="模板名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="state.queryForm.templateType" clearable>
          <el-option v-for="templateType in templateTypes" :key="templateType.provider" :value="templateType.provider"
            :label="templateType.providerName"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="globalTempParamModalRef = true">全局模板参数</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border height="450px"
      @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="templateName" label="模板名称" header-align="center" align="center">
        <template #default="scope">
          <el-text class="mx-1" type="primary" style="cursor: pointer" @click="showTemplateEditDialog(scope.row)">{{
            scope.row.templateName }}
          </el-text>
        </template>
      </el-table-column>
      <el-table-column prop="provider" label="技术类型" header-align="center" width="200px" align="center"></el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip header-align="center" align="center"
        width="300px"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
        <template #default="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row)">修改
          </el-button>
          <el-button type="primary" link @click="openTemplateVarTableModal(scope.row)">参数表
          </el-button>
          <!-- v-if="!scope.row.internal"  -->
          <el-button type="primary" link @click="deleteBatchHandle(scope.row.templateId)" style="color: red;">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="state.pageSizes || []" :page-size="state.limit"
      :total="state.total" layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle">
    </el-pagination>

    <template-viewer ref="templateContentEditorRef"></template-viewer>

    <TemplateVarTable ref="templateVarTableModalRef"></TemplateVarTable>
  </el-card>

  <!-- 弹窗, 新增 / 修改 -->
  <add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>

  <vxe-modal title="全局模板参数" v-model="globalTempParamModalRef" width="80%" show-footer>
    <template-param-table ref="templateParamTableRef"></template-param-table>
    <template #footer="scope">
      <el-button type="primary" @click="submitTemplateParams">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElButton } from "element-plus";
import AddOrUpdate from "./add-or-update.vue";
import { useCrud } from "@/hooks";
import { DataTableOption } from "@/hooks/interface";
import TemplateViewer from "@/views/template/TemplateViewer.vue";
import {
  apiBatchRemoveTemplateByIds,
  apiListTemplatesByPage,
  apiListTemplateTypes,
  apiSaveOrUpdateTemplateParams
} from "@/api/template";
import TemplateVarTable from "@/views/template/TemplateVarTable.vue";
import TemplateParamTable from "@/views/template/TemplateParamTable.vue";
import { Message } from "@/hooks/message";

const state: DataTableOption = reactive({
  queryForm: {
    templateName: "",
    templateType: ""
  },
  primaryKey: "templateId",
  isPage: true,
  queryPage: apiListTemplatesByPage,
  removeByIds: apiBatchRemoveTemplateByIds
});

const {
  getDataList,
  selectionChangeHandle,
  sizeChangeHandle,
  currentChangeHandle,
  deleteBatchHandle
} = useCrud(state);

const templateParamTableRef = ref();
const addOrUpdateRef = ref();
const globalTempParamModalRef = ref();
const addOrUpdateHandle = (row?: any) => {
  addOrUpdateRef.value.init(row);
};

const templateVarTableModalRef = ref();
const openTemplateVarTableModal = (row?: any) => {
  if (row) {
    templateVarTableModalRef.value.show(row);
  }
};

const templateContentEditorRef = ref();
const templateTypes = ref<TemplateProvider[]>();

const resetForm = () => {
  state.queryForm = {
    templateName: "",
    templateType: ""
  };
  getDataList();
};

/**
 * 展示模板内容弹窗
 * @param templateInfo
 */
function showTemplateEditDialog(templateInfo: any) {
  let content = templateInfo.content;
  if (templateInfo.type == 1) {
    // 获取文件内容
  } else {
    // 字符串模板
  }
  templateContentEditorRef.value.init(templateInfo.templateName, content);
}

const submitTemplateParams = () => {
  const params = templateParamTableRef.value.getParams()
  if (params?.length > 0) {
    apiSaveOrUpdateTemplateParams(params).then((res) => {
      Message.info("保存成功")
    })
  }
}

onMounted(() => {
  getDataList();

  apiListTemplateTypes().then((res) => {
    templateTypes.value = res.data;
  });
});
</script>
