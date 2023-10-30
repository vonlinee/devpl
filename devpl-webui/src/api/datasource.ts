import http from "@/utils/http"

export const useDataSourceTestApi = (id: Number) => {
  return http.get("/api/gen/datasource/test/" + id)
}

/**
 * 测试数据库连接
 * @param connInfo 连接信息
 * @returns
 */
export const apiTestConnection = (connInfo: any) => {
  return http.post<string>("/api/gen/datasource/connection/test", connInfo)
}

export const useDataSourceApi = (id: Number) => {
  return http.get("/api/gen/datasource/" + id)
}

export const useDataSourceListApi = () => {
  return http.get("/api/gen/datasource/list")
}

export const useDataSourceSubmitApi = (dataForm: any) => {
  return http.post("/api/gen/datasource", dataForm)
}

export const useDataSourceTableListApi = (id: string) => {
  return http.get("/api/gen/datasource/table/list/" + id)
}

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNames = (dataForm: any) => {
  return http.post("/api/gen/datasource/dbnames", dataForm)
}

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNamesById = (dataSourceId: number) => {
  return http.get(`/api/gen/datasource/dbnames/${dataSourceId}`)
}

/**
 * 上传驱动jar包
 * @param file
 */
export const apiUploadDriverJar = (file: File) => {
  return http.post("/api/jdbc/driver/upload", {
    file: file,
  }, { "Content-Type": "multipart/form-data" })
}

/**
 * 所有支持的驱动类型
 * @returns 所有支持的数据库类型
 */
export const apiListSupportedDbTypes = () => {
  return http.get("/api/gen/datasource/drivers")
}

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListSelectableDataSources = () => {
  return http.get("/api/gen/datasource/list/selectable")
}

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListTableNames = (id: number, dbName: string) => {
  return http.get(`/api/gen/datasource/${id}/${dbName}/table/names`)
}
