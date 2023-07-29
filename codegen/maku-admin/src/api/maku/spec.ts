import service from '@/utils/request'

export const useSpecApi = (id: number) => {
return service.get('/maku/spec/' + id)
}

export const useSpecSubmitApi = (dataForm: any) => {
if (dataForm.id) {
return service.put('/maku/spec', dataForm)
} else {
return service.post('/maku/spec', dataForm)
}
}
