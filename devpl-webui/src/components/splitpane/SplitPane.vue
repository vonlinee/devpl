<script lang="ts">
import { defineComponent, h, reactive, VNode } from "vue";
import { SetupContext, VNodeArrayChildren } from "@vue/runtime-core";

export default defineComponent({
  props: {
    orientation: {
      type: String
    }
  },
  setup(props, context: SetupContext) {

    const { slots } = context;

    const children: VNodeArrayChildren = [];

    const state = reactive({
      active: false,
      hasMoved: false,
      percent: 30,
      orientation: "vertical"
    });

    function onResizeStart() {
      console.log("移动开始");
      state.active = true;
      state.hasMoved = false;
    }

    if (slots.default) {
      const defaultSlot: VNode[] = slots.default();
      for (let i = 0; i < defaultSlot.length; i++) {
        children.push(defaultSlot[i]);

        if (i != defaultSlot.length - 1) {
          children.push(h("div", {
            class: "resizer",
            style: {
              width: "100%",
              height: "2px"
            },
            onMousedown: onResizeStart,
            onClick: () => {
              if (!state.hasMoved) {
                state.percent = 50;
              }
            }
          }, []));
        }
      }
    }

    const mouseUpHandle = () => {
      state.active = false;
    };

    const mouseMoveHandle = (e: MouseEvent) => {
      if (e.buttons === 0) {
        state.active = false;
      }
      if (state.active) {
        let offset = 0;
        let target: HTMLElement = e.currentTarget as HTMLElement;
        if (state.orientation === "vertical") {
          while (target) {
            offset += target.offsetLeft;
            target = target.offsetParent as HTMLElement;
          }
        } else {
          while (target) {
            offset += target.offsetTop;
            target = target.offsetParent as HTMLElement;
          }
        }

        const currentTarget = e.currentTarget as HTMLElement;

        const currentPage = state.orientation === "vertical" ? e.pageX : e.pageY;
        const targetOffset = state.orientation === "vertical" ? currentTarget.offsetWidth : currentTarget.offsetHeight;
        const percent = Math.floor(((currentPage - offset) / targetOffset) * 10000) / 100;

        state.hasMoved = true;
      }
    };

    return () => h("div", {
      className: "splitter-container clearfix",
      style: {
        backgroundColor: "red",
        width: "200px",
        height: "200px"
      },
      onMouseup: mouseUpHandle,
      onMousemove: mouseMoveHandle
    }, children);
  }
});
</script>

<style lang="scss" scoped>
.clearfix:after {
  visibility: hidden;
  display: block;
  font-size: 0;
  content: " ";
  clear: both;
  height: 0;
}

.splitter-container {
  height: 100%;
  position: relative;
}

.splitter-container-mask {
  z-index: 9999;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.resizer:hover {
  cursor: pointer;
}
</style>