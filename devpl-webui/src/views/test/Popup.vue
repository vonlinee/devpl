<!--
  https://github.com/accforgit/blog-data/blob/master/Popover/src/App.vue
  浮动面板组件 将锚点元素与popover进行隔离
-->
<template>
  <Teleport to="body">
    <div class="custom-popover">
      <div
        v-show="visible"
        :class="[
        'custom-popover-body',
        {'custom-popover-body-placeholder': !positionComputeDone}]"
        ref="bodyRef"
        :style="{
          top: top + 'px', left: left + 'px', zIndex: zIndex
        }"
        @mouseenter="handleMouseenter"
        @mouseleave="handleMouseleave">
        <div class="custom-popover-body-content">
          <slot></slot>
        </div>
        <span :class="[
          'custom-tooltip-body-arrow',
          { 'custom-tooltip-body-arrow-top': ['top', 'topLeft', 'topRight'].includes(position) },
          { 'custom-tooltip-body-arrow-bottom': ['bottom', 'bottomLeft', 'bottomRight'].includes(position) },
          { 'custom-tooltip-body-arrow-left': ['left', 'leftTop', 'leftBottom'].includes(position) },
          { 'custom-tooltip-body-arrow-right': ['right', 'rightTop', 'rightBottom'].includes(position) }
        ]" :style="{
          top: arrowTop,
          left: arrowLeft,
          bottom: arrowBottom,
          right: arrowRight
        }"></span>
      </div>
    </div>
  </Teleport>
</template>
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from "vue";

const emit = defineEmits<{
  (e: "visible-change", visible: boolean): void
}>();

type PositionType = "topLeft" | "top" | "topRight" | "leftTop" | "left" | "leftBottom" |
  "rightTop" | "right" | "rightBottom" | "bottomLeft" | "bottom" | "bottomRight"

type TriggerMode = "click" | "hover"

const { trigger, anchor, position } = withDefaults(defineProps<{
  position?: PositionType
  trigger?: TriggerMode
  anchor?: HTMLElement | (() => HTMLElement | undefined),
  getPopupContainer?: (triggerNode: HTMLElement) => Element,
  zIndex: number
}>(), {
  position: "top",
  trigger: "click"
});

const isParent = (child: HTMLElement | null, parent: HTMLElement): boolean => {
  if (child === null) return false;
  if (child === parent) return true;
  return isParent(child.parentElement, parent);
};

const gap = 12;
const gutter = 12;
const visible = ref(false);
// 弹层位置是否计算完毕
const positionComputeDone = ref(false);
const top = ref(0);
const left = ref(0);
const arrowTop = ref<string | undefined>();
const arrowLeft = ref<string | undefined>();
const arrowBottom = ref<string | undefined>();
const arrowRight = ref<string | undefined>();
// trigger模式为hover的情况下
// 在 referenceRef 与 bodyRef 之间来回移动，不应该改变visible
const debounceTimer = ref<number>(-1);
const referenceRef = ref<HTMLElement>();

const arrowPosition = ref({
  top: "",
  bottom: "",
  left: "",
  right: ""
});

/**
 * popup元素
 */
const bodyRef = ref<HTMLElement>();

/**
 * 获取锚点元素，用于计算popup出现的位置
 * @param event 如果不为空，则优先使用event触发元素作为锚点元素
 */
const getAnchorElement = (event?: Event) => {
  if (event) {
    return event.target as HTMLElement;
  }
  if (anchor instanceof Function) {
    return anchor();
  }
  return anchor;
};

/**
 * 计算popup出现的坐标
 * @param anchorRect 锚点元素的位置
 */
