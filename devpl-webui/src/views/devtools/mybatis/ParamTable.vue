<template>
  <el-table border :data="tableData" row-class-name="param-table-row">
    <el-table-column label="名称" prop="fieldKey"></el-table-column>
    <el-table-column label="值" prop="fieldKey"></el-table-column>
    <el-table-column label="数据类型" prop="dataType">
      <template #default="scope">
        <el-select v-model="scope.row.dataType">
          <el-option v-for="dt in dataTypes" :label="dt.label" :key="dt.key" :value="dt.value" />
        </el-select>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-dropdown>
          <el-button type="primary">
            添加
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>同级节点</el-dropdown-item>
              <el-dropdown-item>子节点</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button type="danger" @click="removeRow(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ArrowDown } from "@element-plus/icons";

const tableData = ref<FieldInfo[]>([{
  id: 1,
  fieldKey: "name"
}]);

const dataTypes = ref<DataTypeSelectOption[]>([
  {
    label: "int",
    key: "int",
    value: "int"
  }
]);

const removeRow = (row: FieldInfo) => {
  console.log(row);
};

</script>

<style lang="scss" scoped>

.param-table-row {
  height: 30px;
}

</style>
