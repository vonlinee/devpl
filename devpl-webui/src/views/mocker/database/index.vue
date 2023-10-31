<template>
  <el-select v-model="currentDataSourceId" clearable @change="loadDatabases">
    <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
  </el-select>

  <splitpanes vertical>
    <pane min-size="20">
      <el-tree :data="dataSource" :props="defaultProps" :load="loadDbTables" lazy show-checkbox>
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
import type Node from "element-plus/es/components/tree/src/model/node";

const defaultProps = {
  label: 'label',
  children: 'children',
  isLeaf: 'leaf',
};

interface TreeNodeVO {
  /**
   * 唯一ID
   */
  id?: number;
  /**
   * 文本
   */
  label: string;
  /**
   * 是否是叶子结点
   */
  leaf: boolean
  /**
   * 子节点
   */
  children?: TreeNodeVO[];
}

interface DataSourceVO {
  id: number,
  name: string
}

const dataSource = ref<TreeNodeVO[]>([]);

const currentDataSourceId = ref<number>(0);
const dataSources = ref<DataSourceVO[]>([]);

const loadDatabases = (val: number) => {
  apiGetDatabaseNamesById(val).then((res) => {
    const nodes: TreeNodeVO[] = [];
    for (let i = 0; i < res.data.length; i++) {
      nodes.push({
        label: res.data[i],
        leaf: false
      });
    }
    dataSource.value = nodes;
  });
};

/**
 * 加载数据库表
 * @param node 
 * @param resolve 
 */
const loadDbTables = (node: Node, resolve: (data: TreeNodeVO[]) => void) => {
  apiListTableNames(currentDataSourceId.value, node.label).then((res) => {
    const tableNodes : TreeNodeVO[] = []
    for (let i = 0; i < res.data?.length; i++) {
      tableNodes.push({
        label: res.data[i],
        leaf: true
      });
    }
    resolve(tableNodes)
  });
}

onMounted(() => {
  apiListSelectableDataSources().then((res) => {
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