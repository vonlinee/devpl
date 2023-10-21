<!--
  弹窗式表单组件
  z-index值比el-select的z-index值小即可，解决
-->
<template>
  <vxe-modal :title="title" v-model:model-value="dialogVisiable" draggable :z-index="2000" show-footer>
    <template #default>
      <slot name="default">
        <div>
          11111
        </div>
      </slot>
    </template>
    <template #footer>
      <div>
        <el-button @click="cancel">取消</el-button>
        <el-button @click="onSubmit">确认</el-button>
      </div>
    </template>
  </vxe-modal>
</template>

<script lang="ts" setup>
import { ref } from "vue";

interface PopupFormProps {
  /**
   * 标题
   */
  title?: string;
  /**
   * 表单对象
   */
  formData: any;
  /**
   * 表单提交事件
   * @param formData
   * @param setVisiable 改变弹窗状态
   */
  submit?: (formData: any, setVisiable: (visiable: boolean) => void) => void;
}

const { title, formData, submit } = withDefaults(defineProps<PopupFormProps>(), {
  title: "标题"
});

const dialogVisiable = ref(false);


/**
 * 事件定义
 */
const emit = defineEmits([
  "submit",
  "cancel"
]);


const cancel = () => {
  emit("cancel", formData, (visiable: boolean) => {
    dialogVisiable.value = visiable;
  });
};

const onSubmit = () => {
  emit("submit", formData, (visiable: boolean) => {
    dialogVisiable.value = visiable;
  });
};

defineExpose({
  show: () => {
    dialogVisiable.value = true;
  }
});
</script>

<style scoped lang="scss">
</style>
