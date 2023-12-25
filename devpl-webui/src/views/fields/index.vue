<!--
 * @ 字段管理列表
-->
<script setup lang="ts">
import { apiDeleteFieldByIds, apiListFields, apiSaveBatchFields } from "@/api/fields";
import FieldParseToolModal from "./FieldParseToolModal.vue";
import { onMounted, reactive, ref } from "vue";
import SaveOrUpdateField from "@/views/fields/SaveOrUpdateField.vue";
import { useCrud } from "@/hooks";
import { DataTableOption } from "@/hooks/interface";
import { ElMessage } from "element-plus";

/**
 * 表格数据模型
 */
interface RowVO {
  id: number;
  fieldId: string;
  fieldKey: string;
  fieldName: string;
  dataType: string;
  description: string;
  defaultValue: string;
}

const fieldImportModalRef = ref();
const saveOrUpdateFieldModal = ref();

const showSaveOrUpdateModal = (row?: RowVO) => {
  saveOrUpdateFieldModal.value.show(row);
};

const removeEvent = async (row: RowVO) => {
  deleteHandle(row.id);
};

onMounted(() => {
  getDataList();
});

const option: DataTableOption = reactive({
  pageSizes: [10, 15, 20],
  queryForm: {
    fieldKey: "",
    fieldName: ""
  },
  queryPage: apiListFields,
  removeByIds: apiDeleteFieldByIds
} as DataTableOption);

const { getDataList, sizeChangeHandle, currentChangeHandle, deleteHandle } = useCrud(option);

/**
 * 保存解析的字段
 * @param parsedFields
 */
const handleFieldParseFinished = (parsedFields: FieldInfo[]) => {
  if (parsedFields && parsedFields.length > 0) {
    apiSaveBatchFields(parsedFields).then((res) => {
      ElMessage.info({
        message: "新增成功",
        duration: 500,
        onClose: () => {
          getDataList();
        }
      });
    });
  }
};

</script>

<template>
  <el-card>
    <el-form :inline="true" :model="option.queryForm" @keyup.enter="getDataList()">
      <div class="query-form">
        <el-form-item label="字段Key" :show-message="false">
          <el-input v-model="option.queryForm.fieldKey"></el-input>
        </el-form-item>
        <el-form-item label="字段名称" :show-message="false">
          <el-input v-model="option.queryForm.fieldName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="option.queryForm = {}">重置</el-button>
          <el-button type="primary" @click="getDataList()">查询</el-button>
          <el-button type="primary" @click="showSaveOrUpdateModal(null)">新增</el-button>
          <el-button type="primary" @click="fieldImportModalRef.init()">导入</el-button>
        </el-form-item>
      </div>
    </el-form>
  </el-card>

  <el-table :border="true" height="525" :data="option.dataList">
    <el-table-column type="selection" width="60" header-align="center" align="center"></el-table-column>
    <el-table-column prop="fieldKey" label="字段Key"></el-table-column>
    <el-table-column prop="fieldName" label="名称"></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
    <el-table-column prop="defaultValue" label="默认值" show-overflow-tooltip></el-table-column>
    <el-table-column prop="description" label="描述信息" show-overflow-tooltip></el-table-column>
    <el-table-column label="操作" align="center" fixed="right" width="130">
      <template #default="{ row }">
        <el-button link @click="showSaveOrUpdateModal(row)">编辑</el-button>
        <el-button link @click="removeEvent(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination :current-page="option.page"
                 background
                 :page-size="option.limit"
                 :page-sizes="option.pageSizes" :total="option.total"
                 layout="total, sizes, prev, next, jumper"
                 @size-change="sizeChangeHandle" @current-change="currentChangeHandle">
  </el-pagination>

  <save-or-update-field ref="saveOrUpdateFieldModal" @refresh-table="getDataList"></save-or-update-field>

  <field-parse-tool-modal ref="fieldImportModalRef" @finished="handleFieldParseFinished"></field-parse-tool-modal>
</template>

<style scoped lang="scss">

.query-form {
  display: flex;
  align-items: center;
}

</style>
