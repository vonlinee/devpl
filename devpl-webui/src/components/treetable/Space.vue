<template>
    <span class="space-container">
        <span class="space" v-for="(item, index) in spaces" v-bind:key="index"></span>
    </span>
</template>
<script lang="ts" setup>
import { computed } from 'vue';

const { depth } = defineProps<{
    depth: number
}>()

const spaces = computed(() => {
    const aArr = []
    for (let i = 0; i < depth; i++) {
        aArr.push('')
    }
    return aArr
})

</script>
<style>
.space {
    display: inline-block;
    width: 15px;
}
</style>
  