import http from "@/utils/request"

export const useDataSourceTestApi = (id: Number) => {
  return http.get<TestConnVO>("/api/gen/datasource/test/" + id)
}

export interface TestConnVO {
  /**
   * 是否失败
   */
  failed: boolean
  /**
   * 数据库类型
   */
  dbmsType: string
  /**
   * 是否使用ssl连接
   */
  useSsl: boolean
  /**
   * 连接失败时的错误信息
   */
  errorMsg: string
}

/**
 * 测试数据库连接
 * @param connInfo 连接信息
 * @returns
 */
export const apiTestConnection = (connInfo: any) => {
  return http.post<TestConnVO>("/api/gen/datasource/connection/test", connInfo)
}

export const useDataSourceApi = (id: Number) => {
  return http.get("/api/gen/datasource/" + id)
}

/**
 * 分页查询数据源列表
 * @returns 
 */
export const apiListDataSourcePage = () => {
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
  return http.post(
    "/api/jdbc/driver/upload",
    {
      file: file,
    },
  )
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

