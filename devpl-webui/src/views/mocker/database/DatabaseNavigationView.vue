<template>
  <div style="height: 100%; overflow-y: scroll;">
    <el-tree :data="dataSource" :props="defaultProps" :load="loadDbTables" lazy @node-click="nodeClickHandler"
      hegiht="100%">
      <template #default="{ node, data }">
        <span class="custom-tree-node" @contextmenu="displayContextMenu($event)">
          <span v-contextmenu:contextmenu>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </div>

  <Contextmenu ref="contextmenu">
    <contextmenu-item>数据模拟</contextmenu-item>
    <contextmenu-item>DDL</contextmenu-item>
    <contextmenu-item>菜单3</contextmenu-item>
  </Contextmenu>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { apiGetDatabaseNamesById, apiListTableNames } from "@/api/datasource";
import type Node from "element-plus/es/components/tree/src/model/node";
import { Contextmenu, ContextmenuItem } from "v-contextmenu";

const contextMenuRef = ref()

const displayContextMenu = (event: Event) => {
  contextMenuRef.value.show(event)
}

const defaultProps = {
  label: 'label',
  children: 'children',
  isLeaf: 'leaf',
};

const dataSource = ref<TreeNodeVO[]>([]);

interface DatabaseNavigationViewProps {
  dataSourceId: number,
  nodeClickCallback: (param: DBTableDataVO) => void,
}

const props = defineProps<DatabaseNavigationViewProps>()

const dataSourceId = computed(() => {
  return props.dataSourceId
})

/**
 * 加载数据库表
 * @param node 
 * @param resolve 
 */
const loadDbTables = (node: Node, resolve: (data: TreeNodeVO[]) => void) => {
  apiListTableNames(dataSourceId.value, node.label).then((res) => {
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

/**
 * 节点点击
 * @param data 
 * @param node 
 * @param item 
 * @param param 
 */
const nodeClickHandler = (data: TreeNodeVO, node: Node, item: any, param: any) => {
  // if (node.level == 2) {
  //   const apiParam: ParamGetDbTableData = {
  //     dataSourceId: dataSourceId.value,
  //     dbName: node.parent.label,
  //     tableName: node.label,
  //     pageIndex: 1,
  //     pageSize: 100
  //   }
  //   apiGetTableData(apiParam).then((res) => {
  //     if (res.data) {
  //       props.nodeClickCallback(res.data)
  //     }
  //   })
  // }
}

/**
 * 加载数据库列表
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

defineExpose({
  loadDbSchemas: loadDatabases
})

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

.card {
  background: var(--surface-card);
  padding: 2rem;
  border-radius: 10px;
  margin-bottom: 1rem;
}

p {
  line-height: 1.75;
}
</style>