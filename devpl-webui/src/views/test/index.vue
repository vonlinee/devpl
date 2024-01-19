<template>
  
  <Child :count="count"></Child>

  <HelloWorld></HelloWorld>

</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import Child from "./Child.vue"

import { apiListTemplateTypes } from "@/api/template"
import HelloWorld from "@/components/treetable/HelloWorld.vue";

const count = ref()

onMounted(() => {
  apiListTemplateTypes().then((res) => {
    count.value = res.data?.length
  })
})
</script>