const computePosition = (anchorRect: DOMRect) => {
  if (!anchorRect || !bodyRef.value) return;
  const referenceRect = anchorRect;
  const bodyRect = bodyRef.value.getBoundingClientRect();
  // 相对于视口的距离
  const referencerectTop = referenceRect.top;
  const referencerectLeft = referenceRect.left;
  const bodyTop = referencerectTop - bodyRect.height;
  let _arrowLeft = 0;
  let _arrowTop = 0;
  const prefixTopTop = bodyTop - gap;
  const prefixBottomTop = referencerectTop + referenceRect.height + gap;
  arrowTop.value = undefined;
  arrowLeft.value = undefined;
  arrowBottom.value = undefined;
  arrowRight.value = undefined;
  switch (position) {
    case "topLeft":
      left.value = referencerectLeft;
      top.value = prefixTopTop;
      _arrowLeft = bodyRect.width / 2 + (referenceRect.width - bodyRect.width) / 2;
      if (_arrowLeft > bodyRect.width - gutter) {
        _arrowLeft = bodyRect.width - gutter;
      }
      arrowLeft.value = _arrowLeft + "px";
      break;
    case "top":
      left.value = (referencerectLeft + referenceRect.width / 2 - bodyRect.width / 2);
      top.value = prefixTopTop;
      arrowLeft.value = bodyRect.width / 2 + "px";
      break;
    case "topRight":
      left.value = referencerectLeft + (referenceRect.width - bodyRect.width);
      top.value = prefixTopTop;
      _arrowLeft = bodyRect.width - referenceRect.width + referenceRect.width / 2;
      if (_arrowLeft < gutter) {
        _arrowLeft = gutter;
      }
      arrowLeft.value = _arrowLeft + "px";
      break;
    case "bottomLeft":
      left.value = referencerectLeft;
      top.value = prefixBottomTop;
      _arrowLeft = bodyRect.width / 2 + (referenceRect.width - bodyRect.width) / 2;
      if (_arrowLeft > bodyRect.width - gutter) {
        _arrowLeft = bodyRect.width - gutter;
      }
      arrowLeft.value = _arrowLeft + "px";
      break;
    case "bottom":
      left.value = (referencerectLeft + referenceRect.width / 2 - bodyRect.width / 2);
      top.value = prefixBottomTop;
      arrowLeft.value = bodyRect.width / 2 + "px";
      break;
    case "bottomRight":
      left.value = referencerectLeft + (referenceRect.width - bodyRect.width);
      top.value = prefixBottomTop;
      _arrowLeft = bodyRect.width - referenceRect.width + referenceRect.width / 2;
      if (_arrowLeft < gutter) {
        _arrowLeft = gutter;
      }
      arrowLeft.value = _arrowLeft + "px";
      break;
    case "leftTop":
      left.value = referencerectLeft - bodyRect.width - gap;
      top.value = referencerectTop;
      arrowRight.value = 0 + "px";
      _arrowTop = referenceRect.height / 2;
      if (_arrowTop > bodyRect.height - gutter) {
        _arrowTop = bodyRect.height - gutter;
      }
      arrowTop.value = _arrowTop + "px";
      break;
    case "left":
      left.value = referencerectLeft - bodyRect.width - gap;
      top.value = referencerectTop + (referenceRect.height - bodyRect.height) / 2;
      arrowRight.value = 0 + "px";
      arrowTop.value = bodyRect.height / 2 + "px";
      break;
    case "leftBottom":
      left.value = referencerectLeft - bodyRect.width - gap;
      top.value = referencerectTop - (bodyRect.height - referenceRect.height);
      arrowRight.value = 0 + "px";
      _arrowTop = bodyRect.height - referenceRect.height + referenceRect.height / 2;
      _arrowTop = bodyRect.height - referenceRect.height + referenceRect.height / 2;
      if (_arrowTop < gutter) {
        _arrowTop = gutter;
      }
      arrowTop.value = _arrowTop + "px";
      break;
    case "rightTop":
      left.value = referencerectLeft + referenceRect.width + gap;
      top.value = referencerectTop;
      arrowLeft.value = 0 + "px";
      _arrowTop = referenceRect.height / 2;
      if (_arrowTop > bodyRect.height - gutter) {
        _arrowTop = bodyRect.height - gutter;
      }
      arrowTop.value = _arrowTop + "px";
      break;
    case "right":
      left.value = referencerectLeft + referenceRect.width + gap;
      top.value = referencerectTop + (referenceRect.height - bodyRect.height) / 2;
      arrowLeft.value = 0 + "px";
      arrowTop.value = bodyRect.height / 2 + "px";
      break;
    case "rightBottom":
      left.value = referencerectLeft + referenceRect.width + gap;
      top.value = referencerectTop - (bodyRect.height - referenceRect.height);
      arrowLeft.value = 0 + "px";
      _arrowTop = bodyRect.height - referenceRect.height + referenceRect.height / 2;
      if (_arrowTop < gutter) {
        _arrowTop = gutter;
      }
      arrowTop.value = _arrowTop + "px";
      break;
  }
  positionComputeDone.value = true;
};

