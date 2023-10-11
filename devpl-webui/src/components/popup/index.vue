<!-- // src/components/popup/index.vue -->
<script setup lang="ts">
// import { Fragment, useAttrs, useSlots } from 'vue'

// import ButtonClose from './button-close.vue';

// const props = defineProps();
// const slots = useSlots()
// const attrs = useAttrs()

// 定义需要外面触发外面的事件
const emits = defineEmits(['close', 'ok', 'cancel'])
const onClose = () => {
  emits('close', Date.now())
}
</script>

<template>
  <div class="popup">
    <div @click="onClose">
      <!-- // 具名插槽close -->
      <slot name="close">
      </slot>
    </div>
    <!-- // 默认插槽default -->
    <slot></slot>
    <!-- <button @click="emits('ok')">ok</button>
    <button @click="emits('cancel')">cancel</button> -->
  </div>
</template>

<style lang="scss" scoped>
.popup {
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100vw;
  height: 100vh;
  z-index: 1;
  // pointer-events: none;
  background-color: rgba(0, 0, 0, 0.1);
  // background-color: rgba(255, 0, 0, 0.377);
}
</style>

