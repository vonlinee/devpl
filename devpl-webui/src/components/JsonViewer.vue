<template>
  <div class="box">
    <JsonViewer :value="jsonData" copyable boxed sort theme="light" />
  </div>
</template>

<script setup lang="ts">
import { JsonViewer } from "vue3-json-viewer"
// if you used v1.0.5 or latster ,you should add import "vue3-json-viewer/dist/index.css"
import "vue3-json-viewer/dist/index.css";
import { reactive, toRaw } from "vue";

const { data } = defineProps<{
  data: any
}>()

const jsonData = reactive(toRaw(data));

</script>

<style>
.box {
  margin-top: 1rem;
}
</style>