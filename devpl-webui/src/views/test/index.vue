<template>
  <!-- <HeightFixedRegion :minus="200"></HeightFixedRegion> -->

  <button @click="modal.show()">11111</button>

  <el-icon :size="20">
    <Edit />
  </el-icon>
</template>

<script lang="ts" setup>

import { onMounted, ref } from "vue";

const modal = ref()

onMounted(() => {
});

</script>

<style lang="scss"></style>