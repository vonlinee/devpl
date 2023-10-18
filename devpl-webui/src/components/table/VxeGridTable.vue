<template>
  <vxe-grid ref="tableRef" v-bind="gridOptions" @cell-dblclick="onCellDbClick">

  </vxe-grid>

  <vxe-modal v-model="addRowModalVisiable" destroy-on-close>
    <slot name="form"></slot>
    <div>
      <el-button status="primary" @click="onFormSubmit">确定</el-button>
    </div>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { reactive, ref, toRaw } from "vue";
import { VxeGridDefines, VxeGridProps } from "vxe-table";
import { VxeTableDefines, VxeTablePropTypes } from "vxe-table/types/table";
import { RowModel } from "@/hooks/vxedatatable";

export type VxeGridOptions = {
  // 是否边框
  border?: boolean;
  // 高度
  height?: VxeTablePropTypes.Height;
  // 是否启用分页
  pageEnabled?: boolean;
  // 列配置项
  columns: VxeTableDefines.ColumnOptions<RowModel>[];
  // 是否自动加载
  autoLoad?: boolean;
  // 查询分页数据
  queryPage?: (currentPage: number, pageSize: number) => Promise<any>;
  // 保存
  save?: (arg: any) => Promise<any>
}

// 新增弹窗
const addRowModalVisiable = ref();
const tableRef = ref();

let { options } = defineProps<{
  options: VxeGridOptions,
}>();

/**
 * 新增表单提交
 * @param args 
 */
const onFormSubmit = (args: any[]): void => {
  if (options.save) {
    options.save(null).then((res: any) => {
      console.log(res);
      addRowModalVisiable.value = false
    })
  }
};

/**
 * 单元格双击出现弹窗
 * @param params 
 */
const onCellDbClick = (params: VxeGridDefines.CellDblclickEventParams<RowModel>): void => {
  let row = toRaw(params.row)
  addRowModalVisiable.value = true
}

/**
 * 转换成 vxe-grid 组件的配置项
 * @param _options
 */
function buildVxeGridOptions(_options: VxeGridOptions) {
  let options = {
    border: true,
    keepSource: true,
    height: 629, // 10 条数据刚好不出现滚动条的高度
    rowConfig: {
      isHover: true
    },
    columnConfig: {
      resizable: true
    },
    pagerConfig: {
      enabled: true,
      perfect: true,
      pageSize: 10
    },
    editConfig: {
      // 使用弹窗进行编辑，不通过点击单元格进行编辑
      enabled: false
    },
    toolbarConfig: {
      buttons: [
        {
          code: "openModal", name: "新增", status: "perfect", icon: "vxe-icon-add", params: {
            modalModelValue: addRowModalVisiable
          }
        }
      ],
      perfect: true,
      refresh: {
        icon: "vxe-icon-refresh",
        iconLoading: "vxe-icon-refresh roll"
      },
      custom: {
        icon: "vxe-icon-menu"
      }
    },
    proxyConfig: {
      props: {
        result: "result",
        total: "page.total"
      },
      ajax: {
        // 接收 Promise
        query: (params: any, ...args: any[]) => {
          return fetchApi(params.page.currentPage, params.page.pageSize);
        },
        // body 对象： { removeRecords }
        delete: (params: any, ...args: any[]) => {
          return delApi(params.body.removeRecords);
        },
        // body 对象： { insertRecords, updateRecords, removeRecords, pendingRecords }
        save: (params: any, ...args: any[]) => {
          return _options.save, params.body.insertRecords;
        }
      }
    },
    columns: _options.columns
  } as VxeGridProps;
  return reactive<VxeGridProps>(options);
}

// 模拟后台接口
const fetchApi = (currentPage: number, pageSize: number) => {
  return new Promise(resolve => {
    const list = [
      { id: 10001, name: "Test1", nickname: "T1", role: "Develop", sex: "Man", age: 28, address: "Shenzhen" },
      { id: 10002, name: "Test2", nickname: "T2", role: "Test", sex: "Women", age: 22, address: "Guangzhou" },
      { id: 10003, name: "Test3", nickname: "T3", role: "PM", sex: "Man", age: 32, address: "Shanghai" }
    ];
    resolve({
      page: {
        total: list.length
      },
      result: list.slice((currentPage - 1) * pageSize, currentPage * pageSize)
    });
  });
};

// 模拟后台接口
const delApi = (removeRecords: []) => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve({
        result: [],
        msg: `delete，${removeRecords.length}条`
      });
    }, 100);
  });
};


const gridOptions = buildVxeGridOptions(options);
</script>
