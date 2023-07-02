<template>
    <div class="demo-collapse">
        <el-collapse v-model="activeName" accordion>
            <el-collapse-item v-for="(item, index) in items" :title="item.key">
                {{ index }}
            </el-collapse-item>
        </el-collapse>
        <ul>
            <li v-for="(value,key,index) in items">{{ value }}-{{ key }}-{{ index + 1 }}</li>
        </ul>
    </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'

let items = ref([
    {
        key: '111',
        value: '2222'
    }, {
        key: '2222',
        value: '2323'
    }
])

const activeName = ref('1')
</script>
