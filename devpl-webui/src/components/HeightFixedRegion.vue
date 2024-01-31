<template>
  <div ref="containerRef" :style="containerStyle">
    <slot name="default" :height="height"></slot>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";

const { minus } = defineProps<{
  minus?: number
}>();

const height = ref();

const containerStyle = computed(() => {
  return {
    height: (height.value - (minus || 0)) + "px"
  };
});

onMounted(() => {
  height.value = window.innerHeight;
});
</script>
