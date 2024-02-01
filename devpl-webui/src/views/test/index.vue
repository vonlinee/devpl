<template>
  <!-- <HeightFixedRegion :minus="200"></HeightFixedRegion> -->

  <button @click="modal.show()">11111</button>

  <Modal ref="modal">
    <template #default="scope">
      <div :style="{
        backgroundColor: 'gray',
        height: '100%'
      }">{{ scope.size.height }}</div>
    </template>
  </Modal>
  <p>
    <button id="updateDetails">Update details</button>
  </p>

</template>

<script lang="ts" setup>

import { onMounted, ref } from "vue";
import HeightFixedRegion from "@/components/HeightFixedRegion.vue";
import Modal from "@/components/Modal.vue";

const modal = ref()

onMounted(() => {
});

</script>

<style lang="scss"></style>