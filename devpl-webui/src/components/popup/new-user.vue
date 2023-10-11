<script setup lang="ts">
const emits = defineEmits(['close', 'ok']);

const okHandle = () => {
  emits('ok');
};
</script>

<template>
  <div class="popup-inner">
    <div class="inner">
      <img
        src="https://img2.baidu.com/it/u=1535537231,3770427295&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750"
        class="poster"
      />
      <div @click="okHandle" class="button-ok">Go!</div>
    </div>
  </div>
  
</template>

