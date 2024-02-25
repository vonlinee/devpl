<template>
  <vxe-table ref="tableRef" :height="height" border show-overflow :column-config="{ resizable: true }"
    :tree-config="{ transform: true }" :edit-config="editConfig" :data="tableData" cell-click="">
    <vxe-column field="fieldKey" title="Key" tree-node :edit-render="{}">
      <template #edit="{ row }">
        <div>
          <vxe-input v-model="row.fieldKey" type="text"></vxe-input>
        </div>
      </template>
    </vxe-column>
    <vxe-column field="dataType" title="数据类型" header-align="center" width="100" :edit-render="{}">
      <template #edit="{ row }">
        <vxe-select v-model="row.dataType" popup-class-name="datatype-select-drop-container">
          <vxe-option v-for="dt in valueDataTypes" :key="dt.key" :label="dt.label" :value="dt.value"></vxe-option>
        </vxe-select>
      </template>
    </vxe-column>
    <vxe-column field="value" title="值" header-align="center" :edit-render="{}">
      <template #edit="{ row }">
        <vxe-input v-model="row.value" type="text"></vxe-input>
      </template>
    </vxe-column>
    <vxe-column title="操作" width="140" header-align="center" align="center" fixed="right">
      <template #default="{ row }">
        <vxe-button type="text" transfer>
          <template #default>添加</template>
          <template #dropdowns>
            <vxe-button type="text" content="同级节点" @click="insertNextRow(row, 'current')"></vxe-button>
            <vxe-button type="text" content="子节点" @click="insertRow(row, 'bottom')"></vxe-button>
          </template>
        </vxe-button>
        <vxe-button type="text" @click="removeRow(row)">删除</vxe-button>
      </template>
    </vxe-column>
  </vxe-table>

  <FieldValueEditor ref="valueEditorModalRef"></FieldValueEditor>
</template>

<script lang="ts" setup>
import { reactive, ref, computed } from "vue"
import FieldValueEditor from "@/components/fields/FieldValueEditor.vue"

import {
  VxeGridConstructor,
  VxeTableInstance,
  VxeTableConstructor,
  VxeTableDefines,
  VxeTablePrivateMethods,
  VxeTablePropTypes,
} from "vxe-table/types/all"

const props = withDefaults(
  defineProps<{
    height?: string
    fields?: FieldInfo[]
    dataTypes?: DataTypeSelectOption[]
  }>(),
  {
    height: "500px",
    fields: () => [],
    dataTypes: () => {
      return []
    },
  }
)

const tableRef = ref<VxeTableInstance<FieldInfo>>()
const valueEditorModalRef = ref()
const height = ref(props.height || "500px")

// 可选择的数据类型
const valueDataTypes = computed(() => {
  return props.dataTypes
})

/**
 * 必须要有id，否则数据不显示
 */
const tableData = ref<FieldInfo[]>([])

const editConfig = reactive<VxeTablePropTypes.EditConfig<FieldInfo>>({
  trigger: "click",
  mode: "cell",
  beforeEditMethod: (params: {
    row: FieldInfo
    rowIndex: number
    column: VxeTableDefines.ColumnInfo<FieldInfo>
    columnIndex: number
    $table: VxeTableConstructor<FieldInfo> & VxeTablePrivateMethods<FieldInfo>
    $grid: VxeGridConstructor<FieldInfo> | null | undefined
  }) => {
    // 只有叶子结点可编辑
    return params.row.leaf != undefined && params.row.leaf
  },
})

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
 * 新增行
 * @param currRow
 * @param locat
 */
const insertRow = async (currRow: FieldInfo, locat: string) => {
  const $table = tableRef.value
  if ($table) {
    // 如果 null 则插入到目标节点顶部
    // 如果 -1 则插入到目标节点底部
    // 如果 row 则有插入到效的目标节点该行的位置
    const rid = Date.now()
    if (locat === "current") {
      const record = {
        fieldKey: "newField",
        id: rid,
        parentId: currRow.parentId, // 父节点必须与当前行一致
        leaf: true,
      } as FieldInfo
      const { row: newRow } = await $table.insertAt(record, currRow)
      await $table.setEditRow(newRow) // 插入子节点
    } else if (locat === "top") {
      const record = {
        fieldKey: "newField",
        id: rid,
        parentId: currRow.id, // 需要指定父节点，自动插入该节点中
        leaf: true,
      } as FieldInfo
      const { row: newRow } = await $table.insert(record)
      await $table.setTreeExpand(currRow, true) // 将父节点展开
      await $table.setEditRow(newRow) // 插入子节点
    } else if (locat === "bottom") {
      const record = {
        fieldKey: "newField",
        id: rid,
        parentId: currRow.id, // 需要指定父节点，自动插入该节点中
        leaf: true,
      } as FieldInfo
      const { row: newRow } = await $table.insertAt(record, -1)
      await $table.setTreeExpand(currRow, true) // 将父节点展开
      await $table.setEditRow(newRow) // 插入子节点

      // 修改当前节点为父节点
      currRow.leaf = false
      currRow.dataType = undefined
    }
  }
}

const insertNextRow = async (currRow: FieldInfo, locat: string) => {
  const $table = tableRef.value
  if ($table) {
    // 如果 null 则插入到目标节点顶部
    // 如果 -1 则插入到目标节点底部
    // 如果 row 则有插入到效的目标节点该行的位置
    const rid = Date.now()
    if (locat === "current") {
      const record = {
        fieldKey: "newField",
        id: rid,
        parentId: currRow.parentId, // 父节点必须与当前行一致
        leaf: true,
      } as FieldInfo
      const { row: newRow } = await $table.insertNextAt(record, currRow)
      await $table.setEditRow(newRow) // 插入子节点
    }
  }
}

const removeRow = async (row: FieldInfo) => {
  const $table = tableRef.value
  if ($table) {
    await $table.remove(row)

    // 如果子节点为空，则修改为叶子结点
    if (row.parentId != undefined) {
      tableData.value
        .filter((f) => f.id == row.parentId)
        .forEach((f) => (f.leaf = f.children?.length == 0))
    }
  }
}

defineExpose({
  /**
   * 获取所有字段
   */
  getFields() {
    return tableData.value
  },
  /**
   * 替换所有字段数据
   * @param fields
   */
  setFields(fields?: FieldInfo[]) {
    tableData.value = fields || []
    expandAll()
  },
})
</script>

<style lang="scss" scoped>
.datatype-select-drop-container {
  background-color: red;
}
</style>
