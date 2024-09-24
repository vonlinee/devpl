<!-- 
  数据源驱动管理
 -->
<template>
  <drvier-manager></drvier-manager>

  <el-table :border="true" height="525" :data="option.dataList">
    <el-table-column type="selection" width="60" header-align="center" align="center"></el-table-column>
    <el-table-column prop="dbType" label="数据库类型"></el-table-column>
    <el-table-column prop="fileName" label="驱动文件名"></el-table-column>
    <el-table-column prop="description" label="描述信息" show-overflow-tooltip></el-table-column>
    <el-table-column prop="createTime" label="上传时间" show-overflow-tooltip></el-table-column>
    <el-table-column label="操作" align="center" fixed="right" width="130">
      <template #default="{ row }">
        <vxe-button type="text" status="primary">编辑</vxe-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination :current-page="option.page" background :page-size="option.limit" :page-sizes="option.pageSizes"
    :total="option.total" layout="total, sizes, prev, next, jumper" @size-change="sizeChangeHandle"
    @current-change="currentChangeHandle">
  </el-pagination>

</template>
<script lang='ts' setup>
import { DataTableOption } from "@/hooks/interface";
import DrvierManager from "./DrvierManager.vue"
import { useCrud } from "@/hooks";
import { apiListDriverFiles } from "@/api/datasource";
import { reactive } from "vue";


const option: DataTableOption = reactive({
  pageSizes: [10, 15, 20],
  queryForm: {
    dbType: "",
  },
  queryPage: apiListDriverFiles,
} as DataTableOption)


const { getDataList, sizeChangeHandle, currentChangeHandle, deleteHandle } = useCrud(option)

</script>
<style lang="scss" scoped></style>