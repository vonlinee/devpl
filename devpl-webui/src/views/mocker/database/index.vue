<template>

  <el-select v-model="currentDataSourceId" clearable @change="loadDbTables">
    <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
  </el-select>

  <splitpanes vertical>
    <pane min-size="20">
      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" />
    </pane>
    <pane>

      <vxe-grid></vxe-grid>
    </pane>
  </splitpanes>
</template>
<script setup lang="ts">

import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import { onMounted, ref } from "vue";
import { apiGetDatabaseNamesById, apiListSelectableDataSources, apiListTableNames } from "@/api/datasource";
import { AxiosResponse } from "axios";

const defaultProps = {
  children: "children",
  label: "label"
};

interface TreeNode {
  label: string;
  children?: TreeNode[];
}


interface DataSourceVO {
  id: number,
  name: string
}

const data = ref<TreeNode[]>([]);

const currentDataSourceId = ref<number>(0);
const dataSources = ref<DataSourceVO[]>([]);


const handleNodeClick = (data: TreeNode) => {
  apiListTableNames(currentDataSourceId.value, data.label).then((res: AxiosResponse) => {

  });
};

const loadDbTables = (val: number) => {
  apiGetDatabaseNamesById(val).then((res: AxiosResponse) => {
    let nodes: TreeNode[] = [];
    for (let i = 0; i < res.data.length; i++) {
      nodes.push({
        label: res.data[i]
      });
    }
    data.value = nodes;
  });
};


onMounted(() => {
  apiListSelectableDataSources().then((res: AxiosResponse<DataSourceVO[]>) => {
    dataSources.value = res.data;
    if (res.data.length > 0) {
      currentDataSourceId.value = res.data[0].id;
    }
  });
});

</script>
<style lang="scss" scoped>

</style>