const handleVisibleChange = (v: boolean, anchorEvent?: Event) => {
  visible.value = v;
  emit("visible-change", v);
  if (v) {
    setTimeout(() => {
      let anchorElement = getAnchorElement(anchorEvent);
      if (anchorElement) {
        computePosition(anchorElement.getBoundingClientRect());
      }
    });
  }
};
const handleMouseenter = () => {
  if (trigger !== "hover") return;
  clearTimeout(debounceTimer.value);
  visible.value = true;
};
const handleMouseleave = () => {
  if (trigger !== "hover") return;
  // 两个元素之间的 enter leave 一般会在 1ms 内完成
  debounceTimer.value = window.setTimeout(() => {
    visible.value = false;
  }, 250);
};


const listener = (e: any) => {
  if (isParent(e.target, referenceRef.value!)) {
    if (visible.value === false) {
      visible.value = !visible.value;
    }
  } else if (!isParent(e.target, bodyRef.value!)) {
    let anchorElement = getAnchorElement();
    if (anchorElement == undefined) {
      if (e.target != referenceRef.value) {
        visible.value = false;
      }
    }
  }
};

onMounted(() => {
  if (trigger === "click") {
    window.addEventListener("click", listener);
  }
});
onBeforeUnmount(() => {
  if (trigger === "click") {
    window.removeEventListener("click", listener);
  }
});

defineExpose({
  /**
   * 以指定元素触发
   * @param event
   */
  show(event: MouseEvent) {
    referenceRef.value = event.target as HTMLElement;
    handleVisibleChange(true, event);
  },
  /**
   * 隐藏popup
   */
  hide() {
    visible.value = false;
  }
});

</script>

<style scoped lang="scss">
.custom-popover-body {
  position: absolute;
  left: 0;
  top: 0;
  padding: 12px 16px;
  border-radius: 4px;
  color: #333;
  box-sizing: border-box;
  box-shadow: 0 4px 10px rgba(0, 0, 0, .1);
  border: 1px solid rgb(229, 230, 235);
  background-color: #fff;
}

.custom-popover-body.custom-popover-body-placeholder {
  opacity: 0;
}

.custom-popover-reference {
  display: inline-block;
}

.custom-popover-body-content {
  border-radius: 4px;
  height: auto;
  overflow: hidden;
}

$arrowSize: 8px;
$halfArrowSize: calc(-1 * $arrowSize / 2);
.custom-tooltip-body-arrow {
  content: "";
  position: absolute;
  border: 1px solid rgb(229, 230, 235);
  width: $arrowSize;
  height: $arrowSize;
  box-sizing: border-box;
  transform: rotate(45deg);
  background-color: #fff;

  &.custom-tooltip-body-arrow-top {
    bottom: $halfArrowSize;
    border-top: none;
    border-left: none;
    margin-left: $halfArrowSize;
  }

  &.custom-tooltip-body-arrow-bottom {
    top: $halfArrowSize;
    border-bottom: none;
    border-right: none;
    margin-left: $halfArrowSize;
  }

  &.custom-tooltip-body-arrow-left {
    border-bottom: none;
    border-left: none;
    margin-right: $halfArrowSize;
    margin-top: $halfArrowSize;
  }

  &.custom-tooltip-body-arrow-right {
    border-top: none;
    border-right: none;
    margin-left: $halfArrowSize;
    margin-top: $halfArrowSize;
  }
}
</style>