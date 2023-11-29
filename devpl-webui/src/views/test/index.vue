<script setup lang="ts">

import CodeInput from "@/components/input/CodeInput.vue";
import { ref } from "vue";

const code = ref()

</script>

<template>
  <div style="width: 500px; height: 400px">
    <code-input lang="ts" v-model:code="code"></code-input>
  </div>

</template>

<style lang="scss">
.demo-form-inline .el-input {
	--el-input-width: 220px;
}
</style>
