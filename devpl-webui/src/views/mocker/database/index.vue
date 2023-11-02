<template>
  <el-select v-model="currentDataSourceId" clearable @change="loadDatabases">
    <el-option v-for="ds in dataSources" :key="ds.id" :value="ds.id" :label="ds.name"></el-option>
  </el-select>

  <splitpanes vertical>
    <pane>
      <div>
        <el-tree :data="dataSource" :props="defaultProps" :load="loadDbTables" lazy @node-click="nodeClickHandler">
      </el-tree>
      </div>
    </pane>
    <pane>
      <div>
        <result-set-table :headers="tableHeader" :rows="tableData"></result-set-table>
      </div>
    </pane>
  </splitpanes>
</template>
<script setup lang="ts">

import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import { onMounted, ref } from "vue";
import { apiGetDatabaseNamesById, apiGetTableData, apiListSelectableDataSources, apiListTableNames } from "@/api/datasource";
import type Node from "element-plus/es/components/tree/src/model/node";
import { ParamGetDbTableData } from "./type";
import ResultSetTable from "./ResultSetTable.vue";

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

/**
 * 加载数据库
 * @param val 
 */
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
    const tableNodes: TreeNodeVO[] = []
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

const tableHeader = ref()
const tableData = ref()

const nodeClickHandler = (data: TreeNodeVO, node: Node, item: any, param: any) => {

  if (node.level == 2) {


    const apiParam: ParamGetDbTableData = {
      dataSourceId: currentDataSourceId.value,
      dbName: node.parent.label,
      tableName: node.label,
      pageIndex: 1,
      pageSize: 100
    }

    apiGetTableData(apiParam).then((res) => {
      if (res.data) {
        tableHeader.value = res.data.headers
        tableData.value = res.data.rows1
      }
    })
  }
}

</script>
<style lang="scss" >
// .custom-tree-node {
//   flex: 1;
//   display: flex;
//   align-items: center;
//   justify-content: space-between;
//   font-size: 14px;
//   padding-right: 8px;
// }

// .splitpanes__splitter {
//   background-color: red;
//   width: 20px;
// }

.splitpanes .splitpanes__splitter {
  width: 4px;
}

.splitpanes .splitpanes__splitter:hover {
  
}
</style>