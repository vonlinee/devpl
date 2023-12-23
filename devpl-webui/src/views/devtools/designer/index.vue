<!-- 
  https://www.cnblogs.com/liuqin-always/p/14103450.html
  拖拽行的时候，同时拖拽该行及其嵌套的所有行
 -->
<template>
  <div>
    <converter></converter>
    <el-card>
      <el-button>导入</el-button>
      <Splitpanes>
        <Pane>
          <el-table border :data="selectableFields" @selection-change="handleSelectionChange" :height="tableHeight">
            <el-table-column type="selection" width="35" align="center"></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column prop="fieldName" label="字段名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </Pane>
        <Pane>
          <el-table ref="fieldTable" border class="sortable-row-gen" row-key="id" :data="selectedFields"
            default-expand-all highlight-current-row :height="tableHeight" row-class-name="field-row">
            <el-table-column type="selection" width="35" align="center"></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column prop="fieldName" label="字段名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </Pane>
      </Splitpanes>
    </el-card>

    <el-button @click="onClickHandle"></el-button>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, nextTick, Ref } from "vue";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

import Sortable from "sortablejs";
import { apiListAllFields } from "@/api/fields";
import Converter from "@/views/devtools/toolset/SqlConverter.vue";

const selectableFields = ref<FieldInfo[]>([]);
const selectedFields = ref<FieldInfo[]>([]);
const fieldList = ref<FieldInfo[]>([]);
const fieldTable = ref();

const sortable = ref();
const tableHeight = "600px";

/**
 * 树形结构拖拽
 */
type SortableTree = {
  /**
   * 当前节点的拖拽实例
   */
  sortable: Sortable,
  /**
   * 子节点
   */
  children?: SortableTree[]
}

const sortableRef = ref<SortableTree>()

/**
 * el-table需要指定row-key，否则会导致拖拽顺序混乱
 */
const enableRowDragDrop = () => {

  rowDrop()

  // nextTick(() => {
  //   // 获取需要拖拽的dom节点
  //   if (fieldTable.value) {
  //     const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody");
  //     if (elTableBody) {
  //       // 创建 Sortable
  //       sortable.value = Sortable.create(elTableBody, {
  //         disabled: false, // 是否开启拖拽
  //         ghostClass: "sortable-ghost", //拖拽样式
  //         animation: 150, // 拖拽延时，效果更好看
  //         handle: ".field-row",
  //         /**
  //          * 进行数据的处理，拖拽实际并不会改变绑定数据的顺序
  //          * @param e
  //          */
  //         onEnd: (e: Sortable.SortableEvent) => {
  //           // oldIndex为列表元素开始拖拽的索引，newIndex为列表元素结束拖拽的索引
  //           const { newIndex, oldIndex } = e;

  //           if (oldIndex && newIndex) {
  //             // 拖拽的行
  //             const curRow = fieldList.value.splice(oldIndex, 1)[0];
  //             // 将拖拽的行插入到拖拽结束的位置
  //             fieldList.value.splice(newIndex, 0, curRow);
  //             // sortIndex(fieldList);
  //           }
  //         }
  //       });
  //     }
  //   }
  // });
};


const onClickHandle = () => {
  const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody");
  if (elTableBody) {
    // 获取所有的行
    const trlist: NodeList = elTableBody.querySelectorAll("tr")

    for (let i = 0; i < trlist.length; i++) {
      Sortable.create(trlist.item(i) as HTMLElement, {
        group: "a",
        animation: 150
      })
    }
  }
}

const flattArray = ref<any[]>([])

function getDealData() {
  const result: any[] = []
  const children = 'children'
  const rowKey = ''
  const func = function (arr: any[], parent: any) {
    arr.forEach(item => {
      const obj = Object.assign(item)
      if (parent) {
        if (obj.parentIds) {
          obj.parentIds.push(parent[rowKey])
        } else {
          obj.parentIds = [parent[rowKey]]
        }
      }
      // 将每一级的数据都一一装入result，不需要删除下面的children，方便拖动的时候根据下标直接拿到整个数据，包括当前节点的子节点
      result.push(obj)
      if (item[children] && item[children].length !== 0) {
        func(item[children], item)
      }
    })
  }
  func(selectedFields.value, null)
  flattArray.value = result
}

