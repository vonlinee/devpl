<template>
  <el-drawer v-model="visible" title="编辑" :size="1200" :with-header="false" :close-on-click-modal="false">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="文件类型" name="target">
        <el-table border :data="generationFiles">
          <el-table-column label="文件名" prop="fileName">
            <template #default="scope">
              <span v-if="!scope.row.editing">{{ scope.row.fileName }}</span>
              <el-input v-if="scope.row.editing" v-model="scope.row.fileName"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="模板" prop="templateId">
            <template #default="scope">
              <span v-if="!scope.row.editing">{{ scope.row.fileName }}</span>
              <template-selector v-if="scope.row.editing" :current="scope.row.templateName ? scope.row.templateId : null"
                :options="templateOptions"
                :on-handle-value-change="(val) => (scope.row.templateId = val)"></template-selector>
            </template>
          </el-table-column>
          <el-table-column label="保存路径" prop="savePath" show-overflow-tooltip>
            <template #default="scope">
              <span v-if="!scope.row.editing">{{ scope.row.savePath }}</span>
              <el-input v-if="scope.row.editing" v-model="scope.row.savePath"></el-input>
            </template>
          </el-table-column>

          <el-table-column fixed="right" label="操作" align="center" width="150px">
            <template #default="scope">
              <el-button link type="primary" @click.prevent="scope.row.editing = !scope.row.editing">
                {{ scope.row.editing ? "保存" : "编辑" }}
              </el-button>
              <el-button link type="primary" @click.prevent="deleteRow(scope.$index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button class="mt-4" style="width: 100%" @click="onAddItem">新增</el-button>
      </el-tab-pane>

      <el-tab-pane label="属性设置" name="field">
        <vxe-table ref="fieldTable" border row-key class="sortable-row-gen" :data="fieldList"
          :checkbox-config="{ checkStrictly: true }" :edit-config="{ trigger: 'click', mode: 'cell' }">
          <vxe-column type="seq" width="50" align="center"></vxe-column>
          <vxe-column width="30" title="拖动">
            <template #default>
              <span class="drag-btn">
                <i class="vxe-icon-sort"></i>
              </span>
            </template>
            <template #header>
              <el-tooltip class="item" effect="dark" content="按住后可以上下拖动排序" placement="top-start">
                <i class="vxe-icon-question-circle-fill"></i>
              </el-tooltip>
            </template>
          </vxe-column>
          <vxe-column field="fieldName" title="字段名"></vxe-column>
          <vxe-column field="fieldComment" title="说明" :edit-render="{ name: 'input' }"></vxe-column>
          <vxe-column field="fieldType" title="字段类型"></vxe-column>
          <vxe-column field="attrName" title="属性名" :edit-render="{ name: 'input' }"></vxe-column>
          <vxe-column field="attrType" title="属性类型">
            <template #default="{ row }">
              <vxe-select v-model="row.attrType" transfer>
                <vxe-option v-for="item in typeList" :key="item.value" :value="item.value"
                  :label="item.label"></vxe-option>
              </vxe-select>
            </template>
          </vxe-column>
          <vxe-column field="autoFill" title="自动填充">
            <template #default="{ row }">
              <vxe-select v-model="row.autoFill" transfer>
                <vxe-option v-for="item in fillList" :key="item.value" :value="item.value"
                  :label="item.label"></vxe-option>
              </vxe-select>
            </template>
          </vxe-column>
          <vxe-column field="primaryKey" title="主键">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.primaryKey"></vxe-checkbox>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>

      <el-tab-pane label="表单配置" name="form">
        <vxe-table ref="formTable" border row-key :data="fieldList" :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }">
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="formItem" title="表单显示">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.formItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="formRequired" title="表单必填">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.formRequired"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="formValidator" title="表单效验" :edit-render="{ name: 'input' }"></vxe-column>
          <vxe-column field="formType" title="表单类型">
            <template #default="{ row }">
              <vxe-select v-model="row.formType" transfer>
                <vxe-option v-for="item in formTypeList" :key="item.value" :value="item.value"
                  :label="item.label"></vxe-option>
              </vxe-select>
            </template>
          </vxe-column>
          <vxe-column field="formDict" title="表单字典类型" :edit-render="{ name: 'input' }"></vxe-column>
        </vxe-table>
      </el-tab-pane>
      <el-tab-pane label="列表配置" name="grid">
        <vxe-table ref="gridTable" border row-key :data="fieldList" :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }">
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="gridItem" title="列表显示">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.gridItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="gridSort" title="列表排序">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.gridSort"></vxe-checkbox>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
      <el-tab-pane label="查询配置" name="query">
        <vxe-table ref="queryTable" :border="true" row-key :data="fieldList" :checkbox-config="{ checkStrictly: true }"
          :edit-config="{ trigger: 'click', mode: 'cell' }">
          <vxe-column field="attrName" title="属性名"></vxe-column>
          <vxe-column field="fieldComment" title="说明"></vxe-column>
          <vxe-column field="queryItem" title="查询显示" width="80" align="center">
            <template #default="{ row }">
              <vxe-checkbox v-model="row.queryItem"></vxe-checkbox>
            </template>
          </vxe-column>
          <vxe-column field="queryType" title="查询方式" width="80" align="center">
            <template #default="{ row }">
              <vxe-select v-model="row.queryType" transfer>
                <vxe-option v-for="item in queryList" :key="item.value" :value="item.value"
                  :label="item.label"></vxe-option>
              </vxe-select>
            </template>
          </vxe-column>
          <vxe-column field="queryFormType" title="查询表单类型" width="160" align="center">
            <template #default="{ row }">
              <vxe-select v-model="row.queryFormType" transfer>
                <vxe-option v-for="item in formTypeList" :key="item.value" :value="item.value"
                  :label="item.label"></vxe-option>
              </vxe-select>
            </template>
          </vxe-column>
        </vxe-table>
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle()">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue"
import {
  ElMessage,
  TabsPaneContext,
  ElTable,
  ElTableColumn,
  ElTabs,
  ElTabPane,
  ElDrawer,
  ElButton,
  ElTooltip,
} from "element-plus/es"
import Sortable from "sortablejs"
import { apiGetGenTableById, apiUpdateGenTableFields } from "@/api/table"
import { useFieldTypeListApi } from "@/api/fieldType"
import { VxeTableInstance } from "vxe-table"
import {
  apiListGenerationFiles,
  apiSaveGenerationFileConfig,
} from "@/api/generator"
import TemplateSelector from "./TemplateSelector.vue"
import { apiListSelectableTemplates } from "@/api/template"

