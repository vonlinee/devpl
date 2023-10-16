<script lang="ts">
import { defineComponent, h, reactive, ref, VNodeArrayChildren } from "vue";
import { VxeColumn, VxeTable, VxeTableProps, VxeTableDataRow, VxeToolbar, VxeButton, VxeTableDefines } from "vxe-table";
// 保存或更新弹窗
import SaveOrUpdate from "./SaveOrUpdate.vue";
// 分页组件
import DataTablePager from "./DataTablePager.vue";
// 工具栏
import DataTableToolBar, { DataTableToolBarProps } from "./DataTableToolBar.vue";
/**
 * 属性选项
 */
export interface DataTableOptions {
  // 列配置项
  columns: VxeTableDefines.ColumnOptions<VxeTableDataRow>[],
  // 表单数据
  formData: {},
  // 是否开启分页
  pageEnabled?: boolean,
  // 分页查询接口
  queryPage: (currentPage: number, pageSize: number) => Promise<any>
}

export interface DataTableProps {
  options: DataTableOptions
}

export default defineComponent({
  name: "DataTable",
  props: {
    options: {}
  },
  setup(props, context) {
    // 获取表格配置项
    const options = props.options as DataTableOptions

    const { formData, columns } = options
    const pageEnabled = options.pageEnabled == undefined || options.pageEnabled == null ? true : options.pageEnabled

    const modalRef = ref()
    const pagerRef = ref()

    /**
     * 编辑事件
     * @param row 
     */
    const editEvent = (row: VxeTableDataRow) => {
      modalRef.value.show()
      Object.assign(formData, row)
    }

    // vxe-table属性
    const vxeTableProps: VxeTableProps = {
      data: [],
      rowConfig: {
        isHover: true
      },
      columnConfig: {
        resizable: true
      },
      border: true
    };

    // 创建列
    const columnsDefs: VNodeArrayChildren = [];
    for (let i = 0; i < columns.length; i++) {
      columnsDefs.push(h(VxeColumn, columns[i]));
    }
    // 操作列
    columnsDefs.push(h(VxeColumn, {
      title: '操作',
      showOverflow: true,
      width: 200,
      align: 'center'
    }, {
      default: () => {
        return h('div', {}, [h(VxeButton, { type: 'text', icon: 'vxe-icon-edit', onClick: editEvent }), h(VxeButton, { type: 'text', icon: 'vxe-icon-delete', onClick: editEvent })])
      }
    }))

    return () => {
      // h 函数 第3个参数传空数组会有警告信息
      // Non-function value encountered for default slot. Prefer function slots
      // 问题原因：Vue3 使用h函数 推荐使用函数式插槽，以便获得更佳的性能。

      let slotVNodes = []

      let op : DataTableToolBarProps = {
        refresh: editEvent
      }

      const toolbarVNode = h(DataTableToolBar, op, context.slots)
      slotVNodes.push(toolbarVNode)
      // 表格
      const tableVNode = h(VxeTable, vxeTableProps, { default: () => columnsDefs })
      // 编辑或修改弹窗
      const saveOrUpdateVNode = h(SaveOrUpdate, {
        ref: modalRef
      }, context.slots)

      slotVNodes.push(tableVNode, saveOrUpdateVNode)

      if (pageEnabled) {
        // 分页
        const pagerVNode = h(DataTablePager, {
          ref: pagerRef
        })
        slotVNodes.push(pagerVNode)
      }

      return h('div', {
        style: {
          class: 'data-table'
        }
      }, slotVNodes);
    };
  }
});

</script>
<style lang="scss" scoped></style>