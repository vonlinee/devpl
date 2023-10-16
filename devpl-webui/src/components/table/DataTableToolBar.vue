<template>
  <div>
    <el-button-group>
      <el-button-group class="ml-4">
        <el-button :icon="Refresh" @click="" />
        <el-button :icon="Plus" />
      </el-button-group>
    </el-button-group>
  </div>
</template>
<script  lang='ts'>
import { defineComponent } from 'vue';

import { Refresh, Plus } from '@element-plus/icons-vue'
import { VxeTableDataRow } from 'vxe-table';

export interface DataTableToolBarProps {
  refresh: (row: VxeTableDataRow) => void
}

export default defineComponent<DataTableToolBarProps>({
  setup(props, ctx) {
    const refreshCallback = ctx.attrs.refresh

    const onRefreshClick = (event: MouseEvent) => {
      refreshCallback(null)
    }

    return {
      Refresh,
      Plus,
      refreshCallback
    }
  },
})

</script>
<style lang='scss' scoped></style>