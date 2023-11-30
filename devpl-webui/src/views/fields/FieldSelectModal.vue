<template>
  <vxe-modal title="字段选择" width="50%" v-model="visible" @close="handleClose" destroy-on-close :mask="false">
    <el-table :border="true" height="525" :data="option.dataList" @selection-change="handleSelection">
      <el-table-column type="selection" width="40" header-align="center" align="center"></el-table-column>
      <el-table-column prop="fieldKey" label="Key"></el-table-column>
      <el-table-column prop="fieldName" label="名称" width="200"></el-table-column>
      <el-table-column prop="dataType" label="数据类型" width="200"></el-table-column>
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
import { sub } from "@/utils/tool";

const visible = ref();

const emits = defineEmits([
  "selection-change"
]);

const handleSelection = (val: FieldInfo[]) => {
  emits("selection-change", val);
  // 删除选中的元素

  if (option.dataList) {
    option.dataList = sub(option.dataList, val)
  }
};

const handleClose = () => {
  option.queryForm = {
    fieldKey: "",
    fieldName: "",
    excludedKeys: ""
  };
};

const option: DataTableOption = reactive({
  queryForm: {
    fieldKey: "",
    fieldName: "",
    excludedKeys: ""
  },
  queryPage: apiListFields,
  removeByIds: apiDeleteFieldByIds
} as DataTableOption);

const { getDataList, sizeChangeHandle, currentChangeHandle } = useCrud(option);

defineExpose({
  show: (existed?: FieldInfo[]) => {
    visible.value = true;
    if (existed) {
      option.queryForm.excludedKeys = existed.map((f) => f.fieldKey).join(",");
    }
    getDataList();
  }
});
</script>

<style scoped></style>