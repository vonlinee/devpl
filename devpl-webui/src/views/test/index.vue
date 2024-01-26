<template>

  <!--  <Child :count="count"></Child>-->

  <!--  <FieldTreeTableV1></FieldTreeTableV1>-->
  <!--  <HelloWorld></HelloWorld>-->

  <div style="width: 500px; height: 600px">
    <FieldTree :fields="fields" selectable></FieldTree>

  </div>

</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";

import { apiListTemplateTypes } from "@/api/template";
import HelloWorld from "@/components/treetable/HelloWorld.vue";
import FieldTreeTableV1 from "@/components/fields/FieldTreeTableV1.vue";
import FieldTree from "@/components/fields/FieldTree.vue";

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
}]);

const count = ref();

onMounted(() => {
  apiListTemplateTypes().then((res) => {
    count.value = res.data?.length;
  });
});
</script>
