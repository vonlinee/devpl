<script lang="ts">
import {defineComponent, ref} from 'vue'

export default defineComponent({
    name: 'TextField',
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
        }

        return {
            inputRef,
            updateValue,
            dialogInputText,
            editIconVisiableRef,
            dialogShowing,
            dialogInput
        }
    }
})

</script>

<template>
    <vxe-input v-model="inputRef" size="medium" type="text" @focus="editIconVisiableRef = true"
               @input="updateValue"
               @blur="editIconVisiableRef = false">
        <template #suffix>
            <i class="vxe-icon-edit" @click="dialogShowing = true"></i>
        </template>
    </vxe-input>

    <vxe-modal v-model="dialogShowing" height="600" width="600" show-footer
               @confirm="dialogInput"
               @show="dialogInputText = inputRef">
        <template #title>
            <span>编辑</span>
        </template>
        <template #default>
            <vxe-textarea v-model="dialogInputText" rows="20" size="medium" resize="none"
                          show-word-count></vxe-textarea>
        </template>
    </vxe-modal>
</template>

<style scoped lang="scss">

</style>
