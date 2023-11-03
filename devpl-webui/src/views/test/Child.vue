<template>
	<div id="child">
		{{ childCount }}
	</div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';

interface ChildProps {
	count: number
}


const props = defineProps<ChildProps>()

console.log(props)

const childCount = computed(() => {
  return props.count
})


</script>
