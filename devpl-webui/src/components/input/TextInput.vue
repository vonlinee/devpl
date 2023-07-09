<script lang="ts">
import {defineComponent, ref} from 'vue'
import {ElButton, ElDialog} from "element-plus";
import SvgIcon from "@/components/svg-icon/src/svg-icon.vue";

export default defineComponent({
    name: 'TextInput',
    components: {ElButton, SvgIcon, ElDialog},
    props: {
        text: {
            type: String,
            required: false
        }
    },
    emits: ["update:text"],
    setup(props, context) {
        const inputRef = ref(``)
        const dialogInputText = ref(``)
        const editIconVisiableRef = ref(false)
        const dialogShowing = ref(false)

        const updateValue = (e: KeyboardEvent) => {
            context.emit('update:text', inputRef.value)
        }

        /**
         * 弹窗输入文本
         */
        function dialogInput() {
            inputRef.value = dialogInputText.value
            context.emit('update:text', inputRef.value)
            dialogShowing.value = false
        }

        function showTextInputDialog() {
            dialogInputText.value = inputRef.value;
            dialogShowing.value = true;
        }

        return {
            inputRef,
            updateValue,
            dialogInputText,
            editIconVisiableRef,
            dialogShowing,
            dialogInput,
            showTextInputDialog
        }
    }
})

</script>

<template>
    <el-input title="" v-model="inputRef" size="large" type="text" @mouseenter="editIconVisiableRef = true"
              @input="updateValue" @mouseleave="editIconVisiableRef = false">
        <template #suffix>
            <svg-icon v-show="editIconVisiableRef" icon="icon-expand" @click.stop="showTextInputDialog"
                      cursor="pointer"></svg-icon>
        </template>
    </el-input>

    <el-dialog v-model="dialogShowing" height="600" width="600" show-footer
               @confirm="dialogInput"
               @show="dialogInputText = inputRef">
        <template #title>
            <span>编辑</span>
        </template>
        <template #default>
            <slot name="editor">
                <el-input v-model="dialogInputText" type="textarea" rows="20" size="small" resize="none"
                          show-word-count>
                </el-input>
            </slot>
        </template>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogShowing = false">取消</el-button>
            <el-button type="primary" @click="dialogInput">确认</el-button>
          </span>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>
