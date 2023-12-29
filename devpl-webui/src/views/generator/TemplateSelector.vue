<!-- 
  模板选择组件 模板列表
 -->
<template>
  <el-select
    v-model="templateId"
    placeholder="选择模板"
    filterable
    @change="onSelectedValueChange"
  >
    <el-option
      v-for="item in templates"
      :key="item.templateId"
      :label="item.templateName"
      :value="item.templateId"
    >
      <div style="height: 100%">
        <span style="float: left">{{ item.templateName }}</span>
        <span
          v-if="previewFlag"
          style="float: right"
          :link="true"
          @click="() => onPreview(item)"
          >预览</span
        >
      </div>
    </el-option>
  </el-select>
</template>
<script setup lang="ts">
import { apiListSelectableTemplates } from "@/api/template"
import { onMounted, ref } from "vue"

interface TemplateSelectorProps {
  // 初始选中的模板ID
  current?: number
  // 可选择模板列表
  options?: TemplateSelectVO[]
  // 是否显示预览按钮
  preview?: boolean
}

const emits = defineEmits([
  // 点击预览按钮，预览模板
  "handlePreview",
  // 选择的模板变化
  "handleValueChange",
])

function onSelectedValueChange(value: number) {
  emits("handleValueChange", value)
}

/**
 * 预览模板信息
 * @param item 模板信息
 */
const onPreview = (item: TemplateSelectVO) => {
  emits("handlePreview", item)
}

const { current, options, preview } = defineProps<TemplateSelectorProps>()

const templates = ref(options)
const templateId = ref(current)
const previewFlag = ref(preview || false)

onMounted(() => {
  if (options == undefined) {
    apiListSelectableTemplates().then((res) => {
      templates.value = res.data
    })
  }
})
</script>
<style lang="scss" scoped></style>
