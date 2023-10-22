import http from "@/utils/http"

/**
 * 查询数据类型列表
 * @param pageIndex 当前页
 * @param pageSize 每页大小
 * @param param
 */
export const apiListDataTypes = (pageIndex: Number, pageSize: Number, param: any = null) => {
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
  return http.postJson("/api/datatype/group/add", {
    groupId: group.typeGroupId,
    groupName: group.typeGroupName,
  })
}

/**
 * @returns
 */
export const apiUpdateDataTypeItem = (dataTypeItem: any) => {
  return http.postJson("/api/datatype/update", dataTypeItem)
}

/**
 * @returns
 */
export const apiSaveDataTypeItems = (dataTypeItem: any[]) => {
  return http.postJson("/api/datatype/saveOrUpdateBatch", {
    dataTypeItems: dataTypeItem,
  })
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiAddDataTypeMapping = (dataTypeItem: any[]) => {
  return http.postForm("/api/datatype/mapping", {
    dataTypeItems: dataTypeItem,
  })
}