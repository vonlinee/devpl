import http from '@/utils/http'

/**
 * 查询字段列表
 * @param content
 */
export const apiListFields = () => {
	return http.get('/api/field/page')
}

/**
 * 保存或更新字段
 * @param content
 */
export const apiSaveOrUpdateField = (field: any) => {
	if (!field.fieldName) {
		field.fieldName = field.fieldKey
	}
	return http.postJson('/api/field/save', field)
}
