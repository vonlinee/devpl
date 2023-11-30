import http from "@/utils/http";

export const useDataSourceTestApi = (id: number) => {
  return http.get<TestConnVO>("/api/gen/datasource/test/" + id);
};

export interface TestConnVO {
  /**
   * 是否失败
   */
  failed: boolean;
  /**
   * 数据库类型
   */
  dbmsType: string;
  /**
   * 是否使用ssl连接
   */
  useSsl: boolean;
  /**
   * 连接失败时的错误信息
   */
  errorMsg: string;
}

/**
 * 测试数据库连接
 * @param connInfo 连接信息
 * @returns
 */
export const apiTestConnection = (connInfo: DbConnInfo) => {
  return http.post<TestConnVO>("/api/gen/datasource/connection/test", connInfo);
};

export const useDataSourceApi = (id: Number) => {
  return http.get("/api/gen/datasource/" + id);
};

export const useDataSourceListApi = () => {
  return http.get("/api/gen/datasource/list");
};

export const useDataSourceSubmitApi = (dataForm: any) => {
  return http.post("/api/gen/datasource", dataForm);
};

/**
 * 获取数据源的所有表信息
 * @param id
 * @param databaseName
 * @param tableNamePattern
 * @returns
 */
export const useDataSourceTableListApi = (
  id: number,
  databaseName?: string,
  tableNamePattern?: string | null
) => {
  return http.get(
    "/api/gen/datasource/table/list", {
      dataSourceId: id,
      databaseName: databaseName,
      tableNamePattern: tableNamePattern
    }
  );
};

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNames = (dataForm: any) => {
  return http.post("/api/gen/datasource/dbnames", dataForm);
};

/**
 * 获取所有数据库名称
 * @param dataSourceId
 */
export const apiGetDatabaseNamesById = (dataSourceId: number) => {
  return http.get(`/api/gen/datasource/dbnames/${dataSourceId}`);
};

/**
 * 上传驱动jar包
 * @param file
 */
export const apiUploadDriverJar = (file: File) => {
  return http.post(
    "/api/jdbc/driver/upload",
    {
      file: file
    },
    { "Content-Type": "multipart/form-data" }
  );
};

/**
 * 所有支持的驱动类型
 * @returns 所有支持的数据库类型
 */
export const apiListSupportedDbTypes = () => {
  return http.get("/api/gen/datasource/drivers");
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListSelectableDataSources = (internal?: boolean) => {
  return http.get<DataSourceVO[]>("/api/gen/datasource/list/selectable", {
    internal: internal
  });
};

/**
 * 获取数据库下的所有表名称
 * @returns 单个数据库的所有表
 */
export const apiListTableNames = (id: number, dbName: string, pattern?: string) => {
  return http.get<string[]>('/api/gen/datasource/table/names', {
    dataSourceId: id,
    databaseName: dbName,
    pattern: pattern
  });
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiGetTableData = (param: ParamGetDbTableData) => {
  return http.post<DBTableDataVO>("/api/gen/datasource/table/data", param);
};
