<template>
  <div>
    <converter></converter>
    <el-card>
      <el-button>导入</el-button>
      <splitpanes>
        <pane>
          <el-table border :data="selectableFields" @selection-change="handleSelectionChange" :height="tableHeight">
            <el-table-column type="selection" width="35" align="center"></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column prop="fieldName" label="字段名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </pane>
        <pane>
          <el-table ref="fieldTable" border class="sortable-row-gen" row-key="id" :data="selectedFields"
                    highlight-current-row
                    :height="tableHeight"
                    row-class-name="field-row">
            <el-table-column type="selection" width="35" align="center"></el-table-column>
            <el-table-column prop="fieldKey" label="Key"></el-table-column>
            <el-table-column prop="fieldName" label="字段名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="dataType" label="数据类型"></el-table-column>
          </el-table>
        </pane>
      </splitpanes>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, nextTick, Ref } from "vue";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

import Sortable from "sortablejs";
import { apiListAllFields } from "@/api/fields";
import Converter from "@/views/devtools/toolset/SqlConverter.vue";

const selectableFields = ref<FieldInfo[]>([]);
const selectedFields = ref<FieldInfo[]>([]);
const fieldList = ref<FieldInfo[]>([]);
const fieldTable = ref();

const sortable = ref();
const tableHeight = "600px";

/**
 * el-table需要指定row-key，否则会导致拖拽顺序混乱
 */
const enableRowDragDrop = () => {
  nextTick(() => {
    // 获取需要拖拽的dom节点
    if (fieldTable.value) {
      const elTableBody = fieldTable.value.$refs.tableBody.querySelector("tbody");
      if (elTableBody) {
        sortable.value = Sortable.create(elTableBody, {
          disabled: false, // 是否开启拖拽
          ghostClass: "sortable-ghost", //拖拽样式
          animation: 150, // 拖拽延时，效果更好看
          handle: ".field-row",
          /**
           * 进行数据的处理，拖拽实际并不会改变绑定数据的顺序
           * @param e
           */
          onEnd: (e: any) => {
            // oldIndex为列表元素开始拖拽的索引，newIndex为列表元素结束拖拽的索引
            const { newIndex, oldIndex } = e;
            // 拖拽的行
            const curRow = fieldList.value.splice(oldIndex, 1)[0];
            // 将拖拽的行插入到拖拽结束的位置
            fieldList.value.splice(newIndex, 0, curRow);
            sortIndex(fieldList);
          }
        });
      }
    }
  });
};

const sortIndex = (tableData: Ref<any[]>) => {
  const array: any[] = [];
  tableData.value.forEach((e, i) => {
    const obj = {
      index: i + 1,
      ...e
    };
    array.push(obj);
  });
  tableData.value = array;
};

const handleSelectionChange = (val: FieldInfo[]) => {
  selectedFields.value = val;
};

onMounted(() => {
  apiListAllFields().then((res) => {
    selectableFields.value = res.data;
  });
  if (!sortable.value) {
    enableRowDragDrop();
  }
});
</script>

<style lang="scss" scoped>

</style>