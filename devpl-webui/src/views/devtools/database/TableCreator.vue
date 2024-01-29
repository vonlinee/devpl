<template>
  <div>
    <el-button @click="addColumn">添加字段</el-button>
  </div>

  <el-tabs>
    <el-tab-pane label="字段">
      <el-table height="500px"
        :data="columns"
        border
        style="width: 100%"
        show-overflow-tooltip
      >
        <el-table-column prop="columnName" label="列名" width="180">
          <template #default="scope">
            <el-input v-model="scope.row.columnName"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="dataType"
          label="数据类型"
          width="180"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-select v-model="scope.row.dataType">
              <el-option label="bigint" value="bigint"></el-option>
              <el-option label="varchar" value="varchar"></el-option>
              <el-option label="datetime" value="datetime"></el-option>
              <el-option label="text" value="text"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="columnSize" label="长度" width="130">
          <template #default="scope">
            <el-input-number
              style="width: 100%"
              controls-position="right"
              v-model="scope.row.columnSize"
              :min="1"
              :max="500"
            />
          </template>
        </el-table-column>
        <el-table-column prop="decimalDigits" label="小数点" width="100">
          <template #default="scope">
            <el-input-number
              style="width: 100%"
              controls-position="right"
              v-model="scope.row.decimalDigits"
              :min="1"
              :max="10"
            />
          </template>
        </el-table-column>
        <el-table-column prop="nullable" label="不是null" width="80">
          <template #default="scope">
            <el-switch v-model="scope.row.nullable" />
          </template>
        </el-table-column>
        <el-table-column prop="virtual" label="虚拟" width="70">
          <template #default="scope">
            <el-switch v-model="scope.row.virtual" />
          </template>
        </el-table-column>
        <el-table-column prop="primaryKey" label="主键" width="70">
          <template #default="scope">
            <el-switch v-model="scope.row.primaryKey" />
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="注释">
          <template #default="scope">
            <el-input v-model="scope.row.remarks"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>

    <el-tab-pane label="索引"> </el-tab-pane>

    <el-tab-pane label="选项">
      <el-form>
        <el-form-item label="表名">
          <el-input></el-input>
        </el-form-item>
        <el-form-item label="字符集">
          <el-select>
            <el-option label="utf8mb4" value="utf8mb4"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import { ref } from "vue"

const columns = ref<ColumnInfo[]>([])

const addColumn = () => {
  columns.value?.push({
    columnName: "",
    dataType: "varchar",
    columnSize: 0,
    decimalDigits: 0,
    nullable: true,
    virtual: false,
    primaryKey: false,
    remarks: "",
  })
}

type ColumnInfo = {
  columnName: string
  dataType: string
  columnSize: number
  decimalDigits: number
  nullable: boolean
  virtual: boolean
  primaryKey: boolean
  remarks: string
}
</script>

<style scoped lang="scss">
.toolbar-container {
  display: flex;
  flex-direction: row;
}
</style>
