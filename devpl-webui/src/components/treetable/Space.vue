<template>
    <span class="space-container">
        <span class="space" v-for="(item, index) in spaces" v-bind:key="index"></span>
    </span>
</template>
<script lang="ts" setup>
import { computed } from "vue";

const { depth } = defineProps<{
  depth: number | string
}>();

const spaces = computed(() => {
  const aArr = [];
  let maxSpace: number;
  if (typeof depth === "string") {
    maxSpace = Number.parseInt(depth);
  } else {
    maxSpace = depth;
  }
  for (let i = 0; i < maxSpace; i++) {
    aArr.push("");
  }
  return aArr;
});

</script>
<style>
.space {
  display: inline-block;
  width: 15px;
}
</style>
  