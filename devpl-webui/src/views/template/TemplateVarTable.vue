<!--
  模板参数表
-->
<script setup lang="ts">

import { ref } from "vue";

const visible = ref();

defineExpose({
  show(templateInfo: TemplateInfo) {
    visible.value = true;
  }
});

</script>

<template>
  <vxe-modal v-model="visible">

  </vxe-modal>
</template>

<style scoped lang="scss">

</style>