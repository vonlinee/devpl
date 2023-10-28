<template>

  <el-select v-model="currentDataSourceId" clearable @change="loadDbTables">
    <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
  </el-select>

  <splitpanes vertical>
    <pane min-size="20">
      <el-tree :data="dataSource" :props="defaultProps" @node-click="handleNodeClick">
      </el-tree>
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
import type Node from "element-plus/es/components/tree/src/model/node";

const defaultProps = {
  children: "children",
  label: "label"
};

interface TreeNode {
  id?: number;
  label: string;
  children?: TreeNode[];
}

interface DataSourceVO {
  id: number,
  name: string
}

const dataSource = ref<TreeNode[]>([]);

const currentDataSourceId = ref<number>(0);
const dataSources = ref<DataSourceVO[]>([]);

/**
 * 节点点击事件
 * @param node
 * @param data
 * @param event
 */
const handleNodeClick = (node: Node, obj: any, data: TreeNode, event: Event) => {
  apiListTableNames(currentDataSourceId.value, data.label).then((res: AxiosResponse) => {
    if (!data.children) {
      data.children = [];
    }
    for (let i = 0; i < res.data.length; i++) {
      data.children?.push({
        label: res.data[i]
      });
    }
    dataSource.value = [...dataSource.value];
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
    dataSource.value = nodes;
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
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>