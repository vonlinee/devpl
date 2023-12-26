<!--
  生成Java pojo类
-->
<template>
  <vxe-modal v-model="visible" title="生成Java Pojo类">

  </vxe-modal>
</template>

<script setup lang="ts">
import { ref } from "vue";

const visible = ref();

defineExpose({
  show(groupId: number) {
    visible.value = true;
  }
});

</script>

<style lang="scss" scoped>

</style>