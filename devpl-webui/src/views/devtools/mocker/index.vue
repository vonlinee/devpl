<template>
  <div>
    <el-card>
      <el-form :model="form" inline>
        <el-form-item label="数据源">
          <el-select v-model="form.dataSourceId" @change="onDataSourceChange">
            <el-option v-for="dataSource in dataSourceList" :key="dataSource.id" :label="dataSource.name"
              :value="dataSource.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库">
          <el-select v-model="form.databaseName" @change="onDatabaseChange">
            <el-option v-for="dbName in databaseNameList" :key="dbName" :label="dbName" :value="dbName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表">
          <el-select v-model="form.tableName" @change="onTableChange">
            <el-option v-for="tableName in tableNameList" :key="tableName" :label="tableName"
              :value="tableName"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
    <el-row>
      <el-col :span="12">
        <el-table border :data="mockFields">
          <el-table-column prop="columnName" label="属性" />
          <el-table-column prop="generatorName" label="生成器" />
        </el-table>
      </el-col>
      <el-col :span="12">
        <el-select>

        </el-select>

        <span>描述信息</span>

        <el-table border :data="generatorOptions">
          <el-table-column prop="name" label="Name" />
          <el-table-column prop="state" label="Value" />
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang='ts'>
import { apiGetDatabaseNamesById, apiListSelectableDataSources, useDataSourceTableListApi } from '@/api/datasource';
import { onMounted, reactive, ref } from 'vue';

const form = reactive({
  dataSourceId: -1,
  databaseName: "",
  tableName: ""
})

const mockFields = ref<DbMockColumn[]>()
const generatorOptions = ref<GeneratorOption[]>()

const dataSourceList = ref<DataSourceVO[]>()
const databaseNameList = ref<string[]>()
const tableNameList = ref<string[]>()

onMounted(() => {
  apiListSelectableDataSources(true).then((res) => {
    dataSourceList.value = res.data
  })
})

function onDataSourceChange(newDataSourceId: number) {
  apiGetDatabaseNamesById(newDataSourceId).then((res) => {
    databaseNameList.value = res.data
  })
}


function onDatabaseChange(newDatabaseName: string) {
  useDataSourceTableListApi(form.dataSourceId, newDatabaseName, null).then((res) => {
    tableNameList.value = res.data
  })
}

function onTableChange(newTableName: string) {

}

</script>
<style lang='scss' scoped></style>