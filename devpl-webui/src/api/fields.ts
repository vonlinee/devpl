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
 * 查询字段列表，不分页
 * @param params
 */
export const apiListAllFields = (params?: any) => {
  return http.get("/api/field/list", params || {});
};

/**
 * 批量保存字段信息
 * @param fields
 */
export const apiSaveBatchFields = (fields: FieldInfo[]) => {
  return http.post("/api/field/save/batch", fields);
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

export type FieldParseParam = {
  /**
   * 输入类型
   */
  type: string;
  /**
   * 待解析的文本
   */
  content: string;
  /**
   * 其他选项
   */
  options?: Record<string, any>;
}

export type FieldParseResult = {
  /**
   * 是否失败
   */
  failed: boolean,
  /**
   * 解析错误信息
   */
  errorMsg: string,
  /**
   * 解析字段信息
   */
  fields: FieldInfo[]
}

/**
 * 解析字段
 * @param param
 */
export const apiParseFields = (param: FieldParseParam) => {
  return http.post<FieldParseResult>("/api/field/parse", param);
};

/**
 * 字段组
 */
type FiledGroup = {
  groupName: string
}

/**
 * 查询字段组列表
 * @param pageIndex
 * @param pageSize
 * @param param
 */
export const apiPageFieldGroup = (
  pageIndex?: number,
  pageSize?: number,
  param?: any
) => {
  return http.get("/api/field/group/page", {
    pageIndex: pageIndex,
    pageSize: pageSize,
    ...param
  });
};

/**
 * 查询字段组的字段列表
 * @param groupId 字段组ID
 */
export const apiListGroupFieldsById = (groupId: number) => {
  return http.get("/api/field/group/field-list", {
    groupId: groupId
  });
};

/**
 * 添加新的字段组
 */
export const apiNewFieldGroup = (fields?: FieldInfo[]) => {
  return http.post("/api/field/group/new", {
    fields: fields
  });
};

/**
 * 查询字段组的字段列表
 */
export const apiUpdateFieldGroup = (
  group: {
    id: number
    groupName: string
  },
  fields: FieldInfo[]
) => {
  return http.post("/api/field/group", {
    group: group,
    fields: fields
  });
};

/**
 * 查询字段组的字段列表
 * @param groupId 字段组ID
 */
export const apiDeleteFieldGroup = (groupId: number) => {
  return http.delete(`/api/field/group?id=${groupId}`);
};

/**
 * 查询字段组的数据类型名称列表
 */
export const apiListFieldDataTypeNames = () => {
  return http.get<string[]>("/api/field/datatype/names");
};