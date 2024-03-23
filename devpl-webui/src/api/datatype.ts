import http from "@/utils/http"

/**
 * 查询数据类型列表
 * @param pageIndex 当前页
 * @param pageSize 每页大小
 * @param param
 */
export const apiListDataTypes = (
  pageIndex: number,
  pageSize: number,
  param: any = null
) => {
  return http.get("/api/datatype/page", {
    pageIndex: pageIndex,
    pageSize: pageSize,
    ...param,
  })
}

/**
 * @returns
 */
export const apiListAllDataTypeGroups = () => {
  return http.get<DataTypeGroup[]>("/api/datatype/groups")
}

/**
 * 添加类型分组
 * @returns
 */
export const apiSaveDataTypeGroup = (group: any) => {
  return http.post("/api/datatype/group/add", {
    groupId: group.typeGroupId,
    groupName: group.typeGroupName,
  })
}

/**
 * @returns
 */
export const apiUpdateDataTypeItem = (dataTypeItem: any) => {
  return http.post("/api/datatype/update", dataTypeItem)
}

/**
 * @returns
 */
export const apiSaveDataTypeItems = (dataTypeItem: any[]) => {
  return http.post("/api/datatype/saveOrUpdateBatch", {
    dataTypeItems: dataTypeItem,
  })
}

export interface DataTypeMappingAddParam {
  /**
   * 映射类型分组ID
   */
  groupId?: number
  typeId?: number
  anotherTypeIds?: number[]
  type?: DataTypeItem
  anotherTypes?: DataTypeItem[]
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiAddDataTypeMapping = (param: DataTypeMappingAddParam) => {
  return http.post("/api/datatype/mapping/add", param)
}

/**
 * 所有可映射的数据类型
 * @param typeId 如果为空，则查询所有未设置过类型映射的数据类型
 * @returns
 */
export const apiListAllMappableDataTypes = (
  typeId: number | undefined = undefined
) => {
  return http.get("/api/datatype/mappable", {
    typeId: typeId,
  })
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiListAllDataTypeMappings = (param: any) => {
  return http.get("/api/datatype/mapping/list", param)
}

/**
 * 保存或更新类型分组信息
 * @returns
 */
export const apiSaveOrUpdateDataTypeGroups = (groups: any[]) => {
  return http.post("/api/datatype/group/saveupdate/batch", groups)
}

/**
 * 保存或更新类型分组信息
 * @returns
 */
export const apiDeleteDataTypeGroupByIds = (groups: any[]) => {
  return http.delete("/api/datatype/group/remove", {
    groups: groups,
  })
}

/**
 * 数据类型选项
 * @param typeGroupId
 */
export const apiListDataTypeOptions = (typeGroupId: string) => {
  return http.get("/api/datatype/options", {
    typeGroupId: typeGroupId || "",
  })
}

/**
 * 数据类型组选择下拉列表
 */
export const apiListTypeGroupOptions = (excludeTypeGroupId?: string) => {
  return http.get("/api/datatype/group/options", {
    excludeTypeGroupId: excludeTypeGroupId,
  })
}

/**
 * 数据类型组选择下拉列表
 */
export const apiListTypeMappingGroupOptions = () => {
  return http.get("/api/datatype/mapping/group/options")
}

/**
 * 主类型列表
 */
export const apiListSelectablePrimaryTypeOptions = (
  param: DataTypeListParam
) => {
  return http.get("/api/datatype/mapping/primary/options", param)
}

/**
 * 数据类型组选择下拉列表
 */
export const apiListMappableAnotherTypeOptions = (param: DataTypeListParam) => {
  return http.get("/api/datatype/mapping/another/options", param)
}


/**
 * 数据类型组选择下拉列表
 */
export const apiRemoveDataMappingById = (id : number) => {
  return http.delete(`/api/datatype/mapping/remove?id=${id}`)
}

export type DataTypeListParam = {
  page?: number
  limit?: number
  typeId?: number
  typeGroupId?: string
  typeKey?: string
  typeName?: string
  excludeTypeGroupId?: string
  excludeIds?: number[]
}
