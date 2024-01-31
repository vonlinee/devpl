<template>
  <!--  <TestPopup></TestPopup>-->

  <!--  <TestList></TestList>-->

  <!--  <TestJSON5Parser></TestJSON5Parser>-->

  <!-- <TableCreator></TableCreator> -->

  <!-- <TestDbTable></TestDbTable> -->

</template>

<script lang="ts" setup>

import TestPopup from "@/views/test/TestPopup.vue";
import TestList from "@/views/test/TestList.vue";
import TestJSON5Parser from "@/views/test/TestJSON5Parser.vue";
import TableCreator from "@/views/devtools/database/TableCreator.vue";


import { onMounted, ref } from "vue";
import { apiGetTableData } from "@/api/datasource";

const tableRef = ref()

const columns = ref<ResultSetColumnDef[]>([])

onMounted(() => {
  apiGetTableData({
    dataSourceId: 2,
    dbName: "devpl",
    tableName: "field_info"
  }).then((res) => {
    tableRef.value.refresh(res.data)
  })
})



</script>

<style lang="scss"></style>