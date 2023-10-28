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
      <el-form-item>
        <el-button type="primary" @click="showTypeMappingTable">类型映射表</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
              @selection-change="selectionChangeHandle">
      <el-table-column type="selection" header-align="center" align="center" width="40"></el-table-column>
      <el-table-column prop="typeGroupId" label="类型分组ID" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column prop="typeKey" label="类型Key" header-align="center" align="center" width="180"></el-table-column>
      <el-table-column prop="typeName" label="类型名称" header-align="center" align="center"></el-table-column>
      <el-table-column prop="defaultValue" label="默认值" header-align="center" align="center"></el-table-column>
      <el-table-column prop="remark" label="备注" header-align="center" align="center">
      </el-table-column>
      <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
        <template #default="scope">
          <el-button type="primary" link @click="addOrUpdateHandle(scope.row)">编辑</el-button>
          <el-button type="primary" link @click="deleteBatchHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="state.page" :page-sizes="state.pageSizes ? state.pageSizes : []" :page-size="state.limit"
                   :total="state.total"
                   layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
                   @current-change="currentChangeHandle">
    </el-pagination>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update ref="addOrUpdateRef" @refresh-data-list="getDataList"></add-or-update>

    <type-mapping-table ref="typeMappingTableRef"></type-mapping-table>
  </el-card>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { IHooksOptions } from "@/hooks/interface";
import { useCrud } from "@/hooks";
import AddOrUpdate from "./add-or-update.vue";
import { ElButton } from "element-plus";
import { apiListDataTypes } from "@/api/datatype";
import TypeMappingTable from "@/views/datatype/TypeMappingTable.vue";

const typeMappingTableRef = ref()

const state: IHooksOptions = reactive({
  dataListUrl: "/gen/fieldtype/page",
  deleteUrl: "/api/datatype/delete",
  pageSizes: [10, 15, 20],
  queryForm: {
    columnType: "",
    attrType: "",
    packageName: "",
    jsonType: ""
  },
  query: apiListDataTypes
} as IHooksOptions);

const showTypeMappingTable = () => {
  typeMappingTableRef.value.show()
}

const addOrUpdateRef = ref();
const addOrUpdateHandle = (row?: any) => {
  addOrUpdateRef.value.init(row);
};

const { getDataList, selectionChangeHandle, sizeChangeHandle, currentChangeHandle, deleteBatchHandle } = useCrud(state);
</script>
