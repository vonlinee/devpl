import http from "@/utils/http";

/**
 * 查询字段列表
 * @param page
 * @param limit
 * @param params
 */
export const apiListFields = (page: number, limit: number, params: any) => {
  return http.get("/api/field/page", {
    pageIndex: page,
    pageSize: limit,
    ...params
  });
};

/**
 * 保存或更新字段
 * @param field
 */
export const apiSaveOrUpdateField = (field: any) => {
  if (!field.fieldName) {
    field.fieldName = field.fieldKey;
  }
  return http.post("/api/field/save", field);
};


/**
 * 保存或更新字段
 */
export const apiDeleteFieldByIds = (ids: number[]) => {
  return http.delete<boolean>("/api/field/delete", ids);
};

interface FieldParseParam {
  /**
   * 输入类型
   */
  type: string;
  /**
   * 待解析的文本
   */
  content: string;
}

/**
 * 保存或更新字段
 * @param param
 */
export const apiParseFields = (param: FieldParseParam) => {
  return http.post<FieldInfo[]>("/api/field/parse", param);
};
