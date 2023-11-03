<template>
	<div>{{ count }}</div>
	<button @click="count = count + 1">count ++</button>

	<Child :count="count"></Child>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Child from './Child.vue';

const count = ref(0)

</script>
