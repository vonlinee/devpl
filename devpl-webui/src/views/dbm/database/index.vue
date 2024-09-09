<template>
  <div style="display: flex; flex-direction: column; height: 100%;">
    <el-card>
      <el-select v-model="currentDataSourceId" clearable @change="fireDatabaseChanged">
        <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
      </el-select>
    </el-card>

    <div>
      <splitpanes ref="paneRef" vertical>
        <pane :max-size="30">
          <database-navigation-view ref="dbNavViewCRef" :data-source-id="currentDataSourceId"
            :node-click-callback="fillTableData"></database-navigation-view>
        </pane>
        <pane>
          <result-set-table ref="resultSetTableRef"></result-set-table>
        </pane>
      </splitpanes>
    </div>
  </div>
</template>
<script setup lang="ts">
import { Pane, Splitpanes } from "splitpanes"
import "splitpanes/dist/splitpanes.css"

import ResultSetTable from "./ResultSetTable.vue"
import DatabaseNavigationView from "./DatabaseNavigationView.vue"
import { nextTick, onMounted, ref } from "vue"
import { apiListSelectableDataSources } from "@/api/datasource"

const dbNavViewCRef = ref()
const resultSetTableRef = ref()
const currentDataSourceId = ref<number>()
const dataSources = ref<DataSourceVO[]>([])

const paneRef = ref<HTMLElement>()

const fireDatabaseChanged = (newVal: number) => {
  dbNavViewCRef.value.loadDbSchemas(newVal)
}

onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSources.value = res.data || []
    if (dataSources.value.length > 0) {
      currentDataSourceId.value = dataSources.value[0].id
      nextTick(() => fireDatabaseChanged(currentDataSourceId.value!))
    }
  })
})

const fillTableData = (data: DBTableDataVO) => {
  resultSetTableRef.value.refresh(data)
}
</script>
<style lang="scss" scoped>
.splitpanes :deep(.splitpanes__splitter) {
  width: 4px;
}
</style>
