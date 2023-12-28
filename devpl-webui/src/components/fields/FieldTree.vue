<!-- 
  字段树结构
 -->
<template>
  <div class="field-tree-container" :style="containerStyle">
    <el-tree :data="dataSource" height="100%" :show-checkbox="selectable" default-expand-all
             :expand-on-click-node="false"
             draggable node-key="id" @node-drag-start="handleDragStart" @node-drag-enter="handleDragEnter"
             @node-drag-leave="handleDragLeave" @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd"
             @node-click="handleNodeClicked"
             @node-drop="handleDrop" :allow-drop="allowDrop" :allow-drag="allowDrag">
      <template #default="{ node, data }">
        <span class="field-tree-node">
          <span v-if="!(data.editing || false)" @click="fireInput(data)">{{ data.fieldKey }}</span>
          <input ref="currentInputRef" v-if="data.editing || false" :value="data.fieldKey" @blur="data.editing = false"
                 @change="(event) => onInputChange(event, data)" @keyup.enter="(event) => onInputChange(event, data)" />
          <span>
            <el-button link :icon="Plus" @click="append(data)"></el-button>
            <el-button v-if="node.fieldKey !== 'Root'" link :icon="Minus" @click="remove(node, data)"></el-button>
          </span>
        </span>
      </template>

      <template #empty>
        没有字段
        <el-button link @click="addIfEmpty">添加</el-button>
      </template>
    </el-tree>
  </div>
</template>

<script lang="ts" setup>
import { StyleValue, nextTick, reactive, ref } from "vue";
import type Node from "element-plus/es/components/tree/src/model/node";
import type { DragEvents } from "element-plus/es/components/tree/src/model/useDragNode";
import type {
  AllowDropType,
  NodeDropType
} from "element-plus/es/components/tree/src/tree.type";
import { Minus, Plus } from "@element-plus/icons";
import { watch } from "vue";

const currentInputRef = ref();

const onInputChange = (event: Event, data: FieldInfo) => {
  data.fieldKey = (event.target as HTMLInputElement).value;
  data.editing = false;
};

const fireInput = (data: FieldInfo) => {
  data.editing = true;
  nextTick(() => currentInputRef.value.focus());
};

/**
 * 组件属性
 */
type FieldTreeProps = {
  selectable?: boolean,
  height?: string,
  // 字段
  fields: FieldInfo[]
}

const { selectable, height, fields } = withDefaults(defineProps<FieldTreeProps>(), {
  selectable: false,
  height: "500px"
});
const dataSource = ref<FieldInfo[]>(fields);

const addIfEmpty = () => {
  if (dataSource.value.length == 0) {
    dataSource.value.push({
      id: 0,
      editing: true,
      fieldKey: ""
    });
  }
};

/**
 * 容器样式
 */
const containerStyle = reactive({
  height: height,
  display: "block",
  overflowY: "scroll"
}) as StyleValue;

let id = 1000;

/**
 * 追加节点
 * @param data 节点数据
 */
const append = (data: FieldInfo) => {
  const newChild = { id: id++, fieldKey: "New Item", children: [] } as FieldInfo;
  if (!data.children) {
    data.children = [];
  }
  data.children.push(newChild);
  dataSource.value = [...dataSource.value];
};

const handleNodeClicked = (data: any, node: any, item: any) => {

};

/**
 * 删除节点
 * @param node
 * @param data
 */
const remove = (node: Node, data: FieldInfo) => {
  if (node.label === "Root") {
    return;
  }
  const parent = node.parent;
  const children: FieldInfo[] = parent.data.children || parent.data;
  const index = children.findIndex((d) => d.id === data.id);
  children.splice(index, 1);
  dataSource.value = [...dataSource.value];
};

/**
 * 是否允许放下拖拽的节点
 * @param draggingNode
 * @param dropNode
 * @param type
 */
const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  if (dropNode.data.fieldKey === "Level two 3-1") {
    return type !== "inner";
  } else {
    return true;
  }
};

/**
 * 是否允许开始拖拽
 * @param draggingNode
 */
const allowDrag = (draggingNode: Node) => {
  return draggingNode.data.editing !== true;
};

const handleDragStart = (node: Node, ev: DragEvents) => {
  console.log("drag start", node);
};

const handleDragEnter = (
  draggingNode: Node,
  dropNode: Node,
  ev: DragEvents
) => {

};
const handleDragLeave = (
  draggingNode: Node,
  dropNode: Node,
  ev: DragEvents
) => {

};
const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {

};
const handleDragEnd = (
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  ev: DragEvents
) => {

};
const handleDrop = (
  draggingNode: Node,
  dropNode: Node,
  dropType: NodeDropType,
  ev: DragEvents
) => {

};

watch(() => fields, (newValue, oldValue) => {

}, {
  deep: true
});
</script>

<style>
.field-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.field-tree-container {
  height: 300px;
  display: block;
  overflow-y: scroll;
}
</style>
  