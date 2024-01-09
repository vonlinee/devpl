<template>
  <div id="child">
    {{ count }}
  </div>
</template>

<script lang="ts" setup>
import { computed } from "vue"

interface ChildProps {
  count: number
}

const props = defineProps<ChildProps>()
console.log(props.count) // undefined
</script>
