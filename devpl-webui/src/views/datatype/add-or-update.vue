<template>
  <vxe-modal v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :mask-closable="false" :z-index="1000"
             @hide="resetFields" :show-footer="true">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px" @keyup.enter="submitHandle()">
      <el-form-item label="类型分组" prop="typeGroupId">
        <div>
          <el-select v-model="dataForm.typeGroupId" class="m-2" placeholder="选择类型组">
            <el-option v-for="item in typeGroupOptions" :key="item.typeGroupId" :label="item.typeGroupId"
                       :value="item.typeGroupId">
              <span style="float: left">{{ item.typeGroupId }}</span>
              <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px;">{{ item.typeGroupName
                }}</span>
            </el-option>
          </el-select>
          <el-button :icon="Plus" @click="typeGroupModalVisiable = true"></el-button>
        </div>
      </el-form-item>
      <el-form-item label="类型Key" prop="typeKey">
        <el-input v-model="dataForm.typeKey"></el-input>
      </el-form-item>
      <el-form-item label="类型名称" prop="typeName">
        <el-input v-model="dataForm.typeName"></el-input>
      </el-form-item>
      <el-form-item label="长度范围">
        <el-row>
          <el-col :span="10" style="margin-right: 20px;">
            <el-input v-model.number="dataForm.minLength"></el-input>
          </el-col>
          -
          <el-col :span="10" style="margin-left: 20px;">
            <el-input v-model.number="dataForm.maxLength"></el-input>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="精度" prop="precision">
        <el-input v-model="dataForm.precision"></el-input>
      </el-form-item>
      <el-form-item label="默认值" prop="defaultValue">
        <el-input v-model="dataForm.defaultValue"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="dataForm.remark" show-word-limit maxlength="30"
                  :autosize="{ minRows: 3, maxRows: 4 }"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>

  <vxe-modal v-model="typeGroupModalVisiable" :draggable="false" :loading="loading">
    <vxe-form :data="formData" size="medium" title-align="right" title-width="100" @submit="submitEvent"
              :rules="formRules">
      <vxe-form-item title="分组ID" field="typeGroupId" span="24">
        <template #default="{ data }">
          <vxe-input v-model="data.typeGroupId" placeholder="请输入类型分组ID" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item title="分组名称" field="typeGroupName" span="24">
        <template #default="{ data }">
          <vxe-input v-model="data.typeGroupName" placeholder="请输入类型分组名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item align="right" :span="24">
        <template #default>
          <vxe-button type="submit" status="primary" content="保存"></vxe-button>
        </template>
      </vxe-form-item>
    </vxe-form>
  </vxe-modal>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, toRaw } from "vue";
import { ElButton, ElMessage } from "element-plus/es";
import {
  apiListAllDataTypeGroups,
  apiSaveDataTypeGroup,
  apiSaveDataTypeItems,
  apiUpdateDataTypeItem
} from "@/api/datatype";
import { Plus } from "@element-plus/icons-vue";
import { VxeFormEvents, VxeFormPropTypes } from "vxe-table";
import { isAllLetter } from "@/utils/tool";

const emit = defineEmits(["refreshDataList"]);

interface TypeGroupVO {
  typeGroupId: string;
  typeGroupName: string;
}

const typeGroupOptions = ref<TypeGroupVO[]>([]);

onMounted(() => {
  apiListAllDataTypeGroups().then((res) => {
    if (res.code == 200) {
      typeGroupOptions.value = res.list;
    }
  });
});

const visible = ref(false);
const typeGroupModalVisiable = ref(false);
const dataFormRef = ref();
const loading = ref(false);

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
  remark: string
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
  remark: ""
});

/**
 * 重置表单，将对象置为初始值
 */
const resetFields = () => {

  emit("refreshDataList");

  dataForm.id = undefined;
  dataForm.typeGroupId = "";
  dataForm.typeKey = "";
  dataForm.typeName = "";
  dataForm.defaultValue = "";
  dataForm.minLength = 0;
  dataForm.maxLength = 0;
  dataForm.precision = "";
  dataForm.remark = "";
};

const formData = reactive<TypeGroupVO>({
  typeGroupId: "",
  typeGroupName: ""
});

const submitEvent: VxeFormEvents.Submit = () => {
  loading.value = true;
  apiSaveDataTypeGroup(toRaw(formData)).then((res) => {
    if (res.code == 200) {
      loading.value = false;
      typeGroupModalVisiable.value = false;
      apiListAllDataTypeGroups().then((res) => {
        if (res.code == 200) {
          typeGroupOptions.value = res.list;
        }
      });
    }
  });
};

const formRules = ref<VxeFormPropTypes.Rules>({
  typeGroupId: [
    { required: true, message: "请输入类型分组ID" }, {
      validator({ itemValue }) {
        // 自定义校验
        if (!isAllLetter(itemValue)) {
          return new Error("分组ID为英文字符");
        }
      }
    }
  ],
  typeGroupName: [
    { required: true, message: "请输入类型名称" }
  ]
});

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

// 表单提交
const submitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    if (dataForm.id) {
      apiUpdateDataTypeItem(toRaw(dataForm)).then((res) => {
        if (res.code == 200) {
          ElMessage({
            message: "更新成功",
            duration: 200,
            onClose: () => {
              visible.value = false;
            }
          });
        }
      });
    } else {
      apiSaveDataTypeItems([toRaw(dataForm)]).then((res) => {
        if (res.code == 200) {
          ElMessage({
            message: "保存成功",
            duration: 200,
            onClose: () => {
              visible.value = false;
            }
          });
        }
      });
    }
  });
};

defineExpose({
  init
});
</script>
