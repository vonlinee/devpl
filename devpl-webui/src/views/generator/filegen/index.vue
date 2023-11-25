<template>
  <el-card>
    <el-dropdown split-button type="primary" @click="handleClick" @command="handleCommand">
      <span style="width: 100px">{{ currentFillStrategyName }}</span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :command="{ type: 1, name: '数据库表' }">数据库表</el-dropdown-item>
          <el-dropdown-item :command="{ type: 2, name: '自定义' }">自定义</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-card>

  <table-select ref="tableSelectRef" @selection-callback="onSelectionCallback"></table-select>

  <gen-template-select ref="templateSelectRef" @selection-callback="onTemplateSelectionCallback"></gen-template-select>

  <vxe-table :loading="loading" ref="tableRef" :data="tableData" border default-expand-all
    :tree-config="{ transform: true, rowField: 'id', parentField: 'parentId', expandAll: true }">
    <vxe-table-column type="checkbox" width="55" />
    <vxe-table-column field="itemName" title="名称" head-align="center" tree-node />
    <vxe-table-column field="fillStrategyName" title="数据填充策略" />
    <vxe-table-column title="操作" align="center">
      <template #default="scope">
        <el-button v-if="!scope.row.parentId" :link="true" @click="showTemplateSelectModal(scope.row)">选择模板</el-button>
        <el-button v-if="scope.row.parentId" :link="true">编辑</el-button>
        <el-button :link="true" @click.prevent="removeRow(scope.row)">删除</el-button>
      </template>
    </vxe-table-column>
  </vxe-table>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { apiListSelectableTemplates } from "@/api/template";
import TableSelect from "@/views/generator/filegen/TableSelect.vue";
import GenTemplateSelect from "./GenTemplateSelect.vue";

const tableData = ref<TemplateFileGeneration[]>([]);
const loading = ref(false)
const currentFillStrategy = ref(1);
const currentFillStrategyName = ref("数据库表");
const templateOptions = ref<TemplateSelectVO[]>();

const tableSelectRef = ref();
const templateSelectRef = ref();
const tableRef = ref();
const currentFileGenGroupRef = ref<TemplateFileGeneration>();
onMounted(() => {
  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data || [];
  });
});

const handleClick = () => {
  if (currentFillStrategy.value == 1) {
    tableSelectRef.value.show();
  } else {
    addCustomFileGenerationItem();
  }
};

const handleCommand = (fillStrategy: {
  type: number,
  name: string
}) => {
  currentFillStrategy.value = fillStrategy.type;
  currentFillStrategyName.value = fillStrategy.name;
};

const currentId = ref(1);
const getIncrId = () => {
  currentId.value++;
  return currentId.value
}

const onSelectionCallback = (tableNames: string[]) => {
  loading.value = true
  for (let i = 0; i < tableNames.length; i++) {
    tableData.value.push({
      id: getIncrId(),
      templateId: 0,
      templateName: "",
      fillStrategy: 1,
      fillStrategyName: "数据库表",
      itemName: tableNames[i],
      children: []
    });
  }
  loading.value = false
}

const showTemplateSelectModal = (row: TemplateFileGeneration) => {
  currentFileGenGroupRef.value = row
  templateSelectRef.value.show()
}

/**
 * 新增模板配置
 * @param templates 
 */
const onTemplateSelectionCallback = async (templates: TemplateSelectVO[]) => {
  const currentRow = currentFileGenGroupRef.value

  const $table = tableRef.value
  if ($table) {
    const newRows = templates.map((template) => {
      const record = {
        itemName: template.templateName,
        id: Date.now(),
        fillStrategy: currentRow?.fillStrategy,
        fillStrategyName: currentRow?.fillStrategyName,
        parentId: currentRow?.id, // 需要指定父节点，自动插入该节点中
      } as TemplateFileGeneration

      return record
    })
    // 如果 null 则插入到目标节点顶部
    // 如果 -1 则插入到目标节点底部
    // 如果 row 则有插入到效的目标节点该行的位置
    const { row: newRow } = await $table.insertAt(newRows, -1)
    await $table.setTreeExpand(currentRow, true) // 将父节点展开
  }
}

const addCustomFileGenerationItem = () => {
  tableData.value.push({
    id: getIncrId(),
    templateId: 0,
    templateName: "",
    fillStrategy: 2,
    fillStrategyName: "自定义",
    itemName: "自定义生成",
  });
}

const removeRow = async (row: TemplateFileGeneration) => {
  const $table = tableRef.value
  if ($table) {
    await $table.remove(row)
  }
}

</script>
