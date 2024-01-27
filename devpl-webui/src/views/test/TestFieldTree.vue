<template>
  <!--  <Child :count="count"></Child>-->

  <!--  <FieldTreeTableV1></FieldTreeTableV1>-->
  <!--  <HelloWorld></HelloWorld>-->
  <div style="width: 500px; height: 600px">
    <FieldTree :fields="fields" selectable></FieldTree>
  </div>

  <!-- <ForEach :items="fields">
    <template #default="scope">
      <li style="list-style-type: none; height: 50px;">
        {{ scope.item.fieldKey }}
      </li>
    </template>
  </ForEach> -->

  <!-- <List :items="myItems"></List> -->

</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";


const handleItemClicked = (item : any, event : Event) => {
  console.log("item clicked ", item, event);
}

import { apiListTemplateTypes } from "@/api/template";
import HelloWorld from "@/components/treetable/HelloWorld.vue";
import FieldTreeTableV1 from "@/components/fields/FieldTreeTableV1.vue";
import FieldTree from "@/components/fields/FieldTree.vue";
import ForEach from "@/components/ForEach.vue";

import List from "@/components/List.vue";

const myItems = [
  { id: 1, text: 'Item 1' },
  { id: 2, text: 'Item 2' },
  { id: 3, text: 'Item 3' }
]


const fields = ref<FieldInfo[]>([{
  id: 1,
  fieldKey: "param",
  dataType: "Object",
  description: "描述信息描述信息描述信息描述信息描述信息描述信息描述信",
  children: [{
    id: 10,
    parentId: 1,
    fieldKey: "userName",
    description: "描述信息",
    dataType: "String"
  }]
}, {
  id: 1,
  fieldKey: "param",
  dataType: "Object",
  description: "描述信息描述信息描述信息描述信息描述信息描述信息描述信",
  children: [{
    id: 10,
    parentId: 1,
    fieldKey: "userName",
    description: "描述信息",
    dataType: "String"
  }]
}]);

const count = ref();

onMounted(() => {
  apiListTemplateTypes().then((res) => {
    count.value = res.data?.length;
  });
});
</script>