const expandLevel = 3

const expandRow = ref<any[]>([])

function getExpandRow(data: any[]) {
  const result: any[] = []
  const children = 'children'
  const rowKey = 'id'
  let level = 0
  // 默认展开三级
  const func = (arr: any, parent: any) => {
    arr.forEach((item: any) => {
      if (item[children] && item[children].length !== 0) {
        if (level < expandLevel) {
          result.push(item[rowKey] + '')
        }
        level++
        func(item[children], item)
      }
    })
  }
  func(data, null)
  expandRow.value = result
}

function changeData(sourceObj: any, targetObj: any) {
  let flag = 0
  const data = Object.assign(selectedFields.value)
  const children = 'children'
  const rowKey = 'id'
  const func = function (arr: any, parent: any) {
    for (let i = arr.length - 1; i >= 0; i--) {
      const item = arr[i]
      // 判断是否是原来拖动的节点，如果是，则删除
      if (item[rowKey] === sourceObj[rowKey] && (!parent || parent && parent[rowKey] !== targetObj[rowKey])) {
        arr.splice(i, 1)
        flag++
      }
      // 判断是否是需要插入的节点，如果是，则装入数据
      if (item[rowKey] === targetObj[rowKey]) {
        if (item[children]) {
          // 判断源数据是否已经是在目标节点下面的子节点，如果是则不移动了
          let repeat = false
          item[children].forEach((e: any) => {
            if (e[rowKey] === sourceObj[rowKey]) {
              repeat = true
            }
          })
          if (!repeat) {
            sourceObj.parentIds = []
            item[children].unshift(sourceObj)
          }
        } else {
          sourceObj.parentIds = []
          item[children] = [sourceObj]
        }
        flag++
      }
      // 判断是否需要循环下一级，如果需要则进入下一级
      if (flag !== 2 && item[children] && item[children].length !== 0) {
        func(item[children], item)
      } else if (flag === 2) {
        break
      }
    }
  }
  // 检测是否是将父级拖到子级下面，如果是则数据不变，界面重新回到原数据
  if (targetObj.parentIds) {
    if (targetObj.parentIds.findIndex((_: any) => _ === sourceObj['id']) === -1) {
      func(data, null)
    } else {
      console.log('不能将父级拖到子级下面');
    }
  } else {
    func(data, null)
  }

  selectedFields.value = []
  // 重新渲染表格，用doLayout不生效，所以重新装了一遍
  nextTick(() => {
    selectedFields.value = data
  })
}


const rowDrop = () => {
  const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody");
  const self = this
  Sortable.create(elTableBody, {
    onEnd({ newIndex, oldIndex }) {
      getDealData()
      if (oldIndex && newIndex) {
        const sourceObj = flattArray.value[oldIndex]
        const targetObj = flattArray.value[newIndex]
        // 改变要显示的树形数据
        changeData(sourceObj, targetObj)
      }
    }
  })
}



/**
 * 排序
 * @param tableData 
 */
const sortIndex = (tableData: Ref<any[]>) => {
  const array: any[] = [];
  tableData.value.forEach((e, i) => {
    const obj = {
      index: i + 1,
      ...e
    };
    array.push(obj);
  });
  tableData.value = array;
};

const handleSelectionChange = (val: FieldInfo[]) => {
  selectedFields.value = val;
};

let i = 1111;

onMounted(() => {
  apiListAllFields().then((res) => {
    selectableFields.value = res.data;
  });
  if (!sortable.value) {
    enableRowDragDrop();
  }
});
</script>

<style lang="scss" scoped></style>