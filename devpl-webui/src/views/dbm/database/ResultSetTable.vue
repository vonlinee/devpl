<template>
  <div>
    <vxe-grid ref="xGrid" v-bind="gridOptions" v-on="gridEvents"></vxe-grid>

    <vxe-modal v-model="showDetails" title="查看详情" width="800" height="400" resize>
      <template #default>
        <div v-if="selectRow">{{ selectRow.address }}</div>
      </template>
    </vxe-modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { VxeGridInstance, VxeGridListeners, VxeGridPropTypes, VxeGridProps, VxeTableDefines, VxeTablePropTypes } from 'vxe-table'

type ResultSetRowModel = Record<string, any>

/**
 * 初始化所有列
 * @param columns 
 */
const initColumns = (columns: ResultSetColumnMetadata[]) => {
  const columnsOptions: VxeGridPropTypes.Columns<ResultSetRowModel> = [
    { type: 'seq', width: 50 },
  ]
  for (let i = 0; i < columns.length; i++) {
    const resultSetColumn = columns[i];
    const columnOption = {
      field: resultSetColumn.columnName,
      title: resultSetColumn.columnName,
      width: 200,
      showHeaderOverflow: true,
      showOverflow: true
    } as VxeTableDefines.ColumnOptions<ResultSetRowModel>
    columnsOptions.push(columnOption)
  }
  return columnsOptions
}

/**
 * 菜单配置项
 */
const menuConfig: VxeTablePropTypes.MenuConfig<ResultSetRowModel> = {
  body: {
    options: [
      [
        { code: 'copy', name: '复制内容', prefixIcon: 'vxe-icon-copy', visible: true, disabled: false },
        { code: 'clear', name: '清除内容', visible: true, disabled: false },
        { code: 'reload', name: '刷新表格', visible: true, disabled: false }
      ],
      [
        { code: 'myPrint', name: '打印', prefixIcon: 'vxe-icon-print', visible: true, disabled: false },
        { code: 'myExport', name: '导出.csv', prefixIcon: 'vxe-icon-download', visible: true, disabled: false }
      ]
    ]
  },
  /**
   * 控制菜单是否可见
   * https://vxetable.cn/#/table/grid/menu
   * @param param0 
   */
  visibleMethod({ options, column }) {
    return true
  }
}

const gridEvents = reactive<VxeGridListeners<ResultSetRowModel>>({
  cellMenu({ row }) {
    const $grid = xGrid.value
    if ($grid) {
      $grid.setCurrentRow(row)
    }
  },
  menuClick({ menu, row, column }) {
    const $grid = xGrid.value
    if ($grid) {
      switch (menu.code) {
        case 'copy':
          if (row && column) {

          }
          break
        case 'clear':
          $grid.clearData(row, column.field)
          break
        case 'myPrint':
          $grid.print()
          break
        case 'myExport':
          $grid.exportData()
          break
      }
    }
  }
})


/**
 * 初始化表格配置属性
 * @param columns 
 */
const initVxeGridProps = (columns: ResultSetColumnMetadata[]) => {
  const options = {
    border: true,
    showOverflow: true,
    loading: false,
    height: 400,
    columnConfig: {
      resizable: true,
      // 选择列时高亮所在列
      isCurrent: true
    },
    rowConfig: {
      // 选择行时高亮所在行
      isCurrent: true
    },
    menuConfig: menuConfig,
    toolbarConfig: {
      // 关闭工具栏
      enabled: false,
      custom: true,
      slots: {
        buttons: 'toolbar_buttons'
      }
    },
    // 开启虚拟行
    scrollY: {
      enabled: true
    },
    editConfig: {
      trigger: 'click',
      mode: 'cell'
    },
    columns: initColumns(columns)
  } as VxeGridProps<ResultSetRowModel>

  return reactive(options)
}

/**
 * 表格选项
 */
const gridOptions = initVxeGridProps([])

const showDetails = ref(false)
const selectRow = ref<ResultSetRowModel | null>(null)

const xGrid = ref<VxeGridInstance<ResultSetRowModel>>()

defineExpose({
  /**
   * 刷新表格，包括表格列及表格数据
   * @param table 
   */
  refresh(table: DBTableDataVO) {
    const $grid = xGrid.value
    if ($grid) {
      $grid.reloadColumn(initColumns(table.headers))
    }
    this.loadData(table.rows1)
  },

  /**
   * 加载数据
   * @param tableData 
   */
  loadData(tableData: ResultSetRowModel[]) {
    gridOptions.loading = true
    const $grid = xGrid.value
    if ($grid) {
      $grid.loadData(tableData)
      gridOptions.loading = false
    }
  }
})

</script>

<style lang="scss" scoped>
/* 隐藏默认的编辑图标 */
:deep(.vxe-cell--edit-icon) {
  display: none;
}
</style>