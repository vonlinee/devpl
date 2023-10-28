<template>

  <el-select v-model="currentDataSource" clearable @change="loadDbTables">
    <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
  </el-select>

  <splitpanes vertical>
    <pane min-size="20">
      <database-tree-view></database-tree-view>
    </pane>
    <pane>
        
      <vxe-grid></vxe-grid>
    </pane>
  </splitpanes>
</template>
<script setup lang='ts'>

import { Splitpanes, Pane } from 'splitpanes';
import 'splitpanes/dist/splitpanes.css'
import DatabaseTreeView from './DatabaseTreeView.vue'
import { onMounted, ref } from 'vue';
import { apiGetDatabaseNamesById, apiListSelectableDataSources } from '@/api/datasource';
import { AxiosResponse } from 'axios';

interface DataSourceVO {
  id: number,
  name: string
}

const currentDataSource = ref()
const dataSources = ref<DataSourceVO[]>([])

const loadDbTables = (val: number) => {
  apiGetDatabaseNamesById(val).then((res: AxiosResponse) => {
    console.log(res);
    
  })
}




onMounted(() => {
  apiListSelectableDataSources().then((res : AxiosResponse<DataSourceVO[]>) => {
    dataSources.value = res.data
    if (res.data.length > 0) {
      currentDataSource.value = res.data[0].name
    }
  })
})

</script>
<style lang='scss' scoped>

</style>