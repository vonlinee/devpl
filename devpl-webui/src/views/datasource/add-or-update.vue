<template>
  <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false">
    <el-form ref="dataFormRef"
             label-position="left"
             :model="dataForm" :rules="dataRules" label-width="120px" @keyup.enter="submitHandle()">
      <el-form-item label="连接名" prop="connName">
        <el-col :span="18">
          <el-input v-model="dataForm.connName" placeholder="连接名"></el-input>
        </el-col>
        <el-col :span="6">
          <el-select placeholder="连接名"></el-select>
        </el-col>
      </el-form-item>
      <el-form-item prop="dbType" label="数据库类型">
        <el-select v-model="dataForm.dbType" clearable placeholder="数据库类型" style="width: 100%">
          <el-option value="MySQL" label="MySQL"></el-option>
          <el-option value="Oracle" label="Oracle"></el-option>
          <el-option value="PostgreSQL" label="PostgreSQL"></el-option>
          <el-option value="SQLServer" label="SQLServer"></el-option>
          <el-option value="DM" label="达梦8"></el-option>
          <el-option value="Clickhouse" label="Clickhouse"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item prop="ip" label="IP">
        <el-input v-model="dataForm.ip"></el-input>
      </el-form-item>
      <el-form-item prop="port" label="端口">
        <el-input v-model="dataForm.port"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="dataForm.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="dataForm.password" autocomplete="off" type="password" placeholder="密码"
                  show-password></el-input>
      </el-form-item>
      <el-form-item prop="dbType" label="数据库名称">
        <el-select v-model="dataForm.databaseName" clearable placeholder="数据库名称"
                   @blur="selectBlur"
                   @change="onDbNameChange"
                   @visibleChange="onStartSelect"
                   filterable style="width: 100%">
          <el-option
            v-for="item in databaseNames"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据库URL" prop="connUrl">
        <el-input v-model="dataForm.connUrl" placeholder="数据库URL"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, toRaw } from "vue";
import { ElButton, ElDialog, ElMessage } from "element-plus/es";
import { apiGetDatabaseNames, useDataSourceApi, useDataSourceSubmitApi } from "@/api/datasource";
import { decrypt, encrypt } from "@/utils/tool";

const emit = defineEmits(["refreshDataList"]);

const visible = ref(false);
const dataFormRef = ref();

const dataForm = reactive({
  id: "",
  dbType: "MySQL",
  ip: "127.0.0.1",
  port: 3306,
  databaseName: "",
  connName: "",
  connUrl: "",
  username: "root",
  password: "123456"
});

const init = (id?: number) => {
  visible.value = true;
  dataForm.id = "";

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields();
  }

  // id 存在则为修改
  if (id) {
    getDataSource(id);
  } else {
    // 新增
    dataForm.username = "root";
    dataForm.password = "123456";
  }
};

const getDataSource = (id: number) => {
  useDataSourceApi(id).then(res => {
    res.data.password = decrypt(res.data.password);
    Object.assign(dataForm, res.data);
  });
};

function onDbNameChange(val: string) {
  console.log(val);
}

let flag = ref(false);
let databaseNames = ref<string[]>([]);

/**
 * 第一次选择时初始化数据库名称
 * @param visiable
 */
function onStartSelect(visiable: boolean) {
  if (visiable && databaseNames.value.length == 0) {
    apiGetDatabaseNames(toRaw(dataForm)).then(res => {
      databaseNames.value = res.data;
      // 第一次时自动获取，之后手动获取
      flag.value = true;
    });
  }
}

function selectBlur(event: FocusEvent) {
  if (event.target) {
    let target = event.target as HTMLInputElement
    if (target.value !== '') {
      dataForm.databaseName = target.value;
    }
  }
}

const dataRules = ref({
  dbType: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  ip: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  port: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  connName: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  // connUrl: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  username: [{ required: true, message: "必填项不能为空", trigger: "blur" }],
  password: [{ required: true, message: "必填项不能为空", trigger: "blur" }]
});

// 表单提交
const submitHandle = () => {
  dataFormRef.value.validate((valid: boolean) => {
    if (!valid) {
      return false;
    }
    useDataSourceSubmitApi({ ...dataForm, password: encrypt(dataForm.password) }).then(() => {
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
