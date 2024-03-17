<!--
  生成Java pojo类
-->
<template>
  <vxe-modal ref="modalRef" v-model="visible" title="其他字段操作" width="80%" height="90%" show-footer destroy-on-close
    @show="onShow">
    <Splitpanes>
      <Pane min-size="20" size="35">
        <FieldTree ref="fieldTableRef" selectable />
      </Pane>
      <Pane min-size="20">
        <el-select v-model="operationType">
          <el-option :value="1" label="Rest Template 请求路径"></el-option>
        </el-select>
        <CodeRegion ref="outputEditorRef" lang="java"></CodeRegion>
      </Pane>
    </Splitpanes>
    <template #footer>
      <el-button type="primary" @click="showInfo">测试</el-button>
      <el-button type="primary" @click="gen">生成</el-button>
    </template>
  </vxe-modal>
</template>

<script setup lang="ts">
import { apiListGroupFieldsById } from "@/api/fields"
import { reactive, ref, onMounted, onBeforeMount, nextTick } from "vue"
import CodeRegion from "@/components/CodeRegion.vue"
import { Pane, Splitpanes } from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import FieldTree from "@/components/fields/FieldTree.vue"

const operationType = ref(1)

const outputEditorRef = ref()
const visible = ref()
const fieldTableRef = ref()
const modalRef = ref()
const contentHeight = ref()

const fields = ref<FieldInfo[]>([])

const formData = reactive({
  type: 1,
  packageName: "io.devpl.test",
  className: "Test",
  useLombok: true,
  setterAndGetter: true
})

const onSelectionChange = (val: number) => {
  formData.type = val
  gen()
}

const gen = () => {
  const fields = fieldTableRef.value.getFields()

  if (operationType.value == 1) {
    let i = 1
    outputEditorRef.value.setText(fields.map((f: FieldInfo) => f.fieldKey + "={" + (i++) + "}").join('&'))
  }
}

defineExpose({
  show(groupId: number) {
    apiListGroupFieldsById(groupId).then((res) => {
      visible.value = true
      nextTick(() => fieldTableRef.value.setFields(res.data))
    })
  },
})

const showInfo = () => {
  console.log(contentHeight.value)
}

onBeforeMount(() => { })

const onShow = (param: any) => {
  contentHeight.value = param.$modal.getBox().clientHeight
}

onMounted(() => {
  const modalBox = modalRef.value.getBox() as HTMLDivElement
  console.log(modalBox.clientHeight)
})
</script>

<style lang="scss" scoped></style>
