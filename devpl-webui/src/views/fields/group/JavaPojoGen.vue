<!--
  生成Java pojo类
-->
<template>
  <vxe-modal
    ref="modalRef"
    v-model="visible"
    title="生成Java Pojo类"
    width="80%"
    height="90%"
    show-footer
    destroy-on-close
    @show="onShow"
  >
    <Splitpanes>
      <Pane min-size="20" size="35">
        <FieldTable ref="fieldTableRef"></FieldTable>
      </Pane>
      <Pane>
        <div>
          <el-form :form="formData" label-position="top">
            <el-form-item>
              <el-select v-model="formData.type">
                <el-option label="Java VO类型" :value="1"></el-option>
                <el-option label="Jackson响应类型" :value="2"></el-option>
                <el-option label="EasyPOI类型" :value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="包名">
              <el-input v-model="formData.packageName"></el-input>
            </el-form-item>
            <el-form-item label="类名">
              <el-input v-model="formData.className"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </Pane>
      <Pane min-size="20" size="45">
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
import { toRaw, reactive, ref, onMounted, onBeforeMount, nextTick } from "vue"
import FieldTree from "@/components/fields/FieldTree.vue"
import { apiCodeGenJavaPojo } from "@/api/generator"
import CodeRegion from "@/components/CodeRegion.vue"
import { Pane, Splitpanes } from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import FieldTable from "@/components/fields/FieldTable.vue"

const tableData = ref()
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
})

const gen = () => {
  apiCodeGenJavaPojo({
    ...toRaw(formData),
    fields: fieldTableRef.value.getFields(),
  }).then((res) => {
    outputEditorRef.value.setText(res.data)
  })
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

onBeforeMount(() => {})

const onShow = (param: any) => {
  contentHeight.value = param.$modal.getBox().clientHeight
}

onMounted(() => {
  const modalBox = modalRef.value.getBox() as HTMLDivElement
  console.log(modalBox.clientHeight)
})
</script>

<style lang="scss" scoped></style>
