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
  <button @click="onClick">1111111</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="onClick">222222</button>
  <br>
  <button @click="popupRef.hide()">222222</button>

  <Popup ref="popupRef" :anchor="() => anchor" position="right" trigger="click" :z-index="500">
    <li>A</li>
    <li>B</li>
    <li>C</li>
  </Popup>
</template>
