import http from "@/utils/http";

/**
 * 查询生成表列表
 * @param id
 */
export const apiListGenTables = (id: number) => {
  return http.get<GenTable>("/gen/table/" + id);
};

export const useTableSubmitApi = (dataForm: any) => {
  return http.put("/gen/table", dataForm);
};

/**
 * 导入表
 * @param datasourceId
 * @param tableNameList
 */
export const apiImportTables = (datasourceId: number, tableNameList: string[]) => {
  return http.post("/gen/table/import/" + datasourceId, tableNameList);
};

export const useTableFieldSubmitApi = (tableId: number, fieldList: any) => {
  return http.put("/gen/table/field/" + tableId, fieldList);
};

/**
 * 同步表的配置
 * @param id
 */
export const apiSyncTable = (id: number) => {
  return http.post("/gen/table/sync/" + id);
};
