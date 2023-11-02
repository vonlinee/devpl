<template>
  <vxe-modal :title="title" v-model="modalVisiable" draggable show-footer :z-index="2000" height="80%" width="80%">
    <template #default>
        <slot></slot>
    </template>
    <template #footer>
      <vxe-button status="success" @click="onOkButtonClicked">确定</vxe-button>
    </template>
  </vxe-modal>
</template>
<script setup lang='ts'>
import { ref } from 'vue';

// 弹窗是否可见
const modalVisiable = ref()

defineProps<{
  /**
   * 标题文本
   */
  title: string
}>()

const emits = defineEmits([
  "onOkButtonClicked"
])

const onOkButtonClicked = () => {
  emits('onOkButtonClicked')
}

defineExpose({
  show: () => {
    modalVisiable.value = true
  }
})

</script>
<style lang='scss' scoped>

</style>