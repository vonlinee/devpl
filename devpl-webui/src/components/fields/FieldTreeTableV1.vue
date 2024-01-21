<script setup lang="ts">
import DraggableTreeTable from "@/components/treetable/DraggableTreeTable.vue";
import { onMounted, ref } from "vue";
import { apiListAllFields } from "@/api/fields";

const treeData = ref({
  lists: [],
  columns: [
    {
      type: "selection",
      title: "字段Key",
      field: "fieldKey",
      width: 200,
      align: "left",
      titleAlign: "left"
    },
    {
      title: "名称",
      field: "fieldName",
      width: 100,
      align: "center"
    },
    {
      title: "数据类型",
      field: "dataType",
      width: 200,
      align: "center"
    },
    {
      title: "注释",
      field: "comment",
      width: 200,
      align: "center"
    }
  ]
});

onMounted(() => {
  apiListAllFields().then((res) => {
    treeData.value.lists = res.data;
  });
});

const addRow = (row: any) => {

};
</script>

<template>
  <DraggableTreeTable ref="table" :data="treeData" :draggable="true">

    <template #action="{ row }">
      <a class="action-item" @click.stop.prevent="addRow(row)">添加子节点</a>
    </template>
  </DraggableTreeTable>
</template>

<style scoped lang="scss">

</style>