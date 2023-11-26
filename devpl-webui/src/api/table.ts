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
 * @param projectId
 */
export const apiImportTables = (datasourceId: number, tableNameList: string[], projectId?: number) => {
  return http.post("/gen/table/import/", {
    dataSourceId: datasourceId,
    tableNameList: tableNameList,
    projectId: projectId
  });
};

export const apiUpdateGenTableFields = (tableId: number, fieldList: any) => {
  return http.put("/gen/table/field/" + tableId, fieldList);
};

/**
 * 同步表的配置
 * @param id
 */
export const apiSyncTable = (id: number) => {
  return http.post("/gen/table/sync/" + id);
};
