<template>
  <table-page :list="list" :table_title="table_title" :operates="operates">

    <template v-slot:operates="scope">
      <table-operation :operations="operations" :rawData="scope.scope.row"
                       @handleOperation="handleOperation"></table-operation>
    </template>
  </table-page>
</template>

<script lang="js">

import { defineComponent } from "vue";
import TableOperation from "@/components/table/TableOperation.vue";
import TablePage from "@/components/table/TablePane.vue";

export default defineComponent({
  components: { TableOperation, TablePage },
  setup(props, ctx) {

    const list = [];
    const table_title = [{
      label: "姓名"
    }, {
      label: "年龄"
    }];

    const handleOperation = () => {
      console.log("handleOperation");
    };

    let operates = [];

    const operations = [];
    return {
      list,
      operations,
      table_title,
      operates,
      handleOperation
    };
  }
});


</script>