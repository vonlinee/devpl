<!--
 * @ 文件生成类型弹窗
-->
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { ElTable } from "element-plus";
import { apiGetTemplateById, apiListSelectableTemplates } from "@/api/template";
import {
  apiDeleteGenFiles,
  apiListGenFiles,
  apiSaveOrUpdateGenFile
} from "@/api/generator";
import { ElMessage } from "element-plus/es";
import TemplateViewer from "@/views/template/TemplateViewer.vue";

const tableRef = ref();
const templateContentEditorRef = ref();
const expandedRowKeys = ref<number[]>([])
// 默认的文件生成与模板对应关系
const tableData = ref<TargetGenFile[]>([]);
const templateOptions = ref<TemplateInfo[]>([]);

/**
 * 获取表格数据
 */
function refreshTableData() {
  apiListGenFiles().then((res) => {
    tableData.value = res.data;
  });
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
    typeName: "",
    defaultTarget: false
  });
}

function init() {
  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data;
  });
  refreshTableData();
}

function handleCurrentChange() {
}

/**
 * 编辑行
 * @param row
 */
function editHandle(row: TargetGenFile, event: Event) {
  event.stopPropagation()
  row.editing = true;
}

/**
 * 保存行
 * @param row
 */
function saveHandle(row: TargetGenFile, event: Event) {
  event.stopPropagation()
  // tableRef.value.toggleRowExpansion(row)

  apiSaveOrUpdateGenFile(row).then((res) => {
    if (res.data) {
      ElMessage.info({
        message: "保存成功",
        duration: 500
      });
    }
    row.editing = false;
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
function fillTemplateName(templateId: number, row: TargetGenFile) {
  const targetOption = templateOptions.value.filter(option => option.templateId == templateId)[0];
  if (targetOption) {
    row.templateId = templateId
    row.templateName = targetOption.templateName
  }
}

onMounted(() => {
  init()
})

/**
 * 单击行进行展开
 * @param row 行数据
 * @param column 列信息
 * @param event 点击事件
 */
const handleRowClicked = (row: TargetGenFile, column: any, event: Event) => {
  if (column.no == 2 || column.no == 3) {
    return
  }

  if (row.id != undefined) {
    if (expandedRowKeys.value.includes(row.id)) {
      expandedRowKeys.value = expandedRowKeys.value.filter((val: number) => val !== row.id);
    } else {
      expandedRowKeys.value.push(row.id);
    }
  }
}

/**
 * 预览模板
 * @param row 
 */
const previewTemplate = (event : MouseEvent, row: TargetGenFile) => {
  event.stopPropagation()
  if (row.templateId != undefined) {
    apiGetTemplateById(row.templateId).then((res) => {
      templateContentEditorRef.value.init(res.data?.templateName, res.data?.content)
    })
  }
}

</script>

<template>
  <el-card>
    <el-button type="primary" @click="refreshTableData()">刷新</el-button>
    <el-button type="info" @click="addNewFileType()">新增</el-button>
  </el-card>
  <el-table ref="tableRef" border :data="tableData" table-layout="auto" highlight-current-row row-key="id"
    :expand-row-keys="expandedRowKeys" @current-change="handleCurrentChange" @row-click="handleRowClicked">
    <el-table-column type="expand">
      <template #default="scope">
        <el-form style="margin-left: 50px;" label-width="100" label-position="left">
          <el-form-item label="文件名称">
            <el-text v-show="!scope.row.editing" v-text="scope.row.fileName" />
            <el-input v-show="scope.row.editing" v-model="scope.row.fileName"></el-input>
          </el-form-item>
          <el-form-item label="保存路径">
            <el-text v-show="!scope.row.editing" v-text="scope.row.savePath" />
            <el-input v-show="scope.row.editing" v-model="scope.row.savePath"></el-input>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <el-table-column property="fileName" label="文件类型" width="200" show-overflow-tooltip>
      <template #default="scope">
        <el-text v-if="!scope.row.editing">{{ scope.row.typeName }}</el-text>
        <el-input v-if="scope.row.editing" v-model="scope.row.typeName"></el-input>
      </template>
    </el-table-column>
    <el-table-column prop="builtin" align="center" label="是否内置" width="90px" min-width="60px">
      <template #default="scope">
        <el-checkbox v-model="scope.row.builtin" :disabled="!scope.row.editing" />
      </template>
    </el-table-column>
    <el-table-column prop="defaultTarget" align="center" label="是否默认生成" width="120px" min-width="120px">
      <template #default="scope">
        <el-checkbox v-model="scope.row.defaultTarget" :disabled="!scope.row.editing" />
      </template>
    </el-table-column>
    <el-table-column label="模板" width="200px" show-overflow-tooltip>
      <template #default="scope">
        <a v-if="!scope.row.editing" truncated @click="previewTemplate($event, scope.row)" style="color: blue;">{{
          scope.row.templateName }}
        </a>
        <el-select v-if="scope.row.editing" v-model="scope.row.templateName" class="m-2" placeholder="选择模板" filterable
          @change="(val) => fillTemplateName(val, scope.row)">
          <el-option v-for="item in templateOptions" :key="item.templateId" :label="item.templateName"
            :value="item.templateId!">
          </el-option>
        </el-select>
      </template>
    </el-table-column>

    <el-table-column property="remark" label="描述信息" show-overflow-tooltip>
      <template #default="scope">
        <el-text v-show="!scope.row.editing" v-text="scope.row.remark" />
        <el-input v-show="scope.row.editing" v-model="scope.row.remark"></el-input>
      </template>
    </el-table-column>
    <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
      <template #default="scope">
        <el-button v-if="!scope.row.editing" type="primary" link @click="editHandle(scope.row, $event)">编辑
        </el-button>
        <el-button v-if="scope.row.editing" type="primary" link @click="saveHandle(scope.row, $event)">保存
        </el-button>
        <el-button v-if="!scope.row.builtin" type="primary" link @click="deleteHandle(scope.$index)">删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <template-viewer ref="templateContentEditorRef"></template-viewer>
</template>

<style scoped lang="scss"></style>
