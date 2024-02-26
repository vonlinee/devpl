<!-- 
  字段树结构
  可拖拽改变层级关系或者排序
 -->
<template>
  <el-scrollbar max-height="100%">
    <el-tree ref="treeRef" :data="dataSource" :show-checkbox="selectable" highlight-current default-expand-all
      :expand-on-click-node="false" draggable node-key="id" :allow-drop="allowDrop" :allow-drag="allowDrag"
      @node-drag-start="handleDragStart" @node-drag-enter="handleDragEnter" @node-drag-leave="handleDragLeave"
      @node-drag-over="handleDragOver" @node-drag-end="handleDragEnd" @node-drop="handleDrop">
      <template #default="{ node, data }">
        <div style="height: auto; display: flex; flex-direction: row; width: 100%; align-items: stretch">
          <div style="height: auto; flex-grow: 1">
            <div class="field-tree-node-container">
              <!-- 字段名称 -->
              <button class="field-label" :title="data.description || '无'" @dblclick="handleNodeClicked($event, data)">
                {{ data.fieldKey }}
              </button>
              <!-- 字段类型 -->
              <el-button ref="buttonRef" link style="margin-left: 10px; color: green"
                @click="handleDataTypeClicked($event, data)">
                {{ data.dataType || "unknown" }}
              </el-button>
            </div>
            <div class="description" :draggable="false">{{ data.description }}</div>
          </div>
          <!-- 操作区域 -->
          <div class="operation-btn-container">
            <el-dropdown style="height: 100%; width: 20px;" size="small">
              <el-button link :icon="Plus" @click="addNode((data))"></el-button>
              <!-- 只有对象类型能添加子节点 -->
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="addNode(data, 'sibling')">添加相邻节点</el-dropdown-item>
                  <el-dropdown-item v-if="data.dataType == 'Object'" @click="addNode(data, 'child')">添加子节点
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button link :icon="Minus" style="height: 100%;  margin: 0 10px 0 0;"
              @click="remove(node, data)"></el-button>
          </div>
        </div>
      </template>
    </el-tree>

    <popup ref="dataTypePopupRef" position="right" :z-index="10000">
      <List :items="dataTypeOptions" width="100px" @item-clicked="(item) => handleDataTypeEdit(item)">
        <template #item="scope">
          {{ scope.item.label }}
        </template>
      </List>
    </popup>

    <field-info-form-modal ref="fieldInfoFormModalRef" />
  </el-scrollbar>
</template>

<script lang="ts" setup>
import { StyleValue, reactive, ref } from "vue";
import type Node from "element-plus/es/components/tree/src/model/node";
import type { DragEvents } from "element-plus/es/components/tree/src/model/useDragNode";
import type {
  AllowDropType,
  NodeDropType
} from "element-plus/es/components/tree/src/tree.type";
import { Minus, Plus } from "@element-plus/icons";
import FieldInfoFormModal from "./FieldInfoFormModal.vue";
import Popup from "@/views/test/Popup.vue";
import List from "@/components/List.vue";

/**
 * 可选择的数据类型
 */
const dataTypeOptions = ref<DataTypeSelectOption[]>([
  {
    label: "String",
    value: "String",
    key: "string"
  },
  {
    label: "Integer",
    value: "Integer",
    key: "Integer"
  },
  {
    label: "Boolean",
    value: "Boolean",
    key: "Boolean"
  },
  {
    label: "Array",
    value: "Array",
    key: "Array"
  },
  {
    label: "Object",
    value: "Object",
    key: "Object"
  },
  {
    label: "Number",
    value: "Number",
    key: "Number"
  },
  {
    label: "null",
    value: "null",
    key: "null"
  },
  {
    label: "any",
    value: "any",
    key: "any"
  }
]);

const treeRef = ref();

const buttonRef = ref();
const dataTypePopupRef = ref();

const fieldInfoFormModalRef = ref();

