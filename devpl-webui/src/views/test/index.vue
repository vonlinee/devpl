<script setup lang="ts">

import FileSelect from './FileSelect.vue'
import {ref} from "vue";

let file = ref()

</script>

<template>

	<file-select ></file-select>

	<el-select filterable>

	</el-select>
</template>

<style lang="scss">

</style>
