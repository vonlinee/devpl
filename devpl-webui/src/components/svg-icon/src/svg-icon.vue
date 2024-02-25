<template>
  <div class="svg-icon" :style="`display: inline-block; cursor: ${cursor}`">
    <svg
      :class="`${className}`"
      :style="`color:${color}; width:${size}; height:${size};`"
      aria-hidden="true"
    >
      <use :xlink:href="iconName" />
    </svg>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue"

const {icon, color, className, size, cursor} = defineProps({
  // 图标名称
  icon: {
    type: String,
    required: true,
  },
  color: {
    type: String,
    default: "",
  },
  className: {
    type: String,
    default: "",
  },
  size: {
    type: String,
    default: "1em",
  },
  cursor: {
    type: String,
    default: "default",
  },
})

// https://www.iconfont.cn 图标库需使用前缀 icon- 才能匹配
const iconName = computed(() => `#icon-${icon.replace("icon-", "")}`)

</script>

<!--iconfont为何要设置vertical-align: -0.15em? https://blog.csdn.net/ddx2019/article/details/109047831-->
<style scoped>
.svg-icon svg {
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
  flex-shrink: 0;
}
</style>
