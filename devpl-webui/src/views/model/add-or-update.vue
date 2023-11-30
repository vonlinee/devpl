<template>
  <vxe-modal v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :mask-closable="false" show-footer width="50%">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="80px" @keyup.enter="submitHandle()">
      <el-form-item label="名称" prop="code">
        <el-input v-model="dataForm.code" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item label="包名" prop="packageName">
        <el-input v-model="dataForm.packageName" placeholder="基类包名"></el-input>
      </el-form-item>
      <el-form-item label="字段信息" prop="fields">
        <template #default>
          <el-table height="400" :data="dataForm.fields">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="Key" prop="fieldKey"></el-table-column>
            <el-table-column label="名称" prop="fieldName"></el-table-column>
            <el-table-column label="数据类型" prop="dataType"></el-table-column>
          </el-table>
          <el-button class="mt-4" style="width: 100%" @click="showFieldSelectModal">选择</el-button>
        </template>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>

  <field-select-modal ref="fieldSelectModal" @selection-change="handleSelection"></field-select-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus/es";
import { apiGetModelById, apiSaveOrUpdateModelById } from "@/api/model";
import FieldSelectModal from "@/views/fields/FieldSelectModal.vue";

const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();
const fieldSelectModal = ref();

type FormDataType = {
  id: string,
  packageName: string,
  code: string,
  remark: string,
  fields: FieldInfo[]
}

const dataForm = reactive<FormDataType>({
  id: "",
  packageName: "",
  code: "",
  remark: "",
  fields: []
});

const handleSelection = (val: FieldInfo[]) => {
  const oldValue = dataForm.fields || [];
  if (oldValue.length == 0) {
    dataForm.fields = oldValue.concat(val);
  } else {
    for (let i = 0; i < val.length; i++) {
      if (!oldValue.find(item => item.fieldKey == val[i].fieldKey)) {
        oldValue.push(val[i]);
      }
    }
  }
};

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  // id 存在则为修改
  if (id) {
    getModelClass(id);
  }
};

const getModelClass = (id: number) => {
  apiGetModelById(id).then(res => {
    Object.assign(dataForm, res.data);
  });
};

const showFieldSelectModal = () => {
  fieldSelectModal.value.show(dataForm.fields);
};

const dataRules = ref({
  packageName: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  code: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

// 表单提交
const submitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    apiSaveOrUpdateModelById(dataForm).then(() => {
      ElMessage.success({
        message: "操作成功",
        duration: 500,
        onClose: () => {
          visible.value = false;
          emit("refreshDataList");
        }
      });
    });
  });
};

defineExpose({
  init
});
</script>
