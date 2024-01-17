<template>
    <el-select v-model="currentMsId" @change="handleSelectionChanged">
        <el-option v-for="msId in msIds" :label="msId" :value="msId" :key="msId"></el-option>
    </el-select>
</template>

<script lang="ts" setup>
import { apiBuildIndex } from '@/api/mybatis';
import { onMounted, ref } from 'vue';


const msIds = ref<string[]>([])
const currentMsId = ref<string>()

const emits = defineEmits([
    "changed"
])

const handleSelectionChanged = (newVal : string) => {
    emits("changed", newVal)
}

onMounted(() => {
    apiBuildIndex("111").then((res) => {
        msIds.value = res.data || []
    })
})

</script>