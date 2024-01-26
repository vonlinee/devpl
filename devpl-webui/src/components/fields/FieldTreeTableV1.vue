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
    },
    {
      title: "操作",
      type: "action",
      flex: 1,
      align: "center",
      actions: [
        {
          text: "添加子节点",
          onclick: (item: { id: any; }) => {

          },
          formatter: (item: any) => {
            return "<i>添加子节点</i>";
          }
        },
        {
          text: "删除",
          onclick: (row) => {

          },
          formatter: item => {
            // console.log(item);
            return "<i>删除</i>";
          }
        }
      ]
    }
  ]
});

onMounted(() => {
  apiListAllFields().then((res) => {
    treeData.value.lists = res.data;
  });
});

const saveOrUpdate = (row: any) => {

};
</script>

<template>
  <DraggableTreeTable ref="table" :data="treeData" :draggable="true" resize>
    <template #action="{ row }">
      <a class="action-item" @click.stop.prevent="saveOrUpdate(row)">添加子节点</a>
      <a class="action-item" @click.stop.prevent="saveOrUpdate(row)"><i>删除</i></a>
    </template>
  </DraggableTreeTable>
</template>

<style scoped lang="scss">

</style>