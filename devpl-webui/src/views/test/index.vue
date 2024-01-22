<template>

  <!--  <Child :count="count"></Child>-->

  <!--  <FieldTreeTableV1></FieldTreeTableV1>-->
  <!--  <HelloWorld></HelloWorld>-->

  <FieldTree :fields="fields"></FieldTree>

</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";

import { apiListTemplateTypes } from "@/api/template";
import HelloWorld from "@/components/treetable/HelloWorld.vue";
import FieldTreeTableV1 from "@/components/fields/FieldTreeTableV1.vue";
import FieldTree from "@/components/fields/FieldTree.vue";

const fields = ref<FieldInfo[]>([{
  fieldKey: "param",
  description: "描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息描述信息",
  children: [{
    fieldKey: "userName",
    description: "userAge"
  }]
}]);

const count = ref();

onMounted(() => {
  apiListTemplateTypes().then((res) => {
    count.value = res.data?.length;
  });
});
</script>
