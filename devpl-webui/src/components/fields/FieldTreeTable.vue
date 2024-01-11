<!-- 
  可拖拽树结构
 -->
<template>
  <el-table
    ref="fieldTable"
    border
    row-key="id"
    :data="tableData"
    default-expand-all
    @expand-change="handleExpandChange"
  >
    <el-table-column
      type="selection"
      width="35"
      align="center"
    ></el-table-column>
    <el-table-column
      prop="fieldKey"
      label="Key"
      show-overflow-tooltip
    ></el-table-column>
    <el-table-column
      prop="fieldName"
      label="字段名称"
      show-overflow-tooltip
    ></el-table-column>
    <el-table-column prop="dataType" label="数据类型"></el-table-column>
    <el-table-column
      prop="description"
      label="注释"
      show-overflow-tooltip
    ></el-table-column>
    <template #empty>
      <el-text class="click-target" @click="insertEmptyRow">暂无数据</el-text>
    </template>
  </el-table>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, ref, toRaw } from "vue"
import Sortable from "sortablejs"

const children = "children"
const rowKey = "id"

const tableData = ref<FieldInfo[]>([])
const fieldTable = ref()
// 平铺数组
const flattArray = ref<any[]>([])

/**
 * 插入空行
 */
const insertEmptyRow = () => {
  tableData.value.push({
    id: "",
    fieldKey: "",
    fieldName: "",
  })
}

/**
 * 处理展开行变化
 * @param row
 * @param expandedRows
 */
function handleExpandChange(row: any, expandedRows: any[]) {
  console.log(expandedRows)
}

/**
 * 记录拖拽开始和结束时的位置
 */
const dragData = {
  status: false,
  rowHeight: 0,
  /**
   * 开始拖拽时鼠标top距离
   */
  startTop: 0,
  startBottom: 0,
  startRowIndex: 0,
  endRowIndex: 0,
  startX: 0,
  startY: 0,
  endX: 0,
  endY: 0,
}

/**
 * 处理数据，给所有数据加上id字段及parentIds字段
 */
