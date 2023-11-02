import http from '@/utils/http'

interface GenTableVO {
	id: number,
	tableName: string,
	className: string,
	tableComment: string
}

export const useTableApi = (id: number) => {
	return http.get<GenTableVO>('/gen/table/' + id)
}

export const useTableSubmitApi = (dataForm: any) => {
	return http.put('/gen/table', dataForm)
}

export const useTableImportSubmitApi = (datasourceId: string, tableNameList: string) => {
	return http.post('/gen/table/import/' + datasourceId, tableNameList)
}

export const useTableFieldSubmitApi = (tableId: number, fieldList: any) => {
	return http.put('/gen/table/field/' + tableId, fieldList)
}

export const useTableSyncApi = (id: number) => {
	return http.post('/gen/table/sync/' + id)
}
