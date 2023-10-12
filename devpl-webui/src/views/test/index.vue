<script setup lang="ts">
import VxeDataTable from '@/components/datatable/VxeDataTable.vue'
import { VDTOptions } from '@/components/datatable/vxedatatable'
import { apiListDataTypes } from '@/api/datatype'

let options: VDTOptions = {
	columns: [{ type: 'checkbox', width: 50 },
	{ type: 'seq', width: 60 },
	{ field: 'typeKey', title: '类型Key', editRender: { autofocus: '.vxe-input--inner' }, slots: { edit: 'name_edit' } },
	{ field: 'typeName', title: '类型名称', editRender: {}, slots: { edit: 'nickname_edit' } },
	{ field: 'typeGroupId', title: '类型分组ID', editRender: {}, slots: { edit: 'role_edit' } },
	{ field: 'defaultValue', title: '默认值', showOverflow: true, editRender: {}, slots: { edit: 'address_edit' } }],

	queryPage: (page, limit) => {
		return apiListDataTypes(page, limit)
	}
}

</script>

<template>
	<vxe-data-table :options="options">
    <template #modal>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
      <div>1111</div>
    </template>
  </vxe-data-table>
</template>

<style lang="scss"></style>
