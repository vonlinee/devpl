<!--
 * https://developer.mozilla.org/en-US/docs/Web/API/File
 * https://www.cnblogs.com/jiangweiping/p/14295286.html
 * @author Von
 * @date 2023/8/19 15:23
-->
<script lang="ts">

import {defineComponent, h} from "vue";

interface Item {
	selected: boolean,
	value: string,
	innerHTML: string
}

let aOptions: Item[] = []

export default defineComponent({
	name: 'FileSelect',
	setup(props, context) {

		return () => h('div', {
			class: 'select_container'
		}, [h('div', {class: 'input_container'}, h('input', {placeholder: '请选择'}, [])),
			h('select', {
				id: 'file_chooser_select'
			}, [h('option', {value: 1, text: '北京'}), h('option', {value: 2, text: '上海'})])
		])
	}
})
</script>

<style scoped lang="scss">
.select_container {

}

#file_chooser_select {
	display: inline-block;
	width: 300px;
	position: relative;
	vertical-align: middle;
	padding: 0;
	overflow: hidden;
	background-color: #fff;
	color: #555;
	border: 1px solid #aaa;
	text-shadow: none;
	border-radius: 4px;
	transition: box-shadow 0.25s ease;
	z-index: 2;
}

.select:hover {
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.15);
}

.select:before {
	content: "";
	position: absolute;
	width: 0;
	height: 0;
	border: 10px solid transparent;
	border-top-color: #ccc;
	top: 14px;
	right: 10px;
	cursor: pointer;
	z-index: -2;
}

.select select {
	cursor: pointer;
	padding: 10px;
	width: 100%;
	border: none;
	background: transparent none;
	-webkit-appearance: none;
	-moz-appearance: none;
}

.select select:focus {
	outline: none;
}

/* 去掉 select 元素的默认箭头 */
select {
	border-right: 200px #1e97f8;
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none;
	/* IE11 */
	-ms-appearance: none;

	outline: none;
	border: none;
	width: 38px;
}

/* 去掉 IE11 的默认箭头 */
select::-ms-expand {
	display: none;
}
</style>
