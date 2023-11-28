<!-- 
  封装一个简单的弹窗，固定的确定和取消按钮
  通过ref进行操作
 -->
<template>
  <vxe-modal :title="title" v-model="modalVisiable" :draggable="_draggable" show-footer :z-index="2000" height="80%"
    width="80%" :show-close="_showClose">
    <template #default>
      <slot></slot>
    </template>
    <template #footer>
      <vxe-button @click="onCancelButtonClicked">取消</vxe-button>
      <vxe-button status="primary" @click="onOkButtonClicked">确定</vxe-button>
    </template>
  </vxe-modal>
</template>
<script setup lang='ts'>
import { computed, ref } from 'vue';

// 弹窗是否可见
const modalVisiable = ref()

const { showClose, draggable } = defineProps<{
  /**
   * 标题文本
   */
  title: string,
  /**
   * 是否展示右上角关闭按钮
   */
  showClose?: boolean,
  /**
   * 是否可拖动
   */
  draggable?: boolean
}>()

const _showClose = computed(() => {
  return showClose === undefined || showClose === null ? false : showClose
})

const _draggable = computed(() => {
  return draggable === undefined || draggable === null ? false : draggable
})

const emits = defineEmits([
  "onOkButtonClicked",
  "onCancelButtonClicked"
])

const onOkButtonClicked = () => {
  emits('onOkButtonClicked')
}

const onCancelButtonClicked = () => {
  modalVisiable.value = false
  emits('onCancelButtonClicked')
}

defineExpose({
  show: (params: any) => {
    modalVisiable.value = true
  }
})

</script>
<style lang='scss' scoped></style>