<template>
  <el-button @click="importFieldGroupModal.show()">导入</el-button>

  <FieldGroupImport ref="importFieldGroupModal"></FieldGroupImport>
</template>

<script lang="ts" setup>
import FieldGroupImport from "./FieldGroupImport.vue";
import { ref } from "vue";

const importFieldGroupModal = ref();

</script>

<style scoped>

</style>