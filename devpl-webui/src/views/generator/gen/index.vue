<template>
    <div>
        <vxe-table border stripe height="400" :column-config="{ resizable: true }"
            :row-config="{ isHover: true }" :checkbox-config="{ labelField: 'id', highlight: true, range: true }"
            :data="tableData">
            <vxe-column field="file" title="文件名" sortable></vxe-column>
            <vxe-column field="sex" title="Sex" ></vxe-column>
            <vxe-column field="age" title="Age" sortable></vxe-column>
            <vxe-column field="address" title="Address" show-overflow></vxe-column>
        </vxe-table>
    </div>
</template>
  
<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { VxeColumnPropTypes } from 'vxe-table'

const tableData = ref<TargetGenFileItem[]>([])

</script>
  