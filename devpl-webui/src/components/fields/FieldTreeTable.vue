<template>
  <el-table ref="fieldTable" border class="sortable-row-gen" row-key="id" :data="tableData" default-expand-all
    highlight-current-row row-class-name="field-row">
    <el-table-column type="selection" width="35" align="center"></el-table-column>
    <el-table-column prop="fieldKey" label="Key"></el-table-column>
    <el-table-column prop="fieldName" label="字段名称" show-overflow-tooltip></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, ref, toRaw } from "vue";
import Sortable from "sortablejs";
import { SortUp } from "@element-plus/icons";

const children = "children";
const rowKey = "id";

const tableData = ref<FieldInfo[]>([
  {
    id: 1,
    fieldKey: "param",
    fieldName: "param",
    children: [{
      id: 11,
      parentId: 1,
      fieldKey: "name",
      fieldName: "name"
    }, {
      id: 12,
      parentId: 1,
      fieldKey: "value",
      fieldName: "value"
    }]
  }, {
    id: 2,
    fieldKey: "param1",
    fieldName: "param1",
    children: [{
      id: 21,
      parentId: 2,
      fieldKey: "name1",
      fieldName: "name1"
    }, {
      id: 22,
      parentId: 2,
      fieldKey: "value1",
      fieldName: "value1"
    }]
  }
]);
const fieldTable = ref();
// 平铺数组
const flattArray = ref<any[]>([]);

/**
 * 处理数据
 */
function getDealData() {
  const result: any[] = [];
  const func = function (arr: any[], parent: any) {
    arr.forEach(item => {
      const obj = Object.assign(item);
      if (parent) {
        if (obj.parentIds) {
          obj.parentIds.push(parent[rowKey]);
        } else {
          obj.parentIds = [parent[rowKey]];
        }
      }
      // 将每一级的数据都一一装入result，不需要删除下面的children，方便拖动的时候根据下标直接拿到整个数据，包括当前节点的子节点
      result.push(obj);
      if (item[children] && item[children].length !== 0) {
        func(item[children], item);
      }
    });
  };
  func(tableData.value, null);

  flattArray.value = result;
}

const resetTableData = (data: any[]) => {
  tableData.value = [];
  // 重新渲染表格，用doLayout不生效，所以重新装了一遍
  nextTick(() => {
    tableData.value = data;
  });
}

/**
 * 拖拽会改变树形结构
 * @param sourceObj 
 * @param targetObj 
 * @param sourceObj 
 * @param newIndex 
 */
function changeData(sourceObj: any, targetObj: any, oldIndex: number, newIndex: number) {
  let flag = 0;

  console.log("开始位置", oldIndex, "结束位置", newIndex);

  // 开始位置和结束位置相同，不变
  if (oldIndex == newIndex) {
    return
  }


  // 将表格拖动之前的数据复制一份
  const data = Object.assign(tableData.value);

  const func = function (arr: any, parent: any) {
    for (let i = arr.length - 1; i >= 0; i--) {
      const item = arr[i];
      // 判断是否是原来拖动的节点，如果是，则删除
      if (item[rowKey] === sourceObj[rowKey] && (!parent || parent && parent[rowKey] !== targetObj[rowKey])) {
        console.log("移除第" + i + "个元素");
        
        arr.splice(i, 1);
        flag++;
      }
      // 判断是否是需要插入的节点，如果是，则装入数据
      if (item[rowKey] === targetObj[rowKey]) {
        if (item[children]) {
          // 判断源数据是否已经是在目标节点下面的子节点，如果是则不移动了
          let repeat = false;
          item[children].forEach((e: any) => {
            if (e[rowKey] === sourceObj[rowKey]) {
              repeat = true;
            }
          });
          if (!repeat) {
            sourceObj.parentIds = [];
            item[children].unshift(sourceObj);
          }
        } else {
          sourceObj.parentIds = [];
          item[children] = [sourceObj];
        }
        flag++;
      }
      // 判断是否需要循环下一级，如果需要则进入下一级
      if (flag !== 2 && item[children] && item[children].length !== 0) {
        func(item[children], item);
      } else if (flag === 2) {
        break;
      }
    }
  };

  console.log("目标位置的父节点", targetObj.parentIds);

  // 检测是否是将父级拖到子级下面，如果是则数据不变，界面重新回到原数据
  if (targetObj.parentIds) {

    if (targetObj.parentIds.findIndex((_: any) => _ === sourceObj["id"]) === -1) {
      func(data, null);
    } else {
      console.log("不能将父级拖到子级下面");
    }
  } else {
    func(data, null);
  }

  // 重置表格数据
  resetTableData(data)
}

/**
 * 初始化拖拽状态
 */
const rowDrop = () => {
  const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody");

  // 拖拽配置
  Sortable.create(elTableBody, {
    // 拖动时禁用排序，拖拽的节点不会影响原列表，但是无法获取到节点Drop时的位置
    sort: true, 
    forceFallback: false,
    setData(dataTransfer, draggedElement) {
      dataTransfer.setData('Text', draggedElement.textContent || "");
    },
    onChoose: (e: Sortable.SortableEvent) => {
      console.log("选中", e.oldIndex);
    },
    onStart: function (evt) {
      // element index within parent
      
    },
    // 拖拽过程中进行移动, 如果sort被禁用，onMove不会触发
    onMove(evt: Sortable.MoveEvent, originalEvent: Event) {
      // 不符合判断条件，立即还原交换

      // 返回false来达到 sort 设为false的效果
      return false
    },
    onEnd(e: Sortable.SortableEvent) {
      getDealData();

      // from为table的body to为table的body，二者是相等的
      const { newIndex, oldIndex, from, to, item, pullMode, oldDraggableIndex, newDraggableIndex } = e

      console.log(e);
      


      if (oldIndex && newIndex) {
        let sourceObj = flattArray.value[oldIndex];
        let targetObj = flattArray.value[newIndex];

        sourceObj = toRaw(sourceObj)

        targetObj = toRaw(targetObj)
        // 改变要显示的树形数据
        changeData(sourceObj, targetObj, oldIndex, newIndex);
      }
    }
  });
};

onMounted(() => {
  rowDrop();
});

</script>

<style scoped></style>