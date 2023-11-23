<!-- 
  模板选择组件 模板列表
 -->
<template>
  <el-select v-model="templateId" placeholder="选择模板" filterable>
    <el-option v-for="item in templates" :key="item.templateId" :label="item.templateName" :value="item.templateId">
      <div style=" height: 100%;">
        <span style="float: left">{{ item.templateName }}</span>
        <span style="float: right;" :link="true" @click="() => onPreview(item)">预览</span>
      </div>
    </el-option>
  </el-select>
</template>
<script setup lang='ts'>
import { ref } from 'vue';

interface TemplateSelectorProps {
  templates?: TemplateInfo[]
}

const templateId = ref()

const emits = defineEmits([
  "handlePreview"
])

/**
 * 预览模板信息
 * @param item 模板信息
 */
const onPreview = (item: TemplateInfo) => {
  emits("handlePreview", item)
}

const { templates } = defineProps<TemplateSelectorProps>()

</script>
<style lang='scss' scoped></style>