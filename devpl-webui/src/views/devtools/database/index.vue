<template>
  <div>
    <el-card>

      <el-select v-model="currentDataSourceId" clearable @change="fireDatabaseChanged">
        <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
      </el-select>

    </el-card>
    <splitpanes vertical style="flex:1">
      <pane :max-size="30">
        <database-navigation-view ref="dbNavViewCRef" :data-source-id="currentDataSourceId"
          :node-click-callback="fillTableData"></database-navigation-view>
      </pane>
      <pane>
        <result-set-table :headers="tableHeader" :rows="tableData"></result-set-table>
      </pane>
    </splitpanes>
  </div>
</template>
<script setup lang="ts">

import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

import ResultSetTable from "./ResultSetTable.vue";
import DatabaseNavigationView from "./DatabaseNavigationView.vue";
import { nextTick, onMounted, ref } from "vue";
import { apiListSelectableDataSources } from "@/api/datasource";

const dbNavViewCRef = ref()
const currentDataSourceId = ref<number>();
const tableHeader = ref<ResultSetColumnMetadata[]>([])
const tableData = ref([])
const dataSources = ref<DataSourceVO[]>([]);

const fireDatabaseChanged = (newVal: number) => {
  dbNavViewCRef.value.loadDbSchemas(newVal)
}

onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSources.value = res.data;
    if (res.data.length > 0) {
      currentDataSourceId.value = res.data[0].id;
      nextTick(() => fireDatabaseChanged(currentDataSourceId.value))
    }
  });
});

const fillTableData = (data: DBTableDataVO) => {
  tableHeader.value = data.headers
  tableData.value = data.rows1 as any
}

</script>
<style lang="scss" scoped>
.splitpanes :deep(.splitpanes__splitter) {
  width: 4px;
}
</style>