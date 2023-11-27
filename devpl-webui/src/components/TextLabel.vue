<template>
  <div>
    <el-tooltip v-if="tooltipVisiable" :content="content">
      <div ref="content" class="ellipsis-item">{{ content }}</div>
    </el-tooltip>
    <div v-else ref="contentDivRef" @mouseover="onMouseHover()" class="ellipsis-item">{{ content }}
    </div>
  </div>
</template>
<script setup lang='ts'>
import { ref } from 'vue';
const tooltipVisiable = ref(false)
const contentDivRef = ref<HTMLDivElement>()

const { content } = defineProps<{
  content: string
}>()

function onMouseHover() { // 内容超出，显示文字提示内容
  const tag = contentDivRef.value
  if (tag) {
    tooltipVisiable.value = tag.offsetWidth <= tag.scrollWidth
  }
}

</script>
<style lang='scss' scoped>
.ellipsis-item {
  width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>