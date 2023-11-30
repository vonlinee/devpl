import http from '@/utils/http'

export const useBaseClassApi = (id: Number) => {
	return http.get('/gen/baseclass/' + id)
}

export const apiListBaseClass = () => {
	return http.get('/gen/baseclass/list')
}

export const useBaseClassSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return http.put('/gen/baseclass', dataForm)
	} else {
		return http.post('/gen/baseclass', dataForm)
	}
}
