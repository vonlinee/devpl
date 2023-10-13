<template>
  <el-card>
    <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-form-item>
          <el-input v-model="state.queryForm.columnType" placeholder="字段类型"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="state.queryForm.attrType" placeholder="属性类型"></el-input>
        </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="deleteBatchHandle()">删除</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
              @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="columnType" label="字段类型" header-align="center" align="center"></el-table-column>
      <el-table-column prop="attrType" label="属性类型" header-align="center" align="center"></el-table-column>
      <el-table-column prop="packageName" label="属性包名" header-align="center" align="center"></el-table-column>
      <el-table-column prop="jsonType" label="JSON类型" header-align="center" align="center"></el-table-column>
      <el-table-column prop="mysqlSqlType" label="SQL类型" header-align="center" align="center">
        <template #header="scope">
          <el-text>SQL类型</el-text>
          <el-select style="margin-left: 4px; width: 150px">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template #default="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row.id)">编辑</el-button>
          <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="state.pageSizes" :page-size="state.limit"
                   :total="state.total"
                   layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
                   @current-change="currentChangeHandle">
    </el-pagination>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>

    <vxe-grid v-bind="gridOptions">
      <template #name_edit="{ row }">
        <vxe-input v-model="row.name"></vxe-input>
      </template>
      <template #nickname_edit="{ row }">
        <vxe-input v-model="row.nickname"></vxe-input>
      </template>
      <template #role_edit="{ row }">
        <vxe-input v-model="row.role"></vxe-input>
      </template>
      <template #address_edit="{ row }">
        <vxe-input v-model="row.address"></vxe-input>
      </template>
    </vxe-grid>

  </el-card>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { IHooksOptions } from "@/hooks/interface";
import { useCrud } from "@/hooks";
import AddOrUpdate from "./add-or-update.vue";
import { ElButton } from "element-plus";
import { useVxeGridTable, VDTOptions } from "@/hooks/vxedatatable";
import { apiListDataTypes } from "@/api/datatype";

const state: IHooksOptions = reactive({
  dataListUrl: "/gen/fieldtype/page",
  deleteUrl: "/gen/fieldtype",
  queryForm: {
    columnType: "",
    attrType: "",
    packageName: "",
    jsonType: ""
  }
});

/**
 * 数据类型VO
 */
interface RowVO {
  id: number;
  typeKey: string;
}

let gridOptions = useVxeGridTable({
  columns: [{ type: "checkbox", width: 50 },
    { type: "seq", width: 60 },
    {
      field: "typeGroupId",
      title: "类型组",
      editRender: { autofocus: ".vxe-input--inner" },
      slots: { edit: "name_edit" }
    },
    { field: "typeKey", title: "类型Key", slots: { edit: "nickname_edit" } },
    { field: "typeName", title: "类型名称", editRender: {}, slots: { edit: "role_edit" } },
    { field: "valueType", title: "值类型", showOverflow: true, editRender: {}, slots: { edit: "address_edit" } }],
  queryPage: (currentPage, pageSize) => apiListDataTypes(currentPage, pageSize)
} as VDTOptions);

const addOrUpdateRef = ref();
const addOrUpdateHandle = (id?: number) => {
  addOrUpdateRef.value.init(id);
};

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state);
</script>
