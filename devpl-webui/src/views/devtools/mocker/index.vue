<template>
  <div>
    <el-card>
      <el-form :model="form" inline>
        <el-form-item label="数据源">
          <el-select v-model="form.dataSourceId" @change="onDataSourceChange" clearable>
            <el-option v-for="dataSource in dataSourceList" :key="dataSource.id" :label="dataSource.name"
              :value="dataSource.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库">
          <el-select v-model="form.databaseName" @change="onDatabaseChange" clearable>
            <el-option v-for="dbName in databaseNameList" :key="dbName" :label="dbName" :value="dbName"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="表">
          <el-select v-model="form.tableName" @change="onTableChange" clearable>
            <el-option v-for="tableName in tableNameList" :key="tableName" :label="tableName"
              :value="tableName"></el-option>
          </el-select>
        </el-form-item>
        <el-button @click="getMockFields">刷新</el-button>
      </el-form>
    </el-card>
    <el-row>
      <el-col :span="12">
        <el-table border :data="mockFields">
          <el-table-column prop="fieldName" label="属性" />
          <el-table-column prop="generatorName" label="生成器" >
            <template #default="scope">
              <el-select v-model="scope.generatorId">
                <el-option v-for="generator in generatorOptions" :key="generator.name"></el-option>
              </el-select>
            </template>
          </el-table-column>
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
<script setup lang="ts">
import {
  apiGetDatabaseNamesById,
  apiListSelectableDataSources,
  apiListTableNames
} from "@/api/datasource";
import { onMounted, reactive, ref } from "vue";
import { apiListMockFields, apiListMockGenerators } from "@/api/mocker";
import { allHasText, hasText } from "@/utils/tool";

type FormObject = {
  dataSourceId?: number,
  databaseName?: string,
  tableName?: string
}

const form = reactive<FormObject>({});

const mockFields = ref<MockField[]>();
const generatorOptions = ref<GeneratorOption[]>();

const dataSourceList = ref<DataSourceVO[]>();
const databaseNameList = ref<string[]>();
const tableNameList = ref<string[]>();
const mockGenerators = ref<GeneratorItem[]>();
onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSourceList.value = res.data;
    if (res.data && res.data.length > 0) {
      form.dataSourceId = res.data[0].id
      onDataSourceChange(form.dataSourceId)
    }
  });
  apiListMockGenerators().then((res) => {
    mockGenerators.value = res.data
  });
});

function onDataSourceChange(newDataSourceId: number) {
  apiGetDatabaseNamesById(newDataSourceId).then((res) => {
    databaseNameList.value = res.data;
    if (res.data && res.data.length > 0) {
      form.databaseName = res.data[0]
      if (form.databaseName) {
        onDatabaseChange(form.databaseName)
      }
    }
  });
}

function onDatabaseChange(newDatabaseName: string) {
  if (form.dataSourceId) {
    apiListTableNames(form.dataSourceId, newDatabaseName).then((res) => {
      tableNameList.value = res.data;
      if (res.data && res.data.length > 0) {
        form.tableName = res.data[0]
      }
    });
  }
}

function onTableChange(newTableName: string) {
  if (form.dataSourceId && form.databaseName) {
    apiListMockFields(form.dataSourceId, form.databaseName, newTableName).then((res) => {
      mockFields.value = res.data;
    });
  }
}

function getMockFields() {
  if (!allHasText(form.databaseName, form.tableName) || form.dataSourceId == undefined) {
    return;
  }
  if (form.databaseName && form.tableName) {
    apiListMockFields(form.dataSourceId, form.databaseName, form.tableName).then((res) => {
      mockFields.value = res.data;
    });
  }
}


</script>
<style lang="scss" scoped></style>