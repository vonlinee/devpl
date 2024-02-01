<!-- 
  全局唯一异常弹窗
 -->
<template>
  <dialog id="globalExceptionDialog">
    <div>
        <button value="cancel">Cancel</button>
        <button id="confirmBtn" value="default">Confirm</button>
      </div>
  </dialog>
</template>

<script setup lang="ts">

import { ref, onMounted } from "vue";

const dialogRef = ref()

onMounted(() => {
  dialogRef.value = document.getElementById("globalExceptionDialog")
})

</script>











