<template>
  <el-tree :data="dataSource" :props="defaultProps" :load="loadDbTables" lazy @node-click="nodeClickHandler" hegiht="100%"
    @node-contextmenu="onContextMenuRequest">
    <template #default="{ node, data }">
      <span class="custom-tree-node">
        <span>{{ node.label }}</span>
      </span>
    </template>
  </el-tree>

  <vue-simple-context-menu element-id="myUniqueId" :options="options" ref="vueSimpleContextMenu"
    @option-clicked="optionClicked" />
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { apiGetDatabaseNamesById, apiGetTableData, apiListTableNames } from "@/api/datasource";
import type Node from "element-plus/es/components/tree/src/model/node";
import { DBTableDataVO, ParamGetDbTableData } from "./type";

const vueSimpleContextMenu = ref()

const handleClick = (event: Event, item: any) => {
  console.log(event, item);
  console.log(vueSimpleContextMenu.value)
  // vueSimpleContextMenu.value.showMenu(event, item)
}

const optionClicked = (event: Event) => {
  window.alert(JSON.stringify(event))
}

const options = [
  {
    name: "A",
  }
]


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

const dataSource = ref<TreeNodeVO[]>([]);

const contextMenuVisiable = ref()

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
  if (node.level == 2) {
    const apiParam: ParamGetDbTableData = {
      dataSourceId: dataSourceId.value,
      dbName: node.parent.label,
      tableName: node.label,
      pageIndex: 1,
      pageSize: 100
    }
    apiGetTableData(apiParam).then((res) => {
      if (res.data) {
        props.nodeClickCallback(res.data)
      }
    })
  }
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

const onContextMenuRequest = (event: Event, data: any, node: Node, nodeComponent: any) => {

}

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
</style>