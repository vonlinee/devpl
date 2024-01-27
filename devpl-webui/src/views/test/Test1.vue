<template>
  <!--  <TestFieldTree></TestFieldTree>-->

  <!--  <TestPopover></TestPopover>-->

  <button id="button" @click="handleClick">My reference element</button>

  <div id="tooltip" ref="float">My floating element</div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";

import { computePosition } from "@floating-ui/dom";

// import FloatingPane from "@/components/FloatingPane.vue";
// import TestFieldTree from "./TestFieldTree.vue";
// import TestPopover from "@/views/test/TestPopover.vue";

const float = ref();

const handleClick = (e: Event) => {
  float.value.classList.remove("invisible")
};

onMounted(() => {
  const button: HTMLButtonElement | null = document.querySelector("#button");
  const tooltip: HTMLDivElement | null = document.querySelector("#tooltip");

  if (button && tooltip) {
    tooltip.classList.add("invisible")
    computePosition(button, tooltip, {
      placement: "right"
    }).then(({ x, y }) => {

      console.log(x, y);

      Object.assign(tooltip.style, {
        left: `${x}px`,
        top: `${y}px`
      });
    });
  }
});

</script>

<style lang="scss">

.invisible {
  display: none;
}

#tooltip {
  /* Float on top of the UI */
  position: absolute;
  /* Avoid layout interference */
  width: max-content;
  top: 0;
  left: 0;

  height: 300px;
  -webkit-box-shadow: #666 0 0 10px;
  -moz-box-shadow: #666 0 0 10px;
  box-shadow: #666 0 0 10px;
}
</style>