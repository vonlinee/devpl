<script setup lang="ts">
import {ref} from 'vue'

const input = ref('')
const dialogInputText = ref('')
let editIconVisiableRef = ref(false)
const dialogShowing = ref(false)

</script>

<template>
	<vxe-input v-model="input" size="medium" type="text" @focus="editIconVisiableRef = true"
	           @blur="editIconVisiableRef = false">
		<template #suffix>
			<i class="vxe-icon-edit" @click="dialogShowing = true"></i>
		</template>
	</vxe-input>
	
	<vxe-modal v-model="dialogShowing" height="600" width="600" show-footer
	           @confirm="input = dialogInputText"
	           @show="dialogInputText = input">
		<template #title>
			<span>编辑</span>
		</template>
		<template #default>
			<vxe-textarea v-model="dialogInputText" rows="20" size="medium" resize="none" show-word-count></vxe-textarea>
		</template>
	</vxe-modal>
</template>

<style scoped lang="scss">

</style>