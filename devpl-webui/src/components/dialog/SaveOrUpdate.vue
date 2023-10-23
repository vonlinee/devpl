<!--
  保存或更新
  header和footer固定
  z-index值比el-select的z-index值小即可，解决vxe-modal和element-plus组件混用元素被弹窗覆盖问题
-->
<template>
  <vxe-modal :height="height" :title="title" v-model:model-value="dialogVisiable" draggable :z-index="2000" show-footer>
    <template #default>
      <slot name="default" :formData="formData"></slot>
    </template>
    <template #footer>
      <div>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="onSubmit">确认</el-button>
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
   * 内容区域高度
   */
  height?: string;
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

const { title, formData, height, submit } = withDefaults(defineProps<PopupFormProps>(), {
  title: "标题",
  height: "400px"
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