let editingField: FieldInfo | undefined;

const handleDataTypeEdit = (item: any) => {
  if (editingField != undefined) {
    editingField.dataType = item.value;
  }
  dataTypePopupRef.value.hide();
};

const handleDataTypeClicked = (event: MouseEvent, field: FieldInfo) => {
  editingField = field;
  dataTypePopupRef.value.show(event);
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

const unknownItemName = "Unknown";

const addNode = (data: FieldInfo, type?: string) => {
  if (type == undefined) {
    if (data.dataType == "Object") {
      addChild(data);
    } else {
      addSibling(data);
    }
  } else {
    if (type == "sibling") {
      addSibling(data);
    } else if (type == "child") {
      addChild(data);
    }
  }
};

/**
 * 添加子节点
 * @param data 节点数据
 */
const addChild = (data: FieldInfo) => {
  const newChild = { id: id++, fieldKey: unknownItemName, children: [] } as FieldInfo;
  if (!data.children) {
    data.children = [];
  }
  data.children.push(newChild);
  dataSource.value = [...dataSource.value];
  // treeRef.value.append(newChild, data.id)
};

/**
 * 添加相邻节点
 * @param data 节点数据
 */
const addSibling = (data: FieldInfo) => {
  const newNode = { id: id++, fieldKey: unknownItemName, children: [] } as FieldInfo;
  treeRef.value.insertAfter(newNode, data);
  // 默认选中
  treeRef.value!.setCheckedKeys([newNode.id], false);
};

/**
 * 四个参数：对应于节点点击的节点对象，TreeNode 的 node 属性, TreeNode和事件对象
 * @param event
 * @param data
 */
const handleNodeClicked = (event: Event, data: FieldInfo) => {
  fieldInfoFormModalRef.value.show(data);
};

/**
 * 删除节点
 * @param node 要删除的节点
 * @param data  要删除的节点数据
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

defineExpose({
  /**
   * 获取所有字段信息
   */
  getFields() {
    return dataSource.value;
  },
  /**
   * 设置所有字段信息
   * @param fields 字段信息
   */
  setFields(fields?: FieldInfo[]) {
    dataSource.value = fields || [];
  },
  /**
   * 获取选中的字段列表
   * 不是树形结构
   */
  getSelectedFields() {
    return treeRef.value!.getCheckedNodes(false, false);
  },
  /**
   * 获取选中的字段ID列表
   */
  getSelectedKeys() {
    return treeRef.value!.getCheckedKeys(false);
  }
});
</script>

<style lang="scss" scoped>
// 每个节点高度由其节点的子节点决定
::v-deep(.el-tree-node__content) {
  height: auto;
  cursor: default;
}

.description {
  margin-left: 11px;
  margin-top: 5px;
  margin-bottom: 5px;
  font-size: 12px;
  /*这两行代码可以解决大部分场景下的换行问题*/
  word-break: break-all;
  word-wrap: break-word;
  /*但在有些场景中，还需要加上下面这行代码*/
  white-space: normal;
}

.field-tree-node-container {
  width: 100%;
  height: auto;
  display: flex;

  margin-bottom: 5px;
  margin-top: 5px;

  align-items: center;
  //justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;

  .operation-btn-container {
    flex: 1;
    height: 30px;
    text-align: right;
    // right: 30px;
    background-color: red;


  }

  // 字段名称
  .field-label {
    display: inline-block;
    height: 30px;
    /* 上右下左的顺序设置内边距 */
    padding: 5px 5px 10px 10px;
    /* 修改文本颜色为#1e97f8 */
    background-color: #e6f7ff;
    text-align: center;
    font-size: 14px;
    border: none;
    border-radius: 5px;
    /* 添加圆角边框 */
    cursor: pointer;

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
    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
      0 0 8px rgba(102, 175, 233, 0.6);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
      0 0 8px rgba(102, 175, 233, 0.6);
  }
}
</style>
