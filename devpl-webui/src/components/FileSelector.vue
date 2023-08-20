<script lang="ts">

import {defineComponent, ref} from "vue";

/**
 * https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/Input/file
 * https://developer.mozilla.org/zh-CN/docs/Web/API/File
 */

/**
 * 下拉列表选项
 */
interface FileItem {
    id: number,
    name: string
    size?: number // 文件大小
    type?: string, // 文件类型
}

export default defineComponent({
    props: {
        accept: {
            type: String,
            default: "",
            required: false
        },
        // 是否可多选文件
        multiple: {
            type: Boolean,
            default: true,
            required: false
        },
        limit: {
            type: Number,
            default: 0, // 0 表示不限制
            required: false
        }
    },
    setup(props, context) {
        const inputRef = ref()

        /**
         * 打开文件选择弹窗
         */
        async function showFileChooserDialog() {
            inputRef.value.click()
        }

        const options = ref<FileItem[]>([])
        const selectedFiles = ref<File[]>([])
        const selectedFileItem = ref(' ')

        context.expose({
            getFiles() {
                return selectedFiles.value
            }
        })

        /**
         * 文件选择事件
         */
        function onFileListChange(event: Event) {
            const files: File[] = inputRef.value.files
            if (files && files.length > 0) {
                for (let file of files) {
                    // TODO 文件去重
                    selectedFiles.value?.push(file)
                    // 在Web浏览器中，由于安全原因，在本地文件系统中获取文件的真实路径是不可行的。浏览器只会提供临时的虚拟路径，这是出于安全考虑的一种方式。
                    const newFileItem = {
                        id: options.value.length + 1,
                        name: file.name,
                        size: file.size,
                        type: file.type
                    }
                    options.value.push(newFileItem)
                }
                selectedFileItem.value = files[0].name
            }
        }

        return {
            selectedFileItem,
            options,
            inputRef,
            onFileListChange,
            showFileChooserDialog
        }
    }
})

</script>

<template>
    <el-select v-model="selectedFileItem">
        <template #prefix>
            <span @click.stop="showFileChooserDialog">浏览</span>
        </template>
        <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span :title="item.type" style="float: left">{{ item.name }}</span>
            <span style=" float: right; color: var(--el-text-color-secondary); font-size: 13px;">{{ item.size }}</span>
        </el-option>
    </el-select>
    <input ref="inputRef" type="file" style="display: none" @change="onFileListChange($event)" :accept="accept"
           :multiple="multiple"/>
</template>

<style scoped lang="scss">

</style>