const activeName = ref()
const fieldTable = ref<VxeTableInstance>()
const formTable = ref<VxeTableInstance>()
const gridTable = ref<VxeTableInstance>()
const queryTable = ref<VxeTableInstance>()

const handleClick = (tab: TabsPaneContext) => {
  if (tab.paneName !== "target") {
    formTable.value?.loadData(fieldList.value)
    gridTable.value?.loadData(fieldList.value)
    queryTable.value?.loadData(fieldList.value)
  }
}

const templateOptions = ref<TemplateSelectVO[]>()

onMounted(() => {
  apiListSelectableTemplates().then((res) => (templateOptions.value = res.data))
})

const deleteRow = (index: number) => {
  generationFiles.value?.splice(index, 1)
}

const onAddItem = () => {
  generationFiles.value?.push({
    id: 0,
    tableId: 0,
    templateId: 0,
    templateName: 0,
    fileName: "",
    savePath: "",
    editing: true,
  })
}

const emit = defineEmits(["refreshDataList"])
const visible = ref(false)
const dataFormRef = ref()

const sortable = ref() as any

const typeList = ref([]) as any
const tableId = ref()
const generationFiles = ref<TableFileGeneration[]>()
const fieldList = ref<GenTableField[]>([])
const fillList = reactive([
  { label: "DEFAULT", value: "DEFAULT" },
  { label: "INSERT", value: "INSERT" },
  { label: "UPDATE", value: "UPDATE" },
  { label: "INSERT_UPDATE", value: "INSERT_UPDATE" },
])

const queryList = reactive([
  { label: "=", value: "=" },
  { label: "!=", value: "!=" },
  { label: ">", value: ">" },
  { label: ">=", value: ">=" },
  { label: "<", value: "<" },
  { label: "<=", value: "<=" },
  { label: "like", value: "like" },
  { label: "left like", value: "left like" },
  { label: "right like", value: "right like" },
])

const formTypeList = reactive([
  { label: "单行文本", value: "text" },
  { label: "多行文本", value: "textarea" },
  { label: "富文本编辑器", value: "editor" },
  { label: "下拉框", value: "select" },
  { label: "单选按钮", value: "radio" },
  { label: "复选框", value: "checkbox" },
  { label: "日期", value: "date" },
  { label: "日期时间", value: "datetime" },
])

const init = (id: number) => {
  visible.value = true
  tableId.value = id

  // 重置表单数据
  if (dataFormRef.value) {
    dataFormRef.value.resetFields()
  }

  activeName.value = "target"

  rowDrop()
  getTable(id)
  getFieldTypeList()
}

const rowDrop = () => {
  nextTick(() => {
    const el: any = window.document.querySelector(
      ".body--wrapper>.vxe-table--body tbody"
    )
    sortable.value = Sortable.create(el, {
      handle: ".drag-btn",
      onEnd: (e: any) => {
        const { newIndex, oldIndex } = e
        const currRow = fieldList.value.splice(oldIndex, 1)[0]
        fieldList.value.splice(newIndex, 0, currRow)
      },
    })
  })
}

const getTable = (id: number) => {
  apiGetGenTableById(id).then((res) => {
    fieldList.value = res.data?.fieldList as GenTableField[]
  })

  apiListGenerationFiles(id).then((res) => {
    generationFiles.value = res.data
  })
}

const getFieldTypeList = async () => {
  typeList.value = []
  // 获取数据
  const { data } = await useFieldTypeListApi()
  // 设置属性类型值
  data.forEach((item: any) => typeList.value.push({ label: item, value: item }))
  // 增加Object类型
  typeList.value.push({ label: "Object", value: "Object" })
}

/**
 * 确定按钮，表单提交
 */
const submitHandle = () => {
  apiUpdateGenTableFields(tableId.value, fieldList.value).then(() => {
    apiSaveGenerationFileConfig(
      tableId.value,
      generationFiles.value || []
    ).then((res) => {
      if (res.data) {
        ElMessage.success({
          message: "操作成功",
          duration: 500,
          onClose: () => {
            visible.value = false
            emit("refreshDataList")
          },
        })
      }
    })
  })
}

defineExpose({
  init,
})
</script>

<style lang="scss">
:deep(.el-tab-pane) {
  height: 100%;
}

:deep(.el-tabs__content) {
  height: calc(100% - 55px);
  overflow-y: auto;
}

.sortable-row-gen .drag-btn {
  cursor: move;
  font-size: 12px;
}

.sortable-row-gen .vxe-body--row.sortable-ghost,
.sortable-row-gen .vxe-body--row.sortable-chosen {
  background-color: #dfecfb;
}
</style>
