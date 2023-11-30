<!--
  字段选择表
-->
<template>
  <el-table ref="fieldsTable" :border="true" height="525" :data="fields" @selection-change="handleSelection">
    <el-table-column type="selection" width="60" header-align="center" align="center"></el-table-column>
    <el-table-column prop="fieldKey" label="Key"></el-table-column>
    <el-table-column prop="fieldName" label="名称"></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
  </el-table>
  <el-pagination background :page-sizes="option.pageSizes" :total="option.total" layout="prev, next"
    @size-change="sizeChangeHandle" @current-change="currentChangeHandle">
  </el-pagination>
</template>

<script lang="ts" setup>

import { DataTableOption } from "@/hooks/interface";
import { onMounted, reactive, ref } from "vue";
import { apiDeleteFieldByIds, apiListFields } from "@/api/fields";
import { useCrud } from "@/hooks";

const option: DataTableOption = reactive({
  queryForm: {
    fieldKey: "",
    fieldName: ""
  },
  queryPage: apiListFields,
  removeByIds: apiDeleteFieldByIds
} as DataTableOption);

const selectedFields = ref<FieldInfo[]>();

const fields = ref<FieldInfo[]>()

const { getDataList, sizeChangeHandle, currentChangeHandle } = useCrud(option);

const emits = defineEmits([
  "selection-change"
]);

const handleSelection = (val: FieldInfo[]) => {
  emits("selection-change", val);
};

const { existed } = defineProps<{
  existed?: FieldInfo[]
}>();

onMounted(() => {
  getDataList();
  console.log(option.dataList);
  fields.value = option.dataList
});

defineExpose({
  getSelectedFields: () => {
    return selectedFields.value;
  }
});

</script>

<style scoped></style>