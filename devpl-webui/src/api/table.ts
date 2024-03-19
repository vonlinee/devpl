import http from "@/utils/http"
import { Keys } from "@/api/index"

/**
 * 查询生成表列表
 * @param id
 */
export const apiGetGenTableById = (id: number) => {
  return http.get<TableGeneration>("/api/filegen/table/" + id)
}

/**
 * 查询生成的表列表
 * @param page 
 * @param limit 
 * @param params 
 * @returns 
 */
export const apiListGenTables = (page: number, limit: number, params: any) => {
  return http.get<TableGeneration>("/api/filegen/table/page", {
    pageIndex: page,
    pageSize: limit,
    ...params,
  })
}

export const useTableSubmitApi = (dataForm: any) => {
  return http.put("/api/filegen/table", dataForm)
}

export const apiRemoveGenTableByIds = (ids: Keys) => {
  return http.delete("/api/filegen/table/remove", ids)
}

/**
 * 导入表
 * @param datasourceId
 * @param tableNameList
 * @param projectId
 * @returns 导入表的个数
 */
export const apiImportTables = (
  datasourceId: number,
  tables: TableImportInfo[],
  databaseName?: string,
  projectId?: number
) => {
  return http.post<number>("/api/filegen/table/import", {
    dataSourceId: datasourceId,
    databaseName: databaseName,
    tables: tables,
    projectId: projectId,
  })
}

export const apiUpdateGenTableFields = (tableId: number, fieldList: any) => {
  return http.put("/api/filegen/table/field/" + tableId, fieldList)
}

/**
 * 同步表的配置
 * @param id
 */
export const apiSyncTable = (id: number) => {
  return http.post("/api/filegen/table/sync/" + id)
}
