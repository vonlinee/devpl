<template>
  <div>
    <el-tree
      :data="dataSource"
      :props="defaultProps"
      :load="loadDbTables"
      lazy
      @node-click="nodeClickHandler"
    >
      <template #default="{ node, data }">
        <span
          class="custom-tree-node"
          @contextmenu="displayContextMenu($event, node, data)"
        >
          <span v-contextmenu:contextMenuRef>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </div>

  <Contextmenu ref="contextMenuRef">
    <contextmenu-item @click="handleMockerMenuItemClicked"
      >数据模拟</contextmenu-item
    >
    <contextmenu-item>DDL</contextmenu-item>
    <contextmenu-item>菜单3</contextmenu-item>
  </Contextmenu>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue"
import { apiGetDatabaseNamesById, apiListTableNames } from "@/api/datasource"
import type Node from "element-plus/es/components/tree/src/model/node"
import { Contextmenu, ContextmenuItem } from "v-contextmenu"

const dbMocker = ref()

const contextMenuRef = ref()

/**
 * 展示右键菜单
 * @param event
 * @param node 点击的节点
 * @param data 节点的数据
 */
const displayContextMenu = (event: Event, node: Node, data: TreeNodeVO) => {
  contextMenuRef.value.show(event)
}

function handleMockerMenuItemClicked() {
  dbMocker.value.show()
}

const defaultProps = {
  label: "label",
  children: "children",
  isLeaf: "leaf",
}

const dataSource = ref<TreeNodeVO[]>([])

interface DatabaseNavigationViewProps {
  dataSourceId?: number
  nodeClickCallback: (param: DBTableDataVO) => void
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
  if (dataSourceId.value) {
    apiListTableNames(dataSourceId.value, node.label).then((res) => {
      const tableNodes: TreeNodeVO[] = []
      if (res.data) {
        for (let i = 0; i < res.data?.length; i++) {
          tableNodes.push({
            label: res.data[i],
            leaf: true,
          })
        }
        resolve(tableNodes)
      } else {
        resolve([])
      }
    })
  }
}

/**
 * 节点点击
 * @param data
 * @param node
 * @param item
 * @param param
 */
const nodeClickHandler = (
  data: TreeNodeVO,
  node: Node,
  item: any,
  param: any
) => {
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
    const nodes: TreeNodeVO[] = []
    for (let i = 0; i < res.data.length; i++) {
      nodes.push({
        label: res.data[i],
        leaf: false,
      })
    }
    dataSource.value = nodes
  })
}

defineExpose({
  loadDbSchemas: loadDatabases,
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
