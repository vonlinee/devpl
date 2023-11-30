<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { useResizeObserver } from "@vueuse/core";

type MouseDataType = {
  id: number;
  width: number;
  startX: number;
  clientX: number;
};

type PanelType = {
  id: number;
  width: number;
  type: string;
};

const MIN_WIDTH = 100;

const splitPanelRef = ref<HTMLElement | null>(null);
const isDown = ref(false);
// 整个容器的宽高
const container = reactive({
  width: 0
});

// 子容器
const panelList = ref<PanelType[]>([
  { id: 1, width: 100, type: "left" },
  { id: 2, width: 400, type: "center" },
  { id: 3, width: 10, type: "right" }
]);

const mouseData = ref<MouseDataType>({
  id: 0,
  width: 0,
  startX: 0,
  clientX: 0
});

function setMouseData(options: { [key: string]: number }) {
  mouseData.value = {
    ...mouseData.value,
    ...options
  };
}

useResizeObserver(splitPanelRef, (entries) => {
  const entry = entries[0];
  const { width } = entry.contentRect;
  container.width = width;
  initWidth();
});

onMounted(() => {
  document.onmouseup = onMouseUp;
});

function initWidth() {
  container.width = splitPanelRef.value?.clientWidth || 0;
  panelList.value[0].width = 100;
  panelList.value[1].width = container.width - 100 - 400;
  panelList.value[2].width = 400;
}

function resizePanel(options: MouseDataType) {
  if (options.id && options.clientX) {
    const index = drapIndex();
    const max_width = panelList.value.reduce((prev, curr, i) => {
      if (index === i || index + 1 === i) {
        prev += curr.width;
      }
      return prev;
    }, 0);
    panelList.value.forEach((el, i) => {
      if (el.id === options.id) {
        const offset = options.clientX - options.startX;
        el.width = options.width + offset;
        panelList.value[i + 1].width = max_width - (options.width + offset);
      }
    });
  }
}

function drapIndex() {
  return panelList.value.reduce((prev, curr, i) => {
    if (curr.id === mouseData.value.id) prev = i;
    return prev;
  }, -1);
}

function drapLine(item: PanelType) {
  if (item.id === mouseData.value.id && isDown.value) {
    return {
      opacity: 0.5,
      right: -(mouseData.value.clientX - mouseData.value.startX) + "px",
      zIndex: 10
    };
  } else {
    return { opacity: 0, right: 0, zIndex: 0 };
  }
}

function onMouseDown(e: MouseEvent, item: PanelType) {
  //   console.log("down");
  isDown.value = true;
  setMouseData({ id: item.id, width: item.width, startX: e.clientX });
}

function onMouseMove(e: MouseEvent) {
  if (isDown.value) {
    const index = drapIndex();
    const min = panelList.value[index].width - MIN_WIDTH;
    const max = panelList.value[index + 1].width - MIN_WIDTH;
    const { startX } = mouseData.value;
    // 两侧容器的最小宽度为 MIN_WIDTH
    if (e.clientX - startX > max) {
      setMouseData({ clientX: startX + max });
    } else if (e.clientX - startX < -min) {
      setMouseData({ clientX: startX - min });
    } else {
      setMouseData({ clientX: e.clientX });
    }
  }
}

function onMouseUp() {
  console.log("up");
  if (isDown.value) {
    resizePanel(mouseData.value);
    isDown.value = false;
    setMouseData({ id: 0, startX: 0, clientX: 0 });
  }
}
</script>
<template>
  <div class="split-pane" @mousemove="onMouseMove" ref="splitPanelRef">
    <div
      v-for="(item, index) of panelList"
      :key="index + ''"
      class="pane"
      :style="{
        width: item.width + 'px',
      }"
    >
      <slot :name="item.type"></slot>
      <div
        class="handle"
        @mousedown="(e) => onMouseDown(e, item)"
        :style="drapLine(item)"
        v-if="index !== panelList.length - 1"
      ></div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.ha {
  width: 100%;
  // 定义容器高度
  height: calc(100vh - 33px - 30px);
  display: flex;

  .pane {
    position: relative;
    border-right: 1px solid #ccc;

    .handle {
      position: absolute;
      top: 0;
      right: 0;
      width: 4px;
      height: 100%;
      border-right: 1px solid #ccc;
      cursor: e-resize;
      user-select: none;
    }
  }
}
</style>

