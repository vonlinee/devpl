<template>
  <div class="page">
    <SplitPane
      direction="column"
      :min="20"
      :max="80"
      :triggerLength="20"
      :paneLengthPercent.sync="paneLengthPercent"
    >
      <template v-slot:one>
        <div>
          区域一
        </div>
      </template>

      <template v-slot:two>
        <div>
          区域二
        </div>
      </template>

    </SplitPane>
  </div>
</template>

<script setup lang="ts">

import SplitPane from "@/components/split/SplitPane.vue";

const paneLengthPercent = 30;

</script>

<style scoped lang="scss">
.page {
  height: 600px;
  padding: 10px;
  background: #000;
}
</style>

