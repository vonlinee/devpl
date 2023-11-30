<!--
 * @ 字段管理列表
-->
<script setup lang="ts">
import { apiDeleteFieldByIds, apiListFields } from "@/api/fields";
import FieldImportModal from "./FieldImportModal.vue";
import { onMounted, reactive, ref } from "vue";
import SaveOrUpdateField from "@/views/fields/SaveOrUpdateField.vue";
import { useCrud } from "@/hooks";
import { DataTableOption } from "@/hooks/interface";

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

const fieldSelectModal = ref();
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
    <el-table-column title="操作" align="center">
      <template #default="{ row }">
        <el-button link @click="showSaveOrUpdateModal(row)">编辑</el-button>
        <el-button link @click="removeEvent(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination :current-page="option.currentPage"
                 background
                 :page-sizes="option.pageSizes" :total="option.total"
                 layout="total, sizes, prev, pager, next, jumper"
                 @size-change="sizeChangeHandle" @current-change="currentChangeHandle">
  </el-pagination>

  <save-or-update-field ref="saveOrUpdateFieldModal" @refresh-table="getDataList"></save-or-update-field>

  <field-import-modal ref="fieldImportModalRef" @modal-close="getDataList"></field-import-modal>
</template>

<style scoped lang="scss">

.query-form {
  display: flex;
  align-items: center;
}

</style>
