import http from "@/utils/http"

/**
 * 查询字段列表
 * @param content
 */
export const apiListFields = (page: number, limit: number) => {
  return http.get("/api/field/page", {
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
 * @param content
 */
export const apiParseFields = (param: FieldParseParam) => {
  return http.post("/api/field/parse", param)
}
