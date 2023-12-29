<template>
  <database-mocker></database-mocker>
</template>
<script setup lang="ts">
import {
  apiGetDatabaseNamesById,
  apiListSelectableDataSources,
  apiListTableNames,
} from "@/api/datasource"
import { onMounted, reactive, ref } from "vue"
import { apiListMockFields, apiListMockGenerators } from "@/api/mocker"
import { allHasText, hasText } from "@/utils/tool"
import DatabaseMocker from "@/views/devtools/mocker/DatabaseMocker.vue"

type FormObject = {
  dataSourceId?: number
  databaseName?: string
  tableName?: string
}

const form = reactive<FormObject>({})

const mockFields = ref<MockField[]>()
const generatorOptions = ref<GeneratorOption[]>()

const dataSourceList = ref<DataSourceVO[]>()
const databaseNameList = ref<string[]>()
const tableNameList = ref<string[]>()
const mockGenerators = ref<GeneratorItem[]>()
onMounted(() => {
  apiListSelectableDataSources().then((res) => {
    dataSourceList.value = res.data
    if (res.data && res.data.length > 0) {
      form.dataSourceId = res.data[0].id
      onDataSourceChange(form.dataSourceId)
    }
  })
  apiListMockGenerators().then((res) => {
    mockGenerators.value = res.data
  })
})

function onDataSourceChange(newDataSourceId: number) {
  apiGetDatabaseNamesById(newDataSourceId).then((res) => {
    databaseNameList.value = res.data
    if (res.data && res.data.length > 0) {
      form.databaseName = res.data[0]
      if (form.databaseName) {
        onDatabaseChange(form.databaseName)
      }
    }
  })
}

function onDatabaseChange(newDatabaseName: string) {
  if (form.dataSourceId) {
    apiListTableNames(form.dataSourceId, newDatabaseName).then((res) => {
      tableNameList.value = res.data
      if (res.data && res.data.length > 0) {
        form.tableName = res.data[0]
      }
    })
  }
}

function onTableChange(newTableName: string) {
  if (form.dataSourceId && form.databaseName) {
    apiListMockFields(form.dataSourceId, form.databaseName, newTableName).then(
      (res) => {
        mockFields.value = res.data
      }
    )
  }
}

function getMockFields() {
  if (
    !allHasText(form.databaseName, form.tableName) ||
    form.dataSourceId == undefined
  ) {
    return
  }
  if (form.databaseName && form.tableName) {
    apiListMockFields(
      form.dataSourceId,
      form.databaseName,
      form.tableName
    ).then((res) => {
      mockFields.value = res.data
    })
  }
}
</script>
<style lang="scss" scoped></style>
