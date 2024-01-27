<script setup lang="ts">

import Popup from "@/views/test/Popup.vue";
import { ref } from "vue";

const anchor = ref();
const popupRef = ref();

const onClick = (event: Event) => {
  popupRef.value.show(event);
};

</script>

<template>
  <button ref="anchor" @click="onClick">1111111</button>

  <Popup ref="popupRef" :anchor="anchor"></Popup>
</template>