function getDealData() {
  // 先给id赋值，保证唯一性
  for (let i = 0; i < tableData.value.length; i++) {
    tableData.value[i].id = i
  }
  const result: any[] = []
  const func = function (arr: any[], parent: any) {
    arr.forEach((item) => {
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
  func(tableData.value, null)

  flattArray.value = result
}

const resetTableData = (data: any[]) => {
  tableData.value = []
  // 重新渲染表格，用doLayout不生效，所以重新装了一遍
  nextTick(() => {
    tableData.value = data
  })
}

/**
 * 拖拽会改变树形结构
 * @param targetObj
 * @param sourceObj
 * @param oldIndex
 * @param newIndex
 */
function changeData(
  sourceObj: any,
  targetObj: any,
  oldIndex: number,
  newIndex: number
) {
  let flag = 0
  // 开始位置和结束位置相同，不变
  if (oldIndex == newIndex) {
    return
  }
  // 将表格拖动之前的数据复制一份
  const data = Object.assign(tableData.value)

  const func = function (arr: any, parent: any) {
    for (let i = arr.length - 1; i >= 0; i--) {
      const item = arr[i]
      // 判断是否是原来拖动的节点，如果是，则删除
      if (
        item[rowKey] === sourceObj[rowKey] &&
        (!parent || (parent && parent[rowKey] !== targetObj[rowKey]))
      ) {
        // console.log("移除第" + i + "个元素");
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

  // console.log("目标位置的父节点", targetObj.parentIds);

  // 检测是否是将父级拖到子级下面，如果是则数据不变，界面重新回到原数据
  if (targetObj.parentIds) {
    if (
      targetObj.parentIds.findIndex((_: any) => _ === sourceObj["id"]) === -1
    ) {
      func(data, null)
    } else {
      // console.log("不能将父级拖到子级下面");
    }
  } else {
    func(data, null)
  }

  // 重置表格数据
  resetTableData(data)
}

/**
 * 计算节点放下时的位置
 * 是否进入以放下拖拽节点时鼠标的位置是否进入目标行的范围为准
 * @param event
 * @param startY
 * @param endY
 */
function calculate(event: Sortable.MoveEvent, startY: number, endY: number) {
  // 鼠标移动的距离
  const distance = Math.abs(endY - startY)
  if (endY > startY) {
    // 向下拖动
    const offset = endY - dragData.startBottom
    const offsetRowNum = (distance - offset) / dragData.rowHeight
    dragData.endRowIndex = dragData.startRowIndex + Math.ceil(offsetRowNum)
  } else {
    // 向上拖动
    const offset = startY - dragData.startTop
    const offsetRowNum = (distance - offset) / dragData.rowHeight
    dragData.endRowIndex = dragData.startRowIndex - Math.ceil(offsetRowNum)
  }
}

/**
 * 初始化拖拽状态
 */
const rowDrop = () => {
  const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody")

  // 拖拽配置
  Sortable.create(elTableBody, {
    // 拖动时禁用排序，拖拽的节点不会影响原列表，但是无法获取到节点Drop时的位置
    sort: true,
    forceFallback: false,
    setData(dataTransfer, draggedElement) {
      dataTransfer.setData("Text", draggedElement.textContent || "")
    },
    onChoose: (e: Sortable.SortableEvent) => {
      // 行高度
      dragData.rowHeight = e.item.clientHeight
    },
    /**
     *
     * @param e Sortable.SortableEvent
     */
    onStart: function (e: Sortable.SortableEvent) {
      // element index within parent
      // 记录开始时的鼠标的位置
      const _e = e as any
      const dragEvent = _e.originalEvent
      if (e.oldIndex !== undefined) {
        dragData.status = true
        dragData.startTop = e.item.getBoundingClientRect().top
        dragData.startBottom = e.item.getBoundingClientRect().bottom
        dragData.startRowIndex = e.oldIndex
        dragData.startX = dragEvent.clientX
        dragData.startY = dragEvent.clientY
      }
    },
    /**
     * 拖拽过程中进行移动, 如果sort被禁用，onMove不会触发
     * @param evt
     * @param originalEvent DragEvent
     */
    onMove(evt: Sortable.MoveEvent, originalEvent: Event) {
      // 不符合判断条件，立即还原交换
      const oe = originalEvent as DragEvent
      dragData.endX = oe.clientX
      dragData.endY = oe.clientY
      calculate(evt, dragData.startY, oe.clientY)
      // 返回false来达到sort设为false的效果
      return false
    },
    /**
     * 拖拽结束
     * @param event
     */
    onEnd(event: Sortable.SortableEvent) {
      getDealData()
      // from为table的body to为table的body，二者是相等的
      const { newIndex, oldIndex } = event

      // 因为禁用了排序，所以oldIndex == newIndex
      // from == to，均为tbody
      // 计算拖拽结束时的节点位置
      // console.log("从", dragData.startRowIndex, "到", dragData.endRowIndex)

      let sourceObj = toRaw(flattArray.value[dragData.startRowIndex])
      let targetObj = toRaw(flattArray.value[dragData.endRowIndex])

      console.log(sourceObj, targetObj)

      sourceObj = toRaw(sourceObj)

      targetObj = toRaw(targetObj)
      // 改变要显示的树形数据
      changeData(
        sourceObj,
        targetObj,
        dragData.startRowIndex,
        dragData.endRowIndex
      )
      dragData.status = false
    },
  })
}

onMounted(() => {
  rowDrop()
})

defineExpose({
  /**
   * 获取所有字段信息
   */
  getFields() {
    return tableData.value
  },
  /**
   * 新增字段信息
   * @param fields 字段信息列表
   */
  addFields(fields: FieldInfo[]) {
    let newFields = [...tableData.value]
    fields.forEach((f) => newFields.push(f))
    tableData.value = newFields
  },
  /**
   * 覆盖所有字段信息
   * @param newFields
   */
  setFields(newFields?: FieldInfo[]) {
    if (newFields !== undefined) {
      tableData.value = newFields
    }
  },
})
</script>

<style lang="scss" scoped></style>
