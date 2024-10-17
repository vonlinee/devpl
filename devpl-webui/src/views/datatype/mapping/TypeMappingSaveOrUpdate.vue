3<template>
  <vxe-modal v-model="visible" title="数据类型映射设置" :mask-closable="false" :width="800" :show-footer="true"
    @close="resetFields">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="110px" label-position="top">
      <el-row>
        <el-col :span="12">
          <el-form-item label="类型分组" prop="typeGroupId">
            <el-select v-model="dataForm.typeGroupId" placeholder="选择类型组" style="width: 100%"
              @change="handleTypeGroupChanged">
              <el-option v-for="item in typeGroupOptions" :key="item.typeGroupId" :label="item.typeGroupId"
                :value="item.typeGroupId">
                <span style="float: left">{{ item.typeGroupId }}</span>
                <span style="
                float: right;
                color: var(--el-text-color-secondary);
                font-size: 13px;
              ">{{ item.typeGroupName }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="映射类型分组" prop="anotherTypeGroupId">
            <el-select v-model="dataForm.anotherTypeGroupId" placeholder="选择类型组" style="width: 100%"
              @change="handleMappedTypeGroupChanged">
              <el-option v-for="item in typeGroupOptions" :key="item.typeGroupId" :label="item.typeGroupId"
                :value="item.typeGroupId" :disabled="item.typeGroupId == dataForm.typeGroupId">
                <span style="float: left">{{ item.typeGroupId }}</span>
                <span style="
                float: right;
                color: var(--el-text-color-secondary);
                font-size: 13px;
              ">{{ item.typeGroupName }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="主类型" prop="typeKey">
            <el-select v-model="dataForm.typeId" placeholder="选择类型" style="width: 100%" filterable>
              <el-option v-for="item in dataTypeOptions" :key="item.key" :label="item.label" :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="映射类型Key" prop="anotherTypeKeyList">
            <el-select v-model="dataForm.anotherTypeIds" multiple placeholder="选择类型" style="width: 100%" filterable>
              <el-option v-for="item in mappedDataTypeOptions" :key="item.key" :label="item.label" :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, toRaw } from "vue"
import { ElButton, FormInstance, FormRules } from "element-plus/es"
import {
  apiAddDataTypeMapping,
  apiListAllDataTypeGroups,
  apiListDataTypeOptions,
} from "@/api/datatype"
import { Message } from "@/hooks/message";

const emit = defineEmits(["refreshDataList"])

const typeGroupOptions = ref<DataTypeGroup[]>([])

const dataTypeOptions = ref<DataTypeSelectOption[]>([])
const mappedDataTypeOptions = ref<DataTypeSelectOption[]>([])
onMounted(() => {
  apiListAllDataTypeGroups().then((res) => {
    typeGroupOptions.value = res.data || []
  })
})

const visible = ref(false)

const dataFormRef = ref<FormInstance>()

/**
 * 表单数据模型
 */
interface FormVO {
  groupId: number | string | undefined
  typeGroupId: string
  anotherTypeGroupId: string | undefined
  typeId: number | undefined
  anotherTypeIds: number[] | []
}

const dataForm = reactive<FormVO>({
  groupId: undefined,
  typeGroupId: "",
  anotherTypeGroupId: "",
  typeId: undefined,
  anotherTypeIds: [],
})

const resetForm = () => {
  dataForm.typeGroupId = ""
  dataForm.typeId = undefined
}

const dataRules = reactive<FormRules<FormVO>>({
  typeGroupId: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  anotherTypeGroupId: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  typeId: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  anotherTypeIds: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
})

/**
 * 重置表单，将对象置为初始值
 */
const resetFields = () => {
  resetForm()
  emit("refreshDataList")
}

/**
 * 初始化
 * @param row 新增为null，修改不为null
 */
const show = (row?: FormVO) => {
  visible.value = true
  if (row) {
    dataForm.groupId = row.groupId
    dataForm.typeGroupId = row.typeGroupId
    dataForm.anotherTypeGroupId = row.anotherTypeGroupId

    if (row.typeGroupId) {
      handleTypeGroupChanged(row.typeGroupId)
    }
    if (row.anotherTypeGroupId) {
      handleMappedTypeGroupChanged(row.anotherTypeGroupId)
    }
  }
}

const closeAndRefresh = () => {
  resetFields()
  visible.value = false
}

const handleTypeGroupChanged = (val: string) => {

  // 主类型和映射类型相同，则重置映射类型
  if (dataForm.anotherTypeGroupId != null && val == dataForm.anotherTypeGroupId) {
    dataForm.anotherTypeGroupId = undefined
  }

  apiListDataTypeOptions(val).then((res) => {
    dataTypeOptions.value = res.data
  })
}

const handleMappedTypeGroupChanged = (val: string) => {
  apiListDataTypeOptions(val).then((res) => {
    mappedDataTypeOptions.value = res.data
  })
}

// 表单提交
const submitHandle = async () => {

  if (dataFormRef.value) {
    await dataFormRef.value.validate((valid, fields) => {
      if (valid) {
        apiAddDataTypeMapping(dataForm).then((res) => {
          Message.info("添加成功")

          closeAndRefresh()
        })
      } else {
        return false
      }
    })
  }
}

defineExpose({
  show,
})
</script>
