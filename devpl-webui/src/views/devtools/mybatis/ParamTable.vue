<template>
  <el-table border :data="tableData" :row-class-name="rowClassNameCallbackFn" row-key="id" default-expand-all
    :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
    <el-table-column label="名称" prop="fieldKey">
      <template #default="{ row }">
        <a class="field-label" v-if="!(row.editing || false)" :title="row.description || '无'" @click="fireInput(row)">{{
          row.fieldKey }}</a>
        <input v-if="row.editing || false" class="field-input" ref="currentInputRef" @blur="row.editing = false"
          @change="(e) => onInputChange(e, row)" @keyup.enter="(e) => onInputChange(e, row)">
      </template>
    </el-table-column>
    <el-table-column label="值" prop="fieldKey">
      <template #default="scope">
        <div style="display: flex; flex-direction: row; height: 100%; align-items: center;">
          <el-text style="flex-grow: 1;">{{ scope.row.value }}</el-text>
          <el-icon v-if="scope.row.leaf" :size="16" @click="openValueEditor(scope.row)" style="cursor: pointer;">
            <Edit />
          </el-icon>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="数据类型" prop="dataType">
      <template #default="scope">
        <el-select v-model="scope.row.dataType">
          <el-option v-for="dt in dataTypes" :label="dt.label" :key="dt.key" :value="dt.value" />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="70px" header-align="center" fixed="right">
      <template #default="scope">
        <div style="display: flex; align-items: center; height: 100%;">
          <el-dropdown>
            <el-icon :size="16" style="cursor: pointer;">
              <Plus />
            </el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="addRow(scope.row)">同级节点</el-dropdown-item>
                <el-dropdown-item @click="addRow(scope.row, 1)">子节点</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-icon :size="16" style="cursor: pointer; margin-left: 10px;" @click="removeRow(scope.row)">
            <Delete />
          </el-icon>
        </div>
      </template>
    </el-table-column>

    <template #empty>
      <el-text @click="addRow()">添加</el-text>
    </template>
  </el-table>

  <FieldValueEditor ref="valueEditorModalRef"></FieldValueEditor>
</template>

<script setup lang="ts">
import { nextTick, ref } from "vue";
import FieldValueEditor from "@/components/fields/FieldValueEditor.vue";
import { Edit, Delete, Plus } from '@element-plus/icons-vue'

const valueEditorModalRef = ref()
const currentInputRef = ref();

const props = defineProps<{
  fields?: FieldInfo[],
  dataTypes?: DataTypeSelectOption[]
}>()

// 表格数据
const tableData = ref<FieldInfo[]>(props.fields || []);
// 可选择的数据类型
const dataTypes = ref<DataTypeSelectOption[]>(props.dataTypes || []);

/**
 * 触发编辑输入
 * @param data 
 */
const fireInput = (data: FieldInfo) => {
  data.editing = true;
  nextTick(() => {
    currentInputRef.value.value = data.fieldKey;
    currentInputRef.value.focus();
  });
};

const openValueEditor = (row: FieldInfo) => {
  valueEditorModalRef.value.open(row)
}

/**
 * 删除指定行
 * @param row 指定的行
 */
const removeRow = (row: FieldInfo) => {
  if (row.parentId) {
    // 递归查找 找到其父节点
    recursiveSearch(tableData.value, f => f.id == row.parentId, p => {
      const children = p.children || []
      if (children.length > 0) {
        let index: number = children.indexOf(row)
        if (index > -1) {
          children.splice(index, 1);
        }
      }
    })
  } else {
    // 直接删除
    let index: number = tableData.value.indexOf(row)
    if (index > -1) {
      tableData.value.splice(index, 1);
    }
  }
};

/**
 * rowClassName回调函数
 * @param param 
 */
const rowClassNameCallbackFn = (data: { row: FieldInfo, rowIndex: number }) => {
  data.row.id = data.rowIndex + 1 // 从1开始
  return "param-table-row"
}

/**
 * 递归搜索节点
 * @param nodes 表格树数据
 * @param condition 搜索条件
 * @param consumer 找到之后的逻辑
 */
const recursiveSearch = (nodes: FieldInfo[], condition: (node: FieldInfo) => boolean, consumer: (node: FieldInfo) => void) => {
  if (nodes == undefined) {
    return
  }
  nodes.forEach(node => {
    if (condition(node)) {
      consumer(node)
    } else {
      recursiveSearch(node.children || [], condition, consumer)
    }
  })
}

/**
 * 新增行，需要确保每行的id唯一
 * @param row 行
 * @param type null或者0添加同级节点
 */
const addRow = (row?: FieldInfo, type?: number) => {

  const newObj = {
    fieldKey: "new",
    editing: false,
    selected: false,
    leaf: true
  } as FieldInfo

  if (dataTypes.value?.length > 0) {
    newObj.dataType = dataTypes.value[0].key
  }

  if (row == undefined) {
    newObj.id = 1
    tableData.value.push(newObj)
    return
  } else {
    if (type == 1) {
      // 设置父级ID
      newObj.parentId = row.id
      const children = row.children || []
      // 添加子节点时 rowClassNameCallbackFn 里拿到的 rowIndex 始终为父节点的行号
      // 因此需要分配一个row-key作为唯一ID
      newObj.id = Number.parseInt(row.id + "" + children.length)
      children.push(newObj)
      row.children = children

      // 如果不是懒加载的话，不要设置hasChildren 这个属性，要不然不能树形显示；如果是懒加载，则需要设置hasChildren字段。
      if (row.children.length > 0) {
        row.leaf = false
      }
    } else {
      // 新增同级节点
      if (row.parentId) {
        // 递归查找 添加同级节点
        recursiveSearch(tableData.value, f => f.id == row.parentId, p => {
          const children = p.children || []
          newObj.parentId = p.id
          newObj.id = Number.parseInt(p.id + "" + children.length)
          children.push(newObj)
          p.children = children
        })
      } else {
        // 根节点新增同级节点
        newObj.id = Number.parseInt(row.id + "" + tableData.value.length)
        tableData.value.splice(tableData.value.length, 0, newObj);
      }
    }
  }
}

const onInputChange = (event: Event, data: FieldInfo) => {
  data.fieldKey = (event.target as HTMLInputElement).value;
  data.editing = false;
};


defineExpose({
  /**
   * 获取所有字段
   */
  getFields() {
    return tableData.value
  }
})


</script>

<style lang="scss" scoped>
.param-table-row {
  height: 30px;
}

.field-input {
  // width: 100%;
  flex-grow: 1;
  height: 100%;
  outline-style: none;
  border: 1px solid #ccc;
  border-radius: 3px;
  padding: 8px;
}
</style>
