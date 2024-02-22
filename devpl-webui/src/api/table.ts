import http from "@/utils/http"
import { Keys } from "@/api/index"

/**
 * 查询生成表列表
 * @param id
 */
export const apiGetGenTableById = (id: number) => {
  return http.get<TableGeneration>("/gen/table/" + id)
}

export const apiListGenTables = (page: number, limit: number, params: any) => {
  return http.get<TableGeneration>("/gen/table/page", {
    pageIndex: page,
    pageSize: limit,
    ...params,
  })
}

export const useTableSubmitApi = (dataForm: any) => {
  return http.put("/gen/table", dataForm)
}

export const apiRemoveGenTableByIds = (ids: Keys) => {
  return http.delete("/gen/table/remove", ids)
}

/**
 * 导入表
 * @param datasourceId
 * @param tableNameList
 * @param projectId
 */
export const apiImportTables = (
  datasourceId: number,
  tableNameList: string[],
  projectId?: number
) => {
  return http.post("/gen/table/import", {
    dataSourceId: datasourceId,
    tableNameList: tableNameList,
    projectId: projectId,
  })
}

export const apiUpdateGenTableFields = (tableId: number, fieldList: any) => {
  return http.put("/gen/table/field/" + tableId, fieldList)
}

/**
 * 同步表的配置
 * @param id
 */
export const apiSyncTable = (id: number) => {
  return http.post("/gen/table/sync/" + id)
}
