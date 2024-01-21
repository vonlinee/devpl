<template>
  <div id="app">
    <div id="container">
      <button @click="zipAll">全部折叠</button>
      <button @click="openAll">全部开</button>
      <button @click="highlight(true)">高亮行</button>
      <button @click="highlight(false)">取消高亮</button>
      <DraggableTreeTable ref="table" :data="treeData" @drag="onTreeDataChange" resize fixed :draggable="true">
        <template #selection="{ row }">
          {{ row.name }}
        </template>

        <template #id="{ row }">
          {{ row.id }}
        </template>

        <template #action="{ row }">
          <a class="action-item" @click.stop.prevent="add(row)">添加子节点</a>
          <a class="action-item" @click.stop.prevent="edit(row)">修改子节点</a>
          <a class="action-item" @click.stop.prevent="onDel(row)"><i>删除</i></a>
        </template>
      </DraggableTreeTable>
      <MyDialog :onSave="onEdit" ref="editDialog"></MyDialog>
      <MyDialog :onSave="onAdd" ref="addDialog"></MyDialog>
    </div>
  </div>
</template>

<script>
import MyDialog from "./MyDialog.vue";
import demoDataList from "./data";
import DraggableTreeTable from "./DraggableTreeTable.vue";

export default {
  name: "app",
  data() {
    return {
      treeData: {
        columns: [],
        lists: []
      }
    };
  },
  components: {
    MyDialog,
    DraggableTreeTable
  },
  methods: {
    onTreeDataChange(list) {
      // console.log(list);
      this.treeData.lists = list;
    },
    onAdd(pId, data) {
      this.$refs.table.addRow(pId, data);
    },
    onEdit(id, data) {
      this.$refs.table.editRow(id, data);
    },
    openAll() {
      this.$refs.table.expandAll();
    },
    zipAll() {
      this.$refs.table.foldAll(-1);
    },
    onDel(item) {
      // console.log("当前行的数据", updatedLists);
      this.treeData.lists = this.$refs.table.removeById(item.id);
      alert("本地删除成功");
    },
    highlight(flag) {
      this.$refs.table.highlightRow(383, flag, true);
    },
    add(row) {
      this.$refs.addDialog.show("add", row.id);
    },
    edit(row) {
      this.$refs.editDialog.show("edit", row);
    }
  },
  mounted() {
    let columns = [
      {
        /**
         * 列类型
         */
        type: "checkbox",
        hasChildren: true,
        width: 100,
        align: "center",
        onChange: (item) => {
          // console.log(item)
          alert("您选中了" + item.length + "条数据");
        }
      },
      {
        type: "selection",
        title: "<a>菜单名称</a>",
        field: "name",
        width: 200,
        align: "left",
        titleAlign: "left"
      },
      {
        title: "ID",
        type: "id",
        width: 100,
        align: "center"
      },
      {
        title: "链接",
        field: "uri",
        width: 200,
        align: "center"
      },
      {
        title: "操作(使用actions)",
        type: "action",
        flex: 1,
        align: "center",
        actions: [
          {
            text: "添加子节点",
            onclick: (item) => {
              this.$refs.addDialog.show("add", item.id);
            },
            formatter: item => {
              return "<i>添加子节点</i>";
            }
          },
          {
            text: "修改子节点",
            onclick: (item) => {
              this.$refs.editDialog.show("edit", item);
            },
            formatter: item => {
              // console.log(item);
              return "<i>修改子节点</i>";
            }
          },
          {
            text: "删除",
            onclick: this.onDel,
            formatter: item => {
              // console.log(item);
              return "<i>删除</i>";
            }
          }
        ]
      },
      {
        title: "操作(使用slot自定义)",
        type: "action",
        flex: 1,
        align: "center"
      }
    ];
    this.treeData = {
      columns: columns,
      lists: demoDataList
    };
  }
};
</script>

<style></style>
