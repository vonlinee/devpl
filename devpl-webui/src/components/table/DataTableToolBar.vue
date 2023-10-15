<template>
  <vxe-toolbar ref="toolbarRef" custom>
    <template #buttons>
      <vxe-button icon="vxe-icon-refresh"></vxe-button>
    </template>
  </vxe-toolbar>
</template>
<script  lang='ts'>
import { defineComponent } from 'vue';
import { VXETable } from 'vxe-table';

export default defineComponent({
  setup(props, ctx) {
    const funnelEvent = () => {
      VXETable.modal.alert({ content: '点击事件' })
    }

    return {
      funnelEvent
    }
  },
})

</script>
<style lang='' scoped>

</style>