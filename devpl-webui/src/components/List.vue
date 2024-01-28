<!-- 
  列表组件, 没有虚拟列表功能，展示所有项
-->
<template>
  <ul :style="listStyle">
    <li v-for="item in items" :key="item[key]" class="list-item-container" @click="handleItemClicked($event, item)">
      <!-- 单个项插槽 -->
      <slot name="item" :item="item">{{ item[label] }}</slot>
    </li>
  </ul>
</template>

<script setup lang="ts">
import { computed } from "vue";

const { height, width, key, items } = withDefaults(defineProps<{
  key?: string,
  label?: string,
  height?: string,
  width?: string,
  items: any[]
}>(), {
  key: "key",
  label: "label",
  width: "400px"
});

const listStyle = computed(() => {
  return {
    height: height,
    width: width,
    listStyleType: "none"
  };
});

const emits = defineEmits<{
  (e: "item-clicked", item: any, event: Event): void
}>();

const handleItemClicked = (event: Event, item: any) => {
  emits("item-clicked", item, event);
};

</script>

<style scoped>
.list-item-container {
  padding: 2px 2px 2px 10px;
  cursor: pointer;
  height: 30px;
  /* 文字垂直居中 */
  line-height: 30px;
  vertical-align: middle;
  background-color: #ffffff;
}

.list-item-container:hover {
  background-color: #f6f6f7;
  color: blue;
}
</style>










