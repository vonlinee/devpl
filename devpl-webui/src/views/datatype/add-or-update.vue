<template>
  <vxe-modal v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :mask-closable="false" :z-index="1000" :width="600"
    @close="resetFields" :show-footer="true">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="80px" @keyup.enter="submitHandle()">
      <el-row>
        <el-col :span="20">
          <el-form-item label="类型分组" prop="typeGroupId">
            <el-select v-model="dataForm.typeGroupId" placeholder="选择类型组" style="min-width: 400px;;">
              <el-option v-for="item in typeGroupOptions" :key="item.typeGroupId" :label="item.typeGroupId"
                :value="item.typeGroupId">
                <span style="float: left">{{ item.typeGroupId }}</span>
                <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px;">{{
                  item.typeGroupName
                }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-button :icon="Plus" @click="typeGroupModalVisible = true"></el-button>
        </el-col>
      </el-row>
      <el-form-item label="类型Key" prop="typeKey">
        <el-input v-model="dataForm.typeKey"></el-input>
      </el-form-item>
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="dataForm.typeName"></el-input>
      </el-form-item>
      <el-form-item label="长度范围">
        <range-number v-model:min-value="dataForm.minLength" v-model:max-value="dataForm.maxLength">
        </range-number>
      </el-form-item>
      <el-form-item label="精度" prop="precision">
        <el-input v-model="dataForm.precision"></el-input>
      </el-form-item>
      <el-form-item label="默认值" prop="defaultValue">
        <el-input v-model="dataForm.defaultValue"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="dataForm.remark" show-word-limit maxlength="50"
          :autosize="{ minRows: 3, maxRows: 4 }"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>

</template>

<script setup lang="ts">
import { onMounted, reactive, ref, toRaw } from "vue";
import { ElButton, ElMessage } from "element-plus/es";
import {
  apiListAllDataTypeGroups,
  apiSaveDataTypeItems,
  apiUpdateDataTypeItem
} from "@/api/datatype";
import { Plus } from "@element-plus/icons-vue";
import RangeNumber from "@/components/input/RangeNumber.vue"

const emit = defineEmits(["refreshDataList"]);

const typeGroupOptions = ref<DataTypeGroup[]>([]);

onMounted(() => {
  apiListAllDataTypeGroups().then((res) => {
    typeGroupOptions.value = res.data || [];
  });
});

const visible = ref(false);

const dataFormRef = ref();

/**
 * 表单数据模型
 */
interface FormVO {
  id: number | string | undefined,
  typeGroupId: string,
  typeKey: string,
  typeName: string,
  defaultValue: string,
  minLength: number,
  maxLength: number,
  // 精度
  precision: string,
  remark: string,
  // 是否内部定义的数据类型，内部定义的不可删除
  internal: boolean
}

const dataForm = reactive<FormVO>({
  id: undefined,
  typeGroupId: "",
  typeKey: "",
  typeName: "",
  defaultValue: "",
  minLength: 0,
  maxLength: 0,
  precision: "",
  remark: "",
  internal: false
});

const resetForm = () => {
  dataForm.id = undefined;
  dataForm.typeGroupId = "";
  dataForm.typeKey = "";
  dataForm.typeName = "";
  dataForm.defaultValue = "";
  dataForm.minLength = 0;
  dataForm.maxLength = 0;
  dataForm.precision = "";
  dataForm.remark = "";
}

/**
 * 重置表单，将对象置为初始值
 */
const resetFields = () => {
  resetForm()
  emit("refreshDataList");
};

/**
 * 初始化
 * @param row 新增为null，修改不为null
 */
const init = (row?: any) => {
  visible.value = true;
  if (row) {
    // id 存在则为修改
    if (row.id) {
      dataForm.id = row.id;
      Object.assign(dataForm, row)
      // dataForm.typeGroupId = row.typeGroupId;
      // dataForm.typeKey = row.typeKey;
      // dataForm.typeName = row.typeName;
      // dataForm.defaultValue = row.defaultValue;
      // dataForm.minLength = row.minLength;
      // dataForm.maxLength = row.maxLength;
      // dataForm.precision = row.precision;
      // dataForm.remark = row.remark;
    }
  } else {
    // 重置表单数据
    if (dataFormRef.value) {
      dataFormRef.value.resetFields();
    }
  }
};

const dataRules = ref({
  columnType: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  attrType: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

const closeAndRefresh = () => {
  resetFields()
  visible.value = false;
}

// 表单提交
const submitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    if (dataForm.id) {
      apiUpdateDataTypeItem(toRaw(dataForm)).then((res) => {
        ElMessage({
          message: "更新成功",
          duration: 200,
          onClose: closeAndRefresh
        });
      });
    } else {
      apiSaveDataTypeItems([toRaw(dataForm)]).then((res) => {
        ElMessage({
          message: "保存成功",
          duration: 200,
          onClose: closeAndRefresh
        });
      });
    }
  });
};

defineExpose({
  init
});
</script>
