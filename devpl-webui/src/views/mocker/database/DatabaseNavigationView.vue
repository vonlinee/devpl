<template>
  <el-tree :data="dataSource" :props="defaultProps" :load="loadDbTables" lazy @node-click="nodeClickHandler"
    hegiht="100%">
    <template #default="{ node, data }">
      <span class="custom-tree-node" @contextmenu="displayContextMenu($event)">
        <span>{{ node.label }}</span>
      </span>
    </template>
  </el-tree>

  <ContextMenu ref="contextMenuRef" :model="items">
    <template #item="{ item, props }">
      <a v-ripple style="display: flex; align-items: center; height: 30px;" v-bind="props.action">
        <span :class="item.icon"></span>
        <span>{{ item.label }}</span>
        <Badge v-if="item.badge" :value="item.badge" />
        <span v-if="item.shortcut">{{
          item.shortcut }}</span>
        <i v-if="item.items"></i>
      </a>
    </template>
  </ContextMenu>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { apiGetDatabaseNamesById, apiListTableNames } from "@/api/datasource";
import type Node from "element-plus/es/components/tree/src/model/node";
import ContextMenu from 'primevue/contextmenu';

const contextMenuRef = ref()

const displayContextMenu = (event: Event) => {
  contextMenuRef.value.show(event)
}

const items = ref([
  {
    label: 'Favorite',
    icon: 'pi pi-star',
    shortcut: '⌘+D',
  },
  {
    label: 'Add',
    icon: 'pi pi-shopping-cart',
    shortcut: '⌘+A'
  },
  {
    separator: true
  },
  {
    label: 'Share',
    icon: 'pi pi-share-alt',
    items: [
      {
        label: 'Whatsapp',
        icon: 'pi pi-whatsapp',
        badge: 2
      },
      {
        label: 'Instagram',
        icon: 'pi pi-instagram',
        badge: 3
      }
    ]
  }
]);

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