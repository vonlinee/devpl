import http from "@/utils/http"

/**
 * 查询字段列表
 * @param page
 * @param limit
 */
export const apiListFields = (page: number, limit: number) => {
  return http.get("/api/field/page", {
    pageIndex: page,
    pageSize: limit,
  })
}

/**
 * 保存或更新字段
 * @param field
 */
export const apiSaveOrUpdateField = (field: any) => {
  if (!field.fieldName) {
    field.fieldName = field.fieldKey
  }
  return http.post("/api/field/save", field)
}

interface FieldParseParam {
  /**
   * 输入类型
   */
  type: string
  /**
   * 待解析的文本
   */
  content: string
}

/**
 * 保存或更新字段
 * @param param
 */
export const apiParseFields = (param: FieldParseParam) => {
  return http.post("/api/field/parse", param)
}
