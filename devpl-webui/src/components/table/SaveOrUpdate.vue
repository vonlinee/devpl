<template>
  <vxe-modal v-model="modalVisiable" title="保存/更新">
    <slot name="saveOrUpdate"></slot>
  </vxe-modal>
</template>
<script setup lang='ts'>
import { ref } from 'vue';

let modalVisiable = ref(false)

defineExpose({
  show() {
    modalVisiable.value = true
  }
})

</script>
<style lang='' scoped>

</style>