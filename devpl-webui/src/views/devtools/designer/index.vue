<!--
  https://blog.csdn.net/qq_37130872/article/details/127067677
-->
<template>
  <div style="display: flex">
    <el-table>

    </el-table>
    <!-- 表格 -->
    <el-table
      :data="state.selectedFields"
      style="width: 100%"
      :key="state.dictTableKey"
      border
      :header-cell-style="{backgroundColor:'#E7F8FF',fontWeight: '500',color:'#000000'}"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column prop="fieldKey" label="Key" width="150" align="center" />
      <el-table-column prop="fieldName" label="名称" width="150" align="center" />
      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <el-button :icon="Delete" type="danger"
                     @click="deleteRow(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts" setup>
import { Delete } from "@element-plus/icons-vue";
import { ref, onMounted, nextTick, reactive, watch } from "vue";

const deleteRow = (row: any) => {

};

const modalVisible = ref();

import Sortable from "sortablejs";

const state = reactive<{
  dictTableKey?: number,
  selectedFields: any[]
}>({
  selectedFields: []
});

onMounted(() => {
  nextTick(() => {
    dragSort();
  });
});


//行拖拽
const dragSort = () => {
  let that = this;
  // 首先获取需要拖拽的dom节点
  const el1 = document.querySelectorAll(".el-table__body-wrapper")[0].querySelectorAll("table > tbody")[0] as HTMLElement;
  if (el1) {
    Sortable.create(el1, {
      disabled: false, // 是否开启拖拽
      ghostClass: "sortable-ghost", //拖拽样式
      animation: 150, // 拖拽延时，效果更好看
      group: { // 是否开启跨表拖拽
        name: "group_name",
        pull: false,
        put: false
      },
      onEnd: (event) => { //进行数据的处理，拖拽实际并不会改变绑定数据的顺序
        const { newIndex, oldIndex } = event;
        console.log(oldIndex, newIndex);
        const currRow = state.selectedFields?.splice(oldIndex, 1)[0];
        state.selectedFields?.splice(newIndex, 0, currRow);
        sortIndex();
      }
    });
  }
};

const sortIndex = () => {
  const array = [];
  state.selectedFields.forEach((e, i) => {
    let obj = {
      currency_id: e.currency_id,
      index: i + 1
    };
    array.push(obj);
    if (state.dictTableKey) {
      state.dictTableKey++;
    }
  });
};

watch(
  () => state.dictTableKey,
  () => {
    nextTick(() => {
      dragSort();
    });
  }
);

defineExpose({
  show: () => {
    modalVisible.value = true;
  }
});

</script>

<style lang="scss">

</style>