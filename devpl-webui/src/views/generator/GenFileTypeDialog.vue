<!--
 * @ 文件生成类型弹窗
-->
<script lang="ts" setup>
import { ref } from "vue";
import { ElTable } from "element-plus";
import { apiGetTemplateById, apiListSelectableTemplates } from "@/api/template";
import {
  apiDeleteGenFiles,
  apiListGenFiles,
  apiSaveOrUpdateGenFile
} from "@/api/generator";
import { ElMessage } from "element-plus/es";
import { hasText } from "@/utils/tool";

const dialogVisibleRef = ref(false);

// 默认的文件生成与模板对应关系
const tableData = ref<TargetGenFile[]>([]);

/**
 * 获取表格数据
 */
function refreshTableData() {
  apiListGenFiles().then((res) => {
    tableData.value = res.data;
  });
}

function init() {
  dialogVisibleRef.value = true;
  templateOptions.value = [];
  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data;
  });
  refreshTableData();
}

/**
 * 新增一行
 */
function addNewFileType() {
  tableData.value.push({
    fileName: "文件类型名称",
    templateId: undefined,
    remark: "",
    editing: true,
    builtin: false,
    templateName: "",
    typeName: ""
  });
}

defineExpose({
  init
});

let templateOptions = ref<TemplateInfo[]>([]);

function handleCurrentChange() {
}

function submit() {
  let len: number = tableData.value.length;
  let exit: boolean = true;
  for (let i = 0; i < len; i++) {
    if (tableData.value[i].editing) {
      exit = false;
    }
  }
  if (exit) {
    dialogVisibleRef.value = false;
  } else {
    ElMessage.error({
      message: "有数据行处于编辑状态中，请保存后再试",
      duration: 500
    });
  }
}

/**
 * 编辑行
 * @param row
 */
function editHandle(row: TargetGenFile) {
  row.editing = true;
}

/**
 * 保存行
 * @param row
 */
function saveHandle(row: TargetGenFile) {
  apiSaveOrUpdateGenFile(row).then((res) => {
    if (res.data) {
      ElMessage.info({
        message: "保存成功",
        duration: 500
      });
      row.editing = false;
    }
  });
}

/**
 * 删除行
 * @param rowIndex
 */
function deleteHandle(rowIndex: number) {
  let genFile: TargetGenFile = tableData.value[rowIndex];
  if (genFile.id) {
    apiDeleteGenFiles([genFile]).then((res) => {
      if (res.data) {
        tableData.value.splice(rowIndex, 1);
      }
    });
  } else {
    tableData.value.splice(rowIndex, 1);
  }
}

/**
 * 新增一行时，获取模板名称
 * 选择框绑定的值是模板ID，但是显示的值是模板名称
 * @param row
 */
function fillTemplateName(row: TargetGenFile) {
  if (!hasText(row.templateName)) {
    if (row.templateId) {
      apiGetTemplateById(row.templateId).then((res) => {
        row.templateName = res.data?.templateName || "";
      });
    }
  }
}
</script>

<template>
  <vxe-modal
    v-model="dialogVisibleRef"
    title="目标生成文件类型管理"
    :draggable="false"
    destroy-on-close
    :show-footer="true"
    width="80%"
    height="80%"
    :mask-closable="false"
  >
    <el-table
      ref="singleTableRef"
      border
      :data="tableData"
      table-layout="auto"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column property="fileName" label="文件类型名称" width="100">
        <template #default="scope">
          <el-text v-if="!scope.row.editing">{{ scope.row.typeName }}</el-text>
          <el-input
            v-if="scope.row.editing"
            v-model="scope.row.typeName"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="builtin"
        align="center"
        label="是否内置"
        width="80px"
        min-width="60px"
      >
        <template #default="scope">
          <el-checkbox
            v-model="scope.row.builtin"
            :disabled="!scope.row.editing"
            size="large"
          />
        </template>
      </el-table-column>
      <el-table-column label="模板" width="120px">
        <template #default="scope">
          <div>
            <el-text v-if="!scope.row.editing">{{
                scope.row.templateName
              }}
            </el-text>
            <el-select
              v-if="scope.row.editing"
              v-model="scope.row.templateId"
              class="m-2"
              placeholder="选择模板"
              filterable
              @change="fillTemplateName(scope.row)"
            >
              <el-option
                v-for="item in templateOptions"
                :key="item.templateId"
                :label="item.templateName"
                :value="item.templateId"
              />
            </el-select>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        property="fileName"
        label="文件名称"
        width="240"
        show-overflow-tooltip
      >
        <template #default="scope">
          <el-text v-show="!scope.row.editing" v-text="scope.row.fileName" />
          <el-input
            v-show="scope.row.editing"
            v-model="scope.row.fileName"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
        property="savePath"
        label="保存路径"
        show-overflow-tooltip
      >
        <template #default="scope">
          <el-text v-show="!scope.row.editing" v-text="scope.row.savePath" />
          <el-input
            v-show="scope.row.editing"
            v-model="scope.row.savePath"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
        property="remark"
        label="描述信息"
        width="200"
        show-overflow-tooltip
      >
        <template #default="scope">
          <el-text v-show="!scope.row.editing" v-text="scope.row.remark" />
          <el-input
            v-show="scope.row.editing"
            v-model="scope.row.remark"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        fixed="right"
        header-align="center"
        align="center"
        width="150"
      >
        <template #default="scope">
          <el-button
            v-if="!scope.row.editing"
            type="primary"
            link
            @click="editHandle(scope.row)"
          >编辑
          </el-button>
          <el-button
            v-if="scope.row.editing"
            type="primary"
            link
            @click="saveHandle(scope.row)"
          >保存
          </el-button>
          <el-button
            v-if="!scope.row.builtin"
            type="primary"
            link
            @click="deleteHandle(scope.$index)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button type="primary" @click="refreshTableData()">刷新</el-button>
      <el-button type="info" @click="addNewFileType()">新增</el-button>
      <el-button type="success" @click="submit()">确认</el-button>
      <el-button type="danger" @click="dialogVisibleRef = false"
      >取消
      </el-button
      >
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
