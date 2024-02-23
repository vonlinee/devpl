<template>
  <vxe-modal v-model="visible" :title="!dataForm.group?.id ? '新增' : '修改'" :mask-closable="false" show-footer width="50%" :draggable="false">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="80px" @keyup.enter="submitHandle()">
      <el-form-item label="组名称" prop="groupName">
        <el-input v-model="dataForm.group.groupName" placeholder="组名称"></el-input>
      </el-form-item>
      <el-form-item label="字段信息" prop="fields">
        <template #default>
          <el-table height="400" :data="dataForm.fields">
            <el-table-column type="selection"></el-table-column>
            <el-table-column label="Key" prop="fieldKey"></el-table-column>
            <el-table-column label="名称" prop="fieldName"></el-table-column>
            <el-table-column label="数据类型" prop="dataType"></el-table-column>
            <el-table-column label="注释" prop="comment" show-overflow-tooltip></el-table-column>
            <el-table-column label="操作" fixed="right" width="80">
              <template #default="scope">
                <el-button link @click="removeRow(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button class="mt-4" style="width: 100%" @click="showFieldSelectModal">选择
          </el-button>
        </template>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </vxe-modal>

  <FieldSelectModal ref="fieldSelectModal" @selection-change="handleSelection"></FieldSelectModal>
</template>

<script setup lang="ts">
import { reactive, ref, toRaw } from "vue";
import { ElMessage } from "element-plus/es";
import FieldSelectModal from "@/views/fields/FieldSelectModal.vue";
import { apiListGroupFieldsById, apiUpdateFieldGroup } from "@/api/fields";

const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();
const fieldSelectModal = ref();

const dataForm = reactive<{
  group: FieldGroup
  fields: FieldInfo[]
}>({
  group: {
    id: -1,
    groupName: ""
  },
  fields: []
});

const removeRow = (row: FieldInfo) => {
  const rawRow = toRaw(row);
  const index = dataForm.fields.indexOf(rawRow);
  if (index >= 0) {
    dataForm.fields.splice(index, 1);
  }
};

const handleSelection = (val: FieldInfo[]) => {
  const oldValue = dataForm.fields || [];
  if (oldValue.length == 0) {
    dataForm.fields = oldValue.concat(val);
  } else {
    for (let i = 0; i < val.length; i++) {
      if (!oldValue.find((item) => item.fieldKey == val[i].fieldKey)) {
        oldValue.push(val[i]);
      }
    }
  }
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
    if (!valid || dataForm.group == undefined) {
      return false;
    }
    apiUpdateFieldGroup(dataForm.group, dataForm.fields).then(() => {
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
  show: (group?: FieldGroup) => {
    visible.value = true;
    // 重置表单数据
    if (dataFormRef.value) {
      dataFormRef.value.resetFields();
    }
    // id 存在则为修改
    if (group) {
      dataForm.group = group;

      apiListGroupFieldsById(group.id).then((res) => {
        dataForm.fields = res.data;
      });
    }
  }
});
</script>
