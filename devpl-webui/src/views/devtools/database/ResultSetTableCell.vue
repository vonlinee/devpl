<template>
  <div>{{ columnName }}</div>
</template>
<script setup lang="ts">
const { columnName } = defineProps<{
  columnName: string
}>()
</script>
<style lang="scss" scoped></style>
