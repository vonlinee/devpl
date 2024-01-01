<template>
  <el-button @click="visible = true">Open</el-button>
  <vxe-modal v-model="visible" height="50%" @show="onShow">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" style="height: 100%;">
      <el-tab-pane label="User" name="first">
        <div ref="contentRef" style="background-color: rgb(241, 241, 241); height: 800px">
        </div>
      </el-tab-pane>
      <el-tab-pane label="Config" name="second">Config</el-tab-pane>
      <el-tab-pane label="Role" name="third">Role</el-tab-pane>
      <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
    </el-tabs>
  </vxe-modal>
</template>
<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import type { TabsPaneContext } from 'element-plus'

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
.el-tabs__content {
  height: calc(100% - 55px);
  overflow-y: auto;
}
</style>
