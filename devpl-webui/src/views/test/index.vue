<template>
  <div class="page">
    <SplitPane
      direction="row"
      :min="20"
      :max="80"
    >
      <template v-slot:one>
        <div class="container">
          区域一
        </div>
      </template>

      <template v-slot:another>
        <div class="container">
          区域二
        </div>
      </template>

    </SplitPane>
  </div>
</template>

<script setup lang="ts">

import SplitPane from "@/components/split/SplitPane.vue";

</script>

<style scoped lang="scss">
.page {
  height: 600px;
  padding: 10px;

  .pane {
    border: 1px solid black;
  }

  .container {
    height: 100%;
    border: 1px solid black;
  }
}

</style>

