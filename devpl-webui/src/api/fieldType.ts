import http from '@/utils/http'

export const useFieldTypeApi = (id: Number) => {
	return http.get('/gen/fieldtype/' + id)
}

export const useFieldTypeListApi = () => {
	return http.get('/gen/fieldtype/list')
}

export const useFieldTypeSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return http.put('/gen/fieldtype', dataForm)
	} else {
		return http.post('/gen/fieldtype', dataForm)
	}
}
