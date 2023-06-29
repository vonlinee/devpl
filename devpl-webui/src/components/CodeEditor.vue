<template>
	<codemirror
		v-model="code"
		placeholder="INSERT INTO `province_city_district` VALUES ('13', '0', '河北');"
		ref="cmRef"
		:style="{ width, height }"
		:autofocus="autofocus"
		:indent-with-tab="true"
		:tab-size="2"
		:extensions="extensions"
		@ready="handleReady"
	/>
</template>

<script>
// https://github.com/surmon-china/vue-codemirror
import {defineComponent, ref, shallowRef, defineExpose, nextTick} from 'vue'
import {Codemirror} from 'vue-codemirror'
import {oneDark} from '@codemirror/theme-one-dark'
import {sql} from '@codemirror/lang-sql'

import {getLanguageMode, importLanguage} from "@/components/useComdeMirror";

export default defineComponent({
	components: {
		Codemirror
	},
	props: {
		// 编辑器模式 https://codemirror.net/5/mode/
		mode: {
			type: String,
			required: false,
			default: 'text/plain'
		},
		// 自动聚焦
		autofocus: {
			type: Boolean,
			required: false,
			default: true
		},
		width: {
			default: '100%'
		},
		height: {
			default: '100%'
		}
	},
	setup(props) {
		const codeRef = ref(``)
		const cmRef = ref()
		
		const extensions = [oneDark]
		
		if (props.mode !== 'text/plain') {
			importLanguage(props.mode).then(res => {
				let languageModeFunction = res[props.mode]
				extensions.value = [sql(), oneDark]
			})
		}
		
		nextTick(() => {
			cmRef.value.mode = getLanguageMode(props.mode)
		})
		
		function setText(content) {
			codeRef.value = content
		}
		
		defineExpose({
			setText
		})
		// Codemirror EditorView instance ref
		const editorViewShallowRef = shallowRef()
		// 初始化Codemirror编辑器实例
		const handleReady = (payload) => {
			editorViewShallowRef.value = payload.view
		}
		return {
			code: codeRef,
			extensions,
			cmRef,
			handleReady
		}
	}
})
</script>
<style scoped lang="scss">
.cm-content {
	font-family: Consolas, serif;
}
</style>