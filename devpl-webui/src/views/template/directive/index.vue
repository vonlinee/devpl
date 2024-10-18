<!-- 
  模板指令管理
 -->
<template>
  <el-card>
    <el-form inline v-model="state.queryForm" @keyup.enter="getDataList()">
      <el-form-item>
        <el-input v-model="state.queryForm.directiveName" placeholder="指令名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">刷新</el-button>
        <el-button type="primary" @click="saveOrUpdateRef.show()">新增</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-table :data="state.dataList" :border="true" style="width: 100%" height="600px">
    <el-table-column prop="directiveName" label="指令名称" width="180" />
    <el-table-column prop="remark" label="备注信息" />

    <el-table-column label="操作" width="120px" header-align="center" align="center">
      <template #default="scope">
        <vxe-button type="text" status="primary" @click="saveOrUpdateRef.show(scope.row)">编辑</vxe-button>
        <vxe-button type="text" status="danger" @click="remove(scope.row)">删除</vxe-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination :current-page="state.page" :page-sizes="state.pageSizes" :page-size="state.limit" :total="state.total"
    layout="total, sizes, prev, pager, next, jumper" @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle">
  </el-pagination>

  <save-or-update-directive ref="saveOrUpdateRef" @submit="getDataList"></save-or-update-directive>
</template>
<script lang="ts" setup>
import { ref, reactive } from "vue"
import SaveOrUpdateDirective from "@/views/template/directive/SaveOrUpdateDirective.vue"
import { useCrud } from "@/hooks"
import {
  apiListCustomTemplateDirective,
  apiDeleteCustomTemplateDirective,
} from "@/api/template"
import { Message } from "@/hooks/message"
import { DataTableOption } from "@/hooks/interface"

const saveOrUpdateRef = ref()

const state = reactive({
  queryForm: {
    directiveName: "",
  },
  createdIsNeed: true,
  queryPage: apiListCustomTemplateDirective,
} as DataTableOption)

const { getDataList, sizeChangeHandle, currentChangeHandle } = useCrud(state)

const remove = (row: CustomDirective) => {
  apiDeleteCustomTemplateDirective(row).then((res) => {
    Message.info("删除成功")
    getDataList()
  })
}
</script>
<style lang="scss" scoped></style>
