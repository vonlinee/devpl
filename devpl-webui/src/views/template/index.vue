<template>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.templateName" placeholder="项目名"></el-input>
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
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border height="450px"
              @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="templateId" label="模板ID" header-align="center" align="center"
                       width="80px"></el-table-column>
      <el-table-column prop="templateName" label="模板名称" header-align="center"
                       align="center"></el-table-column>
      <el-table-column prop="provider" label="技术类型" header-align="center" width="200px"
                       align="center"></el-table-column>
      <el-table-column prop="remark" label="备注" show-overflow-tooltip header-align="center"
                       align="center" width="300px"></el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="180">
        <template #default="scope">
          <el-button type="primary" link @click="showTemplateEditDialog(scope.row)">模板</el-button>
          <el-button v-if="!scope.row.internal" type="primary" link @click="addOrUpdateHandle(scope.row)">修改</el-button>
          <el-button v-if="!scope.row.internal" type="primary" link @click="deleteBatchHandle(scope.row.templateId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="state.page"
      :page-sizes="state.pageSizes || []"
      :page-size="state.limit"
      :total="state.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
    >
    </el-pagination>

    <template-viewer ref="templateContentEditorRef"></template-viewer>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>
  </el-card>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElButton } from "element-plus";
import AddOrUpdate from "./add-or-update.vue";
import { useCrud } from "@/hooks";
import { DataTableOption } from "@/hooks/interface";
import TemplateViewer from "@/views/template/TemplateViewer.vue";

const state: DataTableOption = reactive({
  dataListUrl: "/api/codegen/template/page",
  deleteUrl: "/api/codegen/template/delete/batch/ids",
  queryForm: {
    templateName: ""
  },
  primaryKey: "templateId",
  isPage: true
});

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state);

const addOrUpdateRef = ref();
const addOrUpdateHandle = (row?: any) => {
  addOrUpdateRef.value.init(row);
};

const templateContentEditorRef = ref();

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

onMounted(() => getDataList());
</script>
