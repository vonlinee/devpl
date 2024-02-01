<!-- 
  默认展示footer插槽
 -->
<template>
  <vxe-modal ref="vxeModalRef" v-model="visible" :height="height" :width="width" show-footer :z-index="zIndex"
    @show="onShown">
    <template #default>
      <slot name="default" :size="modalConentSize"></slot>
    </template>

    <template #footer>
      <slot name="footer">
        <vxe-button>取消</vxe-button>
        <vxe-button status="primary">确定</vxe-button>
      </slot>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const visible = ref()
const vxeModalRef = ref()
const modalConentSize = ref({
  width: 0,
  height: 0
})

const { zIndex } = withDefaults(defineProps<{
  height?: number | string,
  width?: number | string,
  zIndex?: number
}>(), {
  zIndex: 2000,
  height: "80%",
  width: "80%"
})

defineExpose({
  show() {
    visible.value = true
  }
})

/**
 * 在窗口显示时会触发该事件
 */
const onShown = (param: { type: string }) => {

  // 获取当前窗口元素
  const vxeModalBox: HTMLDivElement = vxeModalRef.value.getBox() as HTMLDivElement

  // 内容区域的宽度和高度
  // const header = vxeModalBox.getElementsByClassName("vxe-modal--header")[0]
  const content : HTMLDivElement = vxeModalBox.getElementsByClassName("vxe-modal--content")[0] as HTMLDivElement
  // const footer = vxeModalBox.getElementsByClassName("vxe-modal--footer")[0]

  modalConentSize.value = {
    width: vxeModalBox.clientWidth,
    height: content.offsetHeight
  }
}

onMounted(() => {

  if (vxeModalRef.value) {


  }

})


</script>