import http from '@/utils/http'

export const useProjectApi = (id: number) => {
	return http.get('/gen/project/' + id)
}

export const useProjectSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return http.put('/gen/project', dataForm)
	} else {
		return http.post('/gen/project', dataForm)
	}
}

// 源码下载
export const useSourceDownloadApi = (id: string) => {
	location.href = import.meta.env.VITE_API_URL + '/gen/project/download/' + id
}
