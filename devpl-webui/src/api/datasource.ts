import service from "@/utils/request";

import http from "@/utils/http";

export const useDataSourceTestApi = (id: Number) => {
  return service.get("/api/gen/datasource/test/" + id);
};

export const useDataSourceApi = (id: Number) => {
  return service.get("/api/gen/datasource/" + id);
};

export const useDataSourceListApi = () => {
  return service.get("/api/gen/datasource/list");
};

export const useDataSourceSubmitApi = (dataForm: any) => {
  if (dataForm.id) {
    return service.put("/api/gen/datasource", dataForm);
  } else {
    return service.post("/api/gen/datasource", dataForm);
  }
};

export const useDataSourceTableListApi = (id: string) => {
  return service.get("/api/gen/datasource/table/list/" + id);
};

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNames = (dataForm: any) => {
  return service.post("/api/gen/datasource/dbnames", dataForm);
};


/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNamesById = (dataSourceId: number) => {
  return service.get(`/api/gen/datasource/dbnames/${dataSourceId}`);
};

/**
 * 上传驱动jar包
 * @param file
 */
export const apiUploadDriverJar = (file: File) => {
  return http.postForm("/api/jdbc/driver/upload", {
    file: file
  });
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListSupportedDbTypes = () => {
  return http.get("/api/gen/datasource/dbtypes");
};


/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListSelectableDataSources = () => {
  return http.get("/api/gen/datasource/list/selectable");
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListTableNames = (id: number, dbName: string) => {
  return http.get(`/api/gen/datasource/${id}/${dbName}/table/names`);
};