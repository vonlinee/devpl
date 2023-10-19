<template>
  <el-table
    :data="list"
    class="mt-10"
    fit
    stripe
    empty-text="暂无数据"
    border
    :highlight-current-row="true"
  >
    <el-table-column
      v-for="(col, index) in columns"
      :key="index"
      :prop="col.prop"
      :label="col.label"
      :width="col.width"
    >
      <template slot-scope="scope">
        <template v-if="col.tag">
          <slot name="tags" :scope="scope.row"></slot>
        </template>
        <span v-else>{{ scope.row[col.prop] }}</span>
      </template>
    </el-table-column>

    <el-table-column label="操作">

    </el-table-column>
  </el-table>

</template>

<script lang="ts">

import { PropType } from "vue";

interface ColumnDef {
  label: string;
  prop: string;
  width: string | number;
  tag: boolean;
}

interface OperationProps {
  operate: boolean;
  label: string;
}

export default {
  name: "TablePage",
  props: {
    columns: [] as PropType<ColumnDef[]>,
    operates: [] as PropType<OperationProps[]>
  },
  data() {
    return {
      list: [],
      columns: [
        {
          label: "列1",
          prop: "name",
          width: "200",
          tag: false
        }
      ] as unknown as PropType<ColumnDef[]>
    };
  }
};

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.table_bsm {
  width: 98%;
  margin: auto;
}
</style>
  
  