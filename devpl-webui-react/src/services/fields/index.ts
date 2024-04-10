import { request } from '@umijs/max';

/**
 * 查询字段列表
 * @param content
 */
export const apiListFields = (page: number, limit: number) => {
  return request("/api/field/page", {
    pageIndex: page,
    pageSize: limit,
  })
}

/**
 * 保存或更新字段
 * @param content
 */
export const apiSaveOrUpdateField = (field: any) => {
  if (!field.fieldName) {
    field.fieldName = field.fieldKey
  }
  return request("/api/field/save", field)
}


/**
 * 保存或更新字段
 * @param content
 */
export const apiParseFields = (param: FieldParseParam) => {
  return request("/api/field/parse", param)
}


export const useFieldTypeApi = (id: Number) => {
	return request('/gen/fieldtype/' + id)
}

export const useFieldTypeListApi = () => {
	return request('/gen/fieldtype/list')
}

export const useFieldTypeSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return request('/gen/fieldtype', dataForm)
	} else {
		return request('/gen/fieldtype', dataForm)
	}
}
