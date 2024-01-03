<!-- 
  字段树结构
 -->
<template>
  <el-scrollbar max-height="100%">
    <el-tree
      :data="dataSource"
      :show-checkbox="selectable"
      default-expand-all
      :expand-on-click-node="false"
      draggable
      node-key="id"
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      @node-drag-start="handleDragStart"
      @node-drag-enter="handleDragEnter"
      @node-drag-leave="handleDragLeave"
      @node-drag-over="handleDragOver"
      @node-drag-end="handleDragEnd"
      @node-click="handleNodeClicked"
      @node-drop="handleDrop"
    >
      <template #default="{ node, data }">
        <div class="field-tree-node-container">
          <!-- 字段名称 -->
          <a class="field-label" v-if="!(data.editing || false)" :title="data.description || '无'"
             @click="fireInput(data)">{{ data.fieldKey }}</a>
          <input v-if="data.editing || false" class="field-input" ref="currentInputRef" @blur="data.editing = false"
                 @change="(e) => onInputChange(e, data)" @keyup.enter="(e) => onInputChange(e, data)">

          <!-- 数据类型-->
          <el-button v-if="!(data.editing || false)" link style="margin-left: 10px; color: green">
            {{ data.dataType || "unknown" }}
          </el-button>

          <div class="operation-btn-container">
            <span style="display: inline-block; height: 100%;">
              <el-button link :icon="Plus" @click="append(data)"></el-button>
              <el-button link :icon="Minus" @click="remove(node, data)" style="margin: 0"></el-button>
            </span>
          </div>
        </div>
      </template>
    </el-tree>
  </el-scrollbar>
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

const fireInput = (data: FieldInfo) => {
  data.editing = true;
  nextTick(() => {
    currentInputRef.value.value = data.fieldKey;
    currentInputRef.value.focus();
  });
};

const onInputChange = (event: Event, data: FieldInfo) => {
  data.fieldKey = (event.target as HTMLInputElement).value;
  data.editing = false;
};

/**
 * 组件属性
 */
type FieldTreeProps = {
  selectable?: boolean
  height?: string
  // 初始字段
  fields?: FieldInfo[]
}

const { selectable, height, fields } = withDefaults(
  defineProps<FieldTreeProps>(),
  {
    selectable: false,
    height: "100%"
  }
);

const dataSource = ref<FieldInfo[]>(fields || []);

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
const handleDragOver = (
  draggingNode: Node,
  dropNode: Node,
  ev: DragEvents
) => {
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

watch(
  () => fields,
  (newValue, oldValue) => {

  },
  {
    deep: true
  }
);

defineExpose({
  getFields: function() {
    return dataSource.value;
  }
});

</script>

<style lang="scss" scoped>
.field-tree-node-container {
  width: 100%;
  display: flex;
  align-items: center;
  //justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;

  .operation-btn-container {
    flex: 1;
    text-align: right;
    position: relative;
    right: 0;
  }

  // 字段标签
  .field-label {
    display: block;
    //width: 100%;
    word-break: keep-all;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    -icab-text-overflow: ellipsis;
    -khtml-text-overflow: ellipsis;
    -moz-text-overflow: ellipsis;
    -webkit-text-overflow: ellipsis;
  }

  .field-input {
    width: 100%;
    outline-style: none;
    border: 1px solid #ccc;
    border-radius: 3px;
    padding: 3px;
    //font-size: 14px;
  }

  input:focus {
    border-color: #66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6)
  }
}
</style>
