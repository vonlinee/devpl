<template>
  <div class="list-view-container">
    <div v-for="(item, index) in items" :key="index" class="list-item" @click="handleItemClicked($event, item, index)">
      <slot :index="index" :item="item">{{ item }}</slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType, ref, type SlotsType } from 'vue';

// 选中的索引
const selectedIndexs = ref<number[]>([])

const { items } = defineProps({
  items: {
    type: Array as PropType<any[]>,
    required: true
  }
})

const emits = defineEmits<{
  (e: "item-clicked", item: any, event: Event): void
}>();

const handleItemClicked = (event: Event, item: any, index: number) => {
  emits("item-clicked", item, event);
};

</script>

<style lang="scss" scoped>
.list-view-container {
  margin: 16px;
}

.list-item {
  padding: 8px;
  border: 1px solid #ccc;
  margin-bottom: 8px;
  border-radius: 4px;
}

.list-item.selected {
  background-color: #d0e3fd;
  border-color: #007bff;
}
</style>