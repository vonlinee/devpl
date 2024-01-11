<template>
  <Child :count="count"></Child>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import Child from "./Child.vue"
import { apiListTemplateTypes } from "@/api/template"

const count = ref()

onMounted(() => {
  apiListTemplateTypes().then((res) => {
    count.value = res.data?.length
  })
})
</script>
