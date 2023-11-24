<!-- 
  模板选择组件 模板列表
 -->
<template>
  <el-select v-model="templateId" placeholder="选择模板" filterable @change="onSelectedValueChange">
    <el-option v-for="item in options" :key="item.templateId" :label="item.templateName" :value="item.templateId">
      <div style=" height: 100%;">
        <el-tooltip class="box-item" effect="dark" :content="item.remark" placement="right-start">
          <template #content> {{ item.remark }} </template>
          <span style="float: left">{{ item.templateName }}</span>
        </el-tooltip>
        <span v-if="previewableFlag" style="float: right;" :link="true" @click="() => onPreview(item)">预览</span>
      </div>
    </el-option>
  </el-select>
</template>
<script setup lang='ts'>
import { ref } from 'vue';

interface TemplateSelectorProps {
  // 初始选中的模板
  current?: number,
  // 模板列表
  options?: TemplateSelectVO[]
  // 是否显示预览按钮
  previewable?: boolean
}

const emits = defineEmits([
  "handlePreview",
  "handleValueChange"
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

const { current, options, previewable } = defineProps<TemplateSelectorProps>()

const templateId = ref(current)
const previewableFlag = ref(previewable || false)
</script>
<style lang='scss' scoped></style>