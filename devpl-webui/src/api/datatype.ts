import http from "@/utils/http"

/**
 * 查询数据类型列表
 * @param pageIndex 当前页
 * @param pageSize 每页大小
 * @param param
 */
export const apiListDataTypes = (
  pageIndex: Number,
  pageSize: Number,
  param: any = null
) => {
  return http.get("/api/datatype/page", {
    pageIndex: pageIndex,
    pageSize: pageSize,
  })
}

/**
 * @returns
 */
export const apiListAllDataTypeGroups = () => {
  return http.get("/api/datatype/groups")
}

/**
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

export interface DataTypeMappingParam {
  typeId: number
  anotherTypeId: number | undefined
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiAddDataTypeMapping = (dataTypeItem: DataTypeMappingParam[]) => {
  return http.post("/api/datatype/mapping", {
    mappings: dataTypeItem,
  })
}

/**
 * 所有可映射的数据类型
 * @param typeId 如果为空，则查询所有未设置过类型映射的数据类型
 * @returns
 */
export const apiListAllMappableDataTypes = (typeId: number | undefined = undefined) => {
  return http.get("/api/datatype/mappable", {
    typeId: typeId,
  })
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiListAllDataTypeMappings = (
  typeId: number | undefined  = undefined,
  typeGroupId: number | undefined = undefined
) => {
  return http.get("/api/datatype/mapping/all", {
    typeId: typeId,
    typeGroupId: typeGroupId,
  })
}
