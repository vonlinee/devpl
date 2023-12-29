<template>
  <vxe-modal
    v-model="typeGroupModalVisible"
    title="类型分组"
    :loading="loading"
    show-footer
    :show-close="false"
    width="60%"
  >
    <el-table :data="typeGroups" border height="500px">
      <el-table-column label="分组ID" prop="typeGroupId">
        <template #default="{ row }">
          <span v-if="!row.editing">{{ row.typeGroupId }}</span>
          <el-input
            v-if="row.editing"
            v-model="row.typeGroupId"
            placeholder="请输入类型分组ID"
            clearable
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="分组名称" prop="typeGroupName">
        <template #default="{ row }">
          <el-input
            v-if="row.editing"
            v-model="row.typeGroupName"
            placeholder="请输入类型分组名称"
            clearable
          ></el-input>
          <span v-if="!row.editing">{{ row.typeGroupName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" show-overflow-tooltip>
        <template #default="{ row }">
          <el-input
            v-if="row.editing"
            v-model="row.remark"
            clearable
          ></el-input>
          <span v-if="!row.editing">{{ row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            v-if="!scope.row.editing"
            link
            @click="scope.row.editing = !scope.row.editing"
            >编辑</el-button
          >
          <el-button v-if="scope.row.editing" link @click="saveRow(scope.row)"
            >保存</el-button
          >
          <el-button v-if="!scope.row.internal" link>删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <vxe-button content="刷新" @click="refreshTable()"></vxe-button>
      <vxe-button content="新增" @click="insertRow()"></vxe-button>
      <vxe-button content="保存" @click="submitEvent"></vxe-button>
      <vxe-button
        content="取消"
        @click="typeGroupModalVisible = false"
      ></vxe-button>
    </template>
  </vxe-modal>
</template>
<script setup lang="ts">
import {
  apiListAllDataTypeGroups,
  apiSaveDataTypeGroup,
  apiSaveOrUpdateDataTypeGroups,
} from "@/api/datatype"
import { onMounted, ref } from "vue"

const loading = ref(false)

const typeGroups = ref<DataTypeGroup[]>([])

const insertRow = () => {
  const groups = typeGroups.value
  groups.push({
    id: undefined,
    typeGroupId: "",
    typeGroupName: "",
    remark: "",
    editing: true,
    internal: false,
  })
  typeGroups.value = groups
}

const refreshTable = () => {
  apiListAllDataTypeGroups().then((res) => {
    typeGroups.value = res.data || []
  })
}

onMounted(() => {
  refreshTable()
})

const typeGroupModalVisible = ref(false)

const saveRow = (row: DataTypeGroup) => {
  apiSaveDataTypeGroup(row).then((res) => {
    row.editing = false
  })
}

const submitEvent = () => {
  loading.value = true
  apiSaveOrUpdateDataTypeGroups(typeGroups.value).then((res) => {
    typeGroups.value.forEach((item) => {
      item.editing = false
    })
    loading.value = false
  })
}

const emit = defineEmits(["close"])

defineExpose({
  show: () => {
    typeGroupModalVisible.value = true
  },
})
</script>
<style lang="scss" scoped></style>
