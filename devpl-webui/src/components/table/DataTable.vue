<script lang="ts">
import { defineComponent, h, PropType, ref, toRaw, VNodeArrayChildren } from "vue";
import { VxeButton, VxeColumn, VxeTable, VxeTableDataRow, VxeTableDefines, VxeTableProps } from "vxe-table";
// 保存或更新弹窗
import SaveOrUpdate from "./SaveOrUpdate.vue";
// 分页组件
import DataTablePager from "./DataTablePager.vue";
// 工具栏
import DataTableToolBar, { DataTableToolBarProps } from "./DataTableToolBar.vue";

/**
 * 表格配置选项
 */
export interface DataTableOptions {
  // 列配置项
  columns: VxeTableDefines.ColumnOptions[],
  // 表单数据
  formData: Record<string, any>,
  // 是否开启分页
  pageEnabled?: boolean,
  // 表格数据
  tableData: any[],
  // 分页查询接口
  queryPage: (currentPage: number, pageSize: number) => Promise<any>
}

/**
 * 组件属性
 */
export interface DataTableProps {
  options: DataTableOptions;
}

/**
 * 完全通过render函数渲染组件
 */
export default defineComponent({
  name: "DataTable",
  props: {
    options: Object as PropType<DataTableOptions>
  },
  setup(props, context) {
    // 获取表格配置项
    const options = props.options;

    // @ts-ignore
    if (options == undefined) {
      // 不会为空
      return h("div");
    }

    const { formData, columns } = options;

    const pageEnabled = options.pageEnabled == undefined ? true : options.pageEnabled;

    // 弹窗 SaveOrUpdate
    const modalRef = ref();
    const tableRef = ref();
    const pagerRef = ref();

    /**
     * 编辑事件
     * @param row
     */
    const editEvent = (row: VxeTableDataRow) => {
      Object.assign(formData, row);

      modalRef.value.show();
    };

    // vxe-table属性
    const vxeTableProps: VxeTableProps = {
      ref: tableRef,
      data: options.tableData,
      // 高度
      height: 629,
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
    // 添加操作列
    columnsDefs.push(h(VxeColumn, {
      title: "操作",
      showOverflow: true,
      width: 200,
      align: "center"
    }, {
      default: () => {
        return h("div", {}, [h(VxeButton, {
          type: "text",
          icon: "vxe-icon-edit",
          onClick: editEvent
        }), h(VxeButton, { type: "text", icon: "vxe-icon-delete", onClick: editEvent })]);
      }
    }));

    return () => {
      // h 函数 第3个参数传空数组会有警告信息
      // Non-function value encountered for default slot. Prefer function slots
      // 问题原因：Vue3 使用h函数 推荐使用函数式插槽，以便获得更佳的性能。

      let slotVNodes = [];

      let op: DataTableToolBarProps = {
        refresh: editEvent
      };

      const toolbarVNode = h(DataTableToolBar, op, context.slots);
      slotVNodes.push(toolbarVNode);
      // 表格
      const tableVNode = h(VxeTable, vxeTableProps, { default: () => columnsDefs });
      // 编辑或修改弹窗
      const saveOrUpdateVNode = h(SaveOrUpdate, {
        ref: modalRef
      }, context.slots);

      slotVNodes.push(tableVNode, saveOrUpdateVNode);

      if (pageEnabled) {
        // 分页
        const pagerVNode = h(DataTablePager, {
          ref: pagerRef
        });
        slotVNodes.push(pagerVNode);
      }

      return h("div", {
        style: {
          class: "data-table"
        }
      }, slotVNodes);
    };
  }
});

</script>
<style lang="scss" scoped></style>