<script setup lang="ts">
import { ref } from 'vue'
import FileSelector from '@/components/FileSelector.vue'
import App1 from '@/views/test/App1.vue'
import App3 from '@/views/test/App3.vue'
import TestPopup from './TestPopup.vue'

let fileSelectorRef = ref()

</script>

<template>
	<app3></app3>

	<app1></app1>

	<file-selector ref="fileSelectorRef"></file-selector>
	<test-popup></test-popup>
</template>

<style lang="scss"></style>
