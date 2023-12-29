<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-table :data="tableData" row-key="id" border default-expand-all>
          <el-table-column prop="schemaName" label="数据库对象" />
          <el-table-column prop="generatorName" label="生成器" />
        </el-table>
      </el-col>
      <el-col :span="12">
        <el-table></el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts" setup>
interface User {
  id: number
  schemaName: string
  generatorName: string
  hasChildren?: boolean
  children?: User[]
}

const tableData: User[] = [
  {
    id: 3,
    schemaName: "dvepl",
    generatorName: "Skip",
    children: [
      {
        id: 31,
        schemaName: "user_info",
        generatorName: "Skip",
        children: [
          {
            id: 312,
            schemaName: "user_name",
            generatorName: "Skip",
          },
        ],
      },
      {
        id: 32,
        schemaName: "model_info",
        generatorName: "Skip",
      },
    ],
  },
]
</script>
