<template>

  <el-dropdown split-button type="primary" @click="handleClick" @command="handleCommand">
    <span style="width: 100px">{{ currentFillStrategyName }}</span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item :command="{type:1, name: '数据库表'}">数据库表</el-dropdown-item>
        <el-dropdown-item :command="{type:2, name: '自定义'}">自定义</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  <table-select ref="tableSelectRef"></table-select>

  <el-table
    :data="tableData"
    row-key="id"
    border
    default-expand-all
    height="500px"
  >
    <el-table-column prop="itemName" label="名称" />
    <el-table-column prop="fillStrategy" label="数据填充策略" />
    <el-table-column label="操作">
      <template #default>
        <el-button link>新增</el-button>

        <el-select>
          <el-option v-for="template in templateOptions" :value="template.templateId" :label="template.templateName">
          </el-option>
        </el-select>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { apiListSelectableTemplates } from "@/api/template";
import TableSelect from "@/views/generator/filegen/TableSelect.vue";

type TemplateFileGeneration = {
  // 主键ID
  id: number
  // 名称
  groupName: string
  // 模板ID
  templateId: number
  // 模板名称
  templateName: String
  // 数据填充策略
  fillStrategy: number
  // 数据填充策略名称
  fillStrategyName?: string
  // 是否有子节点
  hasChildren?: boolean
  // 子节点
  children?: TemplateFileGeneration[]
}

const tableData = ref<TemplateFileGeneration[]>([]);

const currentFillStrategy = ref(1);
const currentFillStrategyName = ref("数据库表");
const templateOptions = ref<TemplateSelectVO[]>();

const tableSelectRef = ref();

onMounted(() => {
  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data || [];
  });
});

const handleClick = () => {
  if (currentFillStrategy.value == 1) {
    tableSelectRef.value.show();
  } else {
    onAddItem();
  }
};

const handleCommand = (fillStrategy: {
  type: number,
  name: string
}) => {
  currentFillStrategy.value = fillStrategy.type;
  currentFillStrategyName.value = fillStrategy.name;
};

const id = ref(1);

const onAddItem = () => {
  tableData.value.push({
    id: id.value++,
    templateId: 0,
    templateName: "",
    fillStrategy: 0,
    groupName: ""
  });
};
</script>
