<!-- 
  类型映射配置表
 -->
<template>
  <vxe-modal v-model="modalShowRef" title="类型映射表" width="60%">

    <vxe-form :data="formData" >
      <vxe-form-item title="类型组" field="name" :item-render="{}" span="12">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
      <vxe-form-item title="类型ID" field="name" :item-render="{}" span="12">
        <template #default="{ data }">
          <vxe-input v-model="data.name" placeholder="请输入名称" clearable></vxe-input>
        </template>
      </vxe-form-item>
    </vxe-form>

    <vxe-table :border="true" :data="tableData" :edit-config="{trigger: 'click', mode: 'cell'}">
      <vxe-column title="主类型" width="10%" align="center" field="typeName"></vxe-column>
      <vxe-column field="name" title="映射类型" align="center" :edit-render="{}">
        <template #default="{ row }">
          <span>{{ row.typeId }}</span>
        </template>
        <template #edit="{ row }">
          <vxe-select v-model="row.name" placeholder="配置式自定义模板" :options="opts3" multiple clearable transfer>
            <template #opt3="{ option }">
              <span style="color: red">
                <i class="vxe-icon-question-circle-fill"></i>
                <span>{{ option.label }}</span>
              </span>
            </template>
            <template #opt4="{ option }">
              <span style="color: green">
                <i class="vxe-icon-question-circle-fill"></i>
                <span>{{ option.label }}</span>
              </span>
            </template>
          </vxe-select>
        </template>
      </vxe-column>
    </vxe-table>
  </vxe-modal>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";

const modalShowRef = ref();

interface FormDataVO {
  name: string
  nickname: string
  sex: string
  age: number
  status: string
  date: string
  active: boolean
  flagList: string[]
}

const formData = reactive<FormDataVO>({
  name: '',
  nickname: '',
  sex: '',
  age: 30,
  status: '1',
  date: '',
  active: false,
  flagList: []
})

const opts3 = ref([
  { label: '1111', value: '1' },
  { label: '2222', value: '2' }
])

interface RowVO {
  typeId: number
  typeName: string,
  anotherTypeId: string | undefined,
}

const tableData = ref<RowVO[]>([
  {
    typeId: 1,
    typeName: "类型1",
    anotherTypeId: undefined
  }
])

defineExpose({
  show: () => modalShowRef.value = true
});


</script>
