<!--
  字段选择弹窗
-->
<template>
  <vxe-modal title="字段选择" width="60%" v-model="visible" @close="handleClose" destroy-on-close>
    <el-form v-model="option.queryForm" :inline="true" class="demo-form-inline">
      <el-form-item label="关键词" prop="keyword">
        <el-input v-model="option.queryForm.keyword" placeholder="通过字段key，字段名称或者字段描述信息查找"></el-input>
      </el-form-item>
      <el-form-item label="数据类型" prop="region">
        <el-select v-model="option.queryForm.dataType">
          <el-option label="String" value="String" />
          <el-option label="int" value="Int" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table :border="true" height="450" :data="option.dataList" @selection-change="handleSelection">
      <el-table-column type="selection" width="40" header-align="center" align="center" fixed="left"></el-table-column>
      <el-table-column prop="fieldKey" label="Key" show-overflow-tooltip></el-table-column>
      <el-table-column prop="fieldName" label="名称" show-overflow-tooltip></el-table-column>
      <el-table-column prop="dataType" label="数据类型" show-overflow-tooltip></el-table-column>
    </el-table>
    <el-pagination background :page-sizes="option.pageSizes" :total="option.total" layout="prev, next"
                   @size-change="sizeChangeHandle" @current-change="currentChangeHandle">
    </el-pagination>
  </vxe-modal>
</template>

<script lang="ts" setup>

import { DataTableOption } from "@/hooks/interface";
import { reactive, ref } from "vue";
import { apiDeleteFieldByIds, apiListFields } from "@/api/fields";
import { useCrud } from "@/hooks";

const visible = ref();

const emits = defineEmits([
  "selection-change"
]);

/**
 * 选中回调
 * @param val
 */
const handleSelection = (val: FieldInfo[]) => {
  emits("selection-change", val);
};

const handleClose = () => {
  option.queryForm = {
    keyword: "",
    dataType: "String",
    excludedKeys: ""
  };
};

const option: DataTableOption = reactive({
  queryForm: {
    /**
     * 需要排除的字段列表，英文逗号分隔
     */
    excludedKeys: "",
    keyword: "",
    dataType: "String"
  },
  queryPage: apiListFields,
  removeByIds: apiDeleteFieldByIds
} as DataTableOption);

const { getDataList, sizeChangeHandle, currentChangeHandle } = useCrud(option);

defineExpose({
  show: (existed?: FieldInfo[]) => {
    visible.value = true;
    // 过滤已选择的字段
    if (existed) {
      option.queryForm.excludedKeys = existed.map((f) => f.fieldKey).join(",");
    }
    getDataList();
  }
});
</script>

<style lang="scss" scoped>
.demo-form-inline .el-input {
  --el-input-width: 420px;
}
</style>