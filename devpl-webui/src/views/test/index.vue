<template>
  <!-- <HeightFixedRegion :minus="200"></HeightFixedRegion> -->

  <button @click="modal.show()">11111</button>

  <Child></Child>

</template>

<script lang="ts" setup>

import { onMounted, ref } from "vue";
import HeightFixedRegion from "@/components/HeightFixedRegion.vue";
import Modal from "@/components/Modal.vue";
import Child from "./Child.vue";
const modal = ref()

onMounted(() => {
});

</script>

<style lang="scss"></style>