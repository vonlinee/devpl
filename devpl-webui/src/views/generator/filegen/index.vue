<template>
  <el-card>
    <el-dropdown split-button type="primary" @click="handleClick" @command="handleCommand">
      <span style="width: 100px">{{ currentFillStrategyName }}</span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :command="CodegenStrategy.DB_TABLE">数据库表</el-dropdown-item>
          <el-dropdown-item :command="CodegenStrategy.MANUAL">自定义</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </el-card>

  <table-select ref="tableSelectRef" @selection-callback="onTableSelectionCallback"></table-select>

  <gen-template-select ref="templateSelectRef" @selection-callback="onTemplateSelectionCallback"></gen-template-select>

  <file-gen-config ref="fileGenConfigModalRef" @refresh-data-list="refreshTableData"></file-gen-config>

  <vxe-table :loading="loading" ref="tableRef" :data="tableData" border default-expand-all :height="600"
             :tree-config="{ transform: true, rowField: 'id', parentField: 'parentId', expandAll: true }">
    <vxe-column type="checkbox" width="55" align="center" />
    <vxe-column field="itemName" title="名称" head-align="center" tree-node />
    <vxe-column field="fillStrategyName" title="数据填充策略" />
    <vxe-column title="操作" align="center">
      <template #default="scope">
        <el-button v-if="!scope.row.parentId" :link="true" @click="showTemplateSelectModal(scope.row)">选择模板</el-button>
        <el-button v-if="scope.row.parentId" :link="true" @click="showFileGenConfigModal(scope.row)">编辑</el-button>
        <el-button :link="true" @click.prevent="removeRow(scope.row)">删除</el-button>
      </template>
    </vxe-column>
  </vxe-table>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { apiListSelectableTemplates } from "@/api/template";
import TableSelect from "@/views/generator/filegen/TableSelect.vue";
import GenTemplateSelect from "./GenTemplateSelect.vue";
import FileGenConfig from "./FileGenerationConfig.vue";
import {
  apiAddCustomFileGenUnit,
  apiAddFileGenUnits,
  apiAddTemplateFileGenerations,
  apiListFileGenUnits,
  apiRemoveFileGenUnit
} from "@/api/generator";


/**
 * 代码生成策略
 */
enum CodegenStrategy {
  MANUAL = "manual",
  DB_TABLE = "db_table"
}

const codegenStrategyType = {
  manual: {
    value: "manual",
    label: "自定义填充",
    id: 1
  },
  db_table: {
    value: "db_table",
    label: "数据库表",
    id: 2
  }
};

const tableData = ref<TemplateFileGeneration[]>([]);
const loading = ref(false);
const currentFillStrategy = ref(CodegenStrategy.MANUAL.toString());
const currentFillStrategyName = ref(codegenStrategyType[CodegenStrategy.MANUAL].label);
const templateOptions = ref<TemplateSelectVO[]>();

const tableSelectRef = ref();
const templateSelectRef = ref();
const tableRef = ref();
const fileGenConfigModalRef = ref();
const currentFileGenGroupRef = ref<TemplateFileGeneration>();

const refreshTableData = () => {
  apiListFileGenUnits().then((res) => {
    tableData.value = res.data;
  });
};

const showFileGenConfigModal = (row: TemplateFileGeneration) => {
  fileGenConfigModalRef.value.init();
};

onMounted(() => {
  refreshTableData();

  apiListSelectableTemplates().then((res) => {
    templateOptions.value = res.data || [];
  });
});

const handleClick = () => {
  if (currentFillStrategy.value == "db_table") {
    tableSelectRef.value.show();
  } else {
    addCustomFileGenerationItem();
  }
};

/**
 * 更改生成策略
 * @param codegenStrategy
 */
const handleCommand = (codegenStrategy: CodegenStrategy) => {
  let type = codegenStrategyType[codegenStrategy];
  currentFillStrategy.value = type.value;
  currentFillStrategyName.value = type.label;
};

const onTableSelectionCallback = (tableNames: string[]) => {
  apiAddFileGenUnits(tableNames.map(tableName => {
    return {
      unitName: tableName
    };
  })).then((res) => {
    refreshTableData();
  });
};

/**
 * 模板选择弹窗
 * @param row
 */
const showTemplateSelectModal = (row: TemplateFileGeneration) => {
  currentFileGenGroupRef.value = row;
  templateSelectRef.value.show();
};

/**
 * 新增模板配置
 * @param templates
 */
const onTemplateSelectionCallback = async (templates: TemplateSelectVO[]) => {
  const currentRow = currentFileGenGroupRef.value;
  const $table = tableRef.value;
  if ($table && currentRow && currentRow.id) {
    apiAddTemplateFileGenerations(currentRow?.id, templates, currentFillStrategy.value).then((res) => {
      refreshTableData();
    });
  }
};

const addCustomFileGenerationItem = () => {
  apiAddCustomFileGenUnit({
    unitName: "自定义生成"
  }).then((res) => {
    refreshTableData();
  });
};

const removeRow = async (row: TemplateFileGeneration) => {
  const $table = tableRef.value;
  if ($table && row.id) {
    let type = 1;
    if (row.parentId) {
      type = 2;
    }
    apiRemoveFileGenUnit(row.id, type).then((res) => {
      if (res) {
        refreshTableData();
      }
    });
  }
};
</script>
