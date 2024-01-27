<!--
  浮动面板，单例组件
  https://juejin.cn/post/7277799132109422653
-->
<template>
  <div id="popup" class="popup-container">

  </div>
</template>

<script setup lang="ts">

import { onMounted, ref } from "vue";
import { computePosition, VirtualElement } from "@floating-ui/dom";

const { anchor } = defineProps<{
  /**
   * 锚点元素
   */
  anchor: any
}>();

const popupRef = ref<HTMLDivElement>();

const update = (anchor: HTMLElement | VirtualElement, popup: HTMLDivElement) => {
  computePosition(anchor, popup, {
    placement: "right"
  }).then(({ x, y }) => {
    popup.classList.remove("hide");
    Object.assign(popup.style, {
      left: `${x}px`,
      top: `${y}px`
    });
  });
};

onMounted(() => {
  if (anchor) {

    const popup: HTMLDivElement = document.getElementById("popup") as HTMLDivElement;

    if (popup) {
      popupRef.value = popup;
      popup.classList.add("hide");
      update(anchor, popup);
    }
  }
});

/**
 * https://floating-ui.com/docs/virtual-elements
 * @param event
 */
const createVirtualElement = (event: Event) => {
// A virtual element that is 20 x 20 px starting from (0, 0)
  return {
    getBoundingClientRect() {
      if (event.target instanceof HTMLElement) {
        const target = event.target as HTMLElement;
        return target.getBoundingClientRect();
      }
      return {
        x: 0,
        y: 0,
        top: 0,
        left: 0,
        bottom: 20,
        right: 20,
        width: 20,
        height: 20
      };
    },
    contextElement: event.target
  } as VirtualElement;
};

defineExpose({
  show(event: Event) {
    if (popupRef.value) {
      update(createVirtualElement(event), popupRef.value);
    }
  }
});


</script>

<style lang="scss" scoped>

// 隐藏
.hide {
  display: none;
}

.popup-container {
  /* Float on top of the UI */
  position: absolute;
  /* Avoid layout interference */
  top: 0;
  left: 0;

  width: max-content;

  transform: translate(-50%, -50%);
  background-color: white;
  border: 1px solid #ccc;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
</style>
