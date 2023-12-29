<template>
  <vxe-table
    ref="tableRef"
    height="auto"
    :min-height="height"
    :max-height="height"
    show-overflow
    :border="true"
    row-key
    :data="rows"
    :checkbox-config="{ checkStrictly: true }"
    :column-config="{
      resizable: true,
    }"
    :row-config="{
      height: rowHeight,
    }"
    :scroll-y="{ enabled: true }"
    :tree-config="{ transform: true }"
    :edit-config="editConfig"
  >
    <vxe-column
      type="checkbox"
      title="#"
      width="45"
      header-align="center"
      align="center"
      :resizable="false"
    ></vxe-column>
    <vxe-column
      field="name"
      title="名称"
      tree-node
      :edit-render="{ name: 'input' }"
    >
      <template #default="{ row }">
        <span>{{ row.name }}</span>
      </template>
    </vxe-column>
    <vxe-column field="value" title="值" :edit-render="{ name: 'input' }">
      <template #default="{ row }">
        <span>{{ row.leaf ? row.value : "" }}</span>
      </template>
    </vxe-column>
    <vxe-column
      field="dataType"
      title="数据类型"
      min-width="120"
      :edit-render="{
        autofocus: 'data-type-select',
      }"
    >
      <template #default="{ row }">
        <span>{{ row.leaf ? row.dataType : "" }}</span>
      </template>
      <!-- <template #edit="{ row }">
				<vxe-select class-name="data-type-select" v-model="row.dataType" clearable transfer>
					<vxe-option v-for="item in dataTypes" :value="item.value" :label="item.name"></vxe-option>
				</vxe-select>
			</template> -->
    </vxe-column>
    <vxe-column
      title="操作"
      header-align="center"
      align="center"
      fixed="right"
      width="150"
      :resizable="false"
    >
      <template #default="{ row, rowIndex }">
        <vxe-button type="text" transfer placement="bottom">
          <template #default> 新增 </template>
          <template #dropdowns>
            <vxe-button
              type="text"
              content="相邻节点"
              @click="insertNextRow(row, 'current')"
            ></vxe-button>
            <vxe-button
              type="text"
              content="子节点"
              @click="insertRow(row, 'bottom')"
            ></vxe-button>
          </template>
        </vxe-button>
        <vxe-button type="text" @click="removeRow(row)">删除</vxe-button>
      </template>
    </vxe-column>
  </vxe-table>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue"
import { VxeTablePropTypes } from "vxe-table/types/all"

type ParamTableProps = {
  height?: number
  rowHeight?: number
  rows: ParamItem[]
  dataTypes: DataTypeItem[]
}

const { height, rows } = withDefaults(defineProps<ParamTableProps>(), {
  height: 400,
  rowHeight: 35,
})

// 表格实例
const tableRef = ref()

/**
 * 表格编辑配置
 */
const editConfig = reactive<VxeTablePropTypes.EditConfig>({
  trigger: "click",
  mode: "cell",
  // row: any, rowIndex: number, column: any, columnIndex: number
  beforeEditMethod: (params) => {
    if (params.columnIndex == 1) {
      return true
    }
    // 只有叶子结点可编辑
    const row = params.row
    return row.leaf != null && row.leaf
  },
})

const insertNextRow = async (currRow: ParamItem, locat: string) => {
  const $table = tableRef.value
  if ($table) {
    const date = new Date()
    // 如果 null 则插入到目标节点顶部
    // 如果 -1 则插入到目标节点底部
    // 如果 row 则有插入到效的目标节点该行的位置
    if (locat === "current") {
      const record = {
        name: "unknown",
        id: Date.now(),
        parentId: currRow.parentId, // 父节点必须与当前行一致
      }
      const { row: newRow } = await $table.insertNextAt(record, currRow)
      await $table.setEditRow(newRow) // 插入子节点
    }
  }
}

/**
 * 新增一行
 * @param currRow
 * @param locat
 */
const insertRow = async (currRow: ParamItem, locat: string) => {
  const $table = tableRef.value
  if ($table) {
    const date = new Date()
    // 如果 null 则插入到目标节点顶部
    // 如果 -1 则插入到目标节点底部
    // 如果 row 则有插入到效的目标节点该行的位置
    const rid = Date.now()
    if (locat === "current") {
      const record = {
        name: `新数据`,
        id: rid,
        parentId: currRow.parentId, // 父节点必须与当前行一致
      }
      const { row: newRow } = await $table.insertAt(record, currRow)
      await $table.setEditRow(newRow) // 插入子节点
    } else if (locat === "top") {
      const record = {
        name: `新数据`,
        id: rid,
        parentId: currRow.id, // 需要指定父节点，自动插入该节点中
      }
      const { row: newRow } = await $table.insert(record)
      await $table.setTreeExpand(currRow, true) // 将父节点展开
      await $table.setEditRow(newRow) // 插入子节点
    } else if (locat === "bottom") {
      const record = {
        name: `新数据`,
        id: rid,
        parentId: currRow.id, // 需要指定父节点，自动插入该节点中
      }
      const { row: newRow } = await $table.insertAt(record, -1)
      await $table.setTreeExpand(currRow, true) // 将父节点展开
      await $table.setEditRow(newRow) // 插入子节点
    }
  }
}

/**
 * 展开所有节点
 */
const expandAll = () => {
  const $table = tableRef.value
  if ($table) {
    $table.setAllTreeExpand(true)
  }
}

/**
 * 删除一行
 * @param row
 */
const removeRow = async (row: ParamItem) => {
  const $table = tableRef.value
  if ($table) {
    await $table.remove(row)
  }
}
</script>

<style lang="scss"></style>
