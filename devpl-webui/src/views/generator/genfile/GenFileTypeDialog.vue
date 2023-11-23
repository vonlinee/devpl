<!--
 * @ 文件生成类型弹窗
 * @author Von
 * @date 2023/7/20 21:08
-->
<script lang="ts" setup>
import { ref } from "vue";
import { ElButton, ElTable } from "element-plus";
import { apiGetTemplateById, apiListSelectableTemplates } from "@/api/template";
import { apiDeleteGenFiles, apiListGenFiles, apiSaveOrUpdateGenFile } from "@/api/generator";
import { ElMessage } from "element-plus/es";
import { hasText } from "@/utils/tool";

const dialogVisiableRef = ref(false);

// 表格
const singleTableRef = ref<InstanceType<typeof ElTable>>();

// 默认的文件生成与模板对应关系
const tableData = ref<GenFile[]>([]);

/**
 * 获取表格数据
 */
function refreshTableData() {
  apiListGenFiles().then((res) => {
    tableData.value = res.data;
  });
}

function init() {
  dialogVisiableRef.value = true;
  templateOptions.value = [];
  apiListSelectableTemplates().then(res => {
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
    pid: 0,
    editing: true,
    builtin: false,
    templateName: ""
  });
}

defineExpose({
  init
});

let templateOptions = ref([]);

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
    dialogVisiableRef.value = false;
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
function editHandle(row: GenFile) {
  row.editing = true;
}

/**
 * 保存行
 * @param row
 */
function saveHandle(row: GenFile) {
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
  let genFile: GenFile = tableData.value[rowIndex];
  if (genFile.pid) {
    apiDeleteGenFiles([genFile]).then(res => {
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
function fillTemplateName(row: GenFile) {
  if (!hasText(row.templateName)) {
    if (row.templateId) {
      apiGetTemplateById(row.templateId).then((res) => {
        row.templateName = res.data.templateName;
      });
    }
  }
}

</script>

<template>
  <vxe-modal v-model="dialogVisiableRef" title="目标生成文件类型管理" draggable destroy-on-close :show-footer="true"
             width="80%"
             :mask-closable="false">
    <el-table ref="singleTableRef" :data="tableData" table-layout="auto" highlight-current-row style="width: 100%"
              height="500px" @current-change="handleCurrentChange">
      <el-table-column type="index" />
      <el-table-column property="fileName" label="文件类型">
        <template #default="scope">
          <el-text v-if="!scope.row.editing">{{ scope.row.fileName }}</el-text>
          <el-input v-if="scope.row.editing" v-model="scope.row.fileName"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="builtin" align="center" label="是否内置" width="100px" min-width="100px">
        <template #default="scope">
          <el-checkbox v-model="scope.row.builtin" :disabled="!scope.row.editing" size="large" />
        </template>
      </el-table-column>

      <el-table-column label="模板">
        <template #default="scope">
          <div>
            <el-text v-if="!scope.row.editing">{{ scope.row.templateName }}</el-text>
            <el-select v-if="scope.row.editing" v-model="scope.row.templateId" class="m-2" placeholder="选择模板"
                       @change="fillTemplateName(scope.row)" filterable>
              <el-option v-for="item in templateOptions" :key="item.templateId" :label="item.templateName"
                         :value="item.templateId" />
            </el-select>
          </div>
        </template>
      </el-table-column>
      <el-table-column property="remark" label="描述信息" width="240">
        <template #default="scope">
          <el-text v-show="!scope.row.editing" v-text="scope.row.remark" />
          <el-input v-show="scope.row.editing" v-model="scope.row.remark"></el-input>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="250">
        <template #default="scope">
          <el-button v-if="!scope.row.editing" type="primary" link @click="editHandle(scope.row)">编辑
          </el-button>
          <el-button v-if="scope.row.editing" type="primary" link @click="saveHandle(scope.row)">保存
          </el-button>
          <el-button type="primary" link @click="deleteHandle(scope.$index)" v-if="!scope.row.builtin">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button type="primary" @click="refreshTableData()">刷新</el-button>
      <el-button type="info" @click="addNewFileType()">新增</el-button>
      <el-button type="success" @click="submit()">确认</el-button>
      <el-button type="danger" @click="dialogVisiableRef = false">取消</el-button>
    </template>
  </vxe-modal>
</template>

<style scoped lang="scss"></style>
