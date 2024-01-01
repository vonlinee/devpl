<template>
  <el-button @click="visible = true">Open</el-button>
  <vxe-modal v-model="visible" height="80%" @show="onShow">
    <div style="height: 100%;" ref="tabRef">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" style="height: 100%;">
        <el-tab-pane label="User" name="first">
            <div ref="contentRef" style="background-color: red; height: 100%;">
            </div>
        </el-tab-pane>
        <el-tab-pane label="Config" name="second">Config</el-tab-pane>
        <el-tab-pane label="Role" name="third">Role</el-tab-pane>
        <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
      </el-tabs>
    </div>
  </vxe-modal>
</template>
<script lang="ts" setup>
import { ref, onMounted, nextTick } from 'vue'
import type { ElScrollbar, TabsPaneContext } from 'element-plus'

const visible = ref()
const tabRef = ref()
const contentRef = ref()
const activeName = ref('first')

const onShow = () => {
  contentRef.value.style.height = tabRef.value.clientHeight - 40
}

onMounted(() => {

})

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
</script>
<style lang = "scss" >
.demo-tabs :deep(.el-tabs__content) {
  height: 100%;
  flex-grow: 1;
}

:deep(.el-tab-pane) {
  height: 100%;
}

.demo-tabs {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.container {
  height: 600px;
}
</style>
