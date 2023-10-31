import request from "@/utils/request";

export const useDataSourceTestApi = (id: Number) => {
  return request.get("/api/gen/datasource/test/" + id);
};

export const useDataSourceApi = (id: Number) => {
  return request.get("/api/gen/datasource/" + id);
};

export const useDataSourceListApi = () => {
  return request.get("/api/gen/datasource/list");
};

export const useDataSourceSubmitApi = (dataForm: any) => {
  if (dataForm.id) {
    return request.put("/api/gen/datasource", dataForm);
  } else {
    return request.post("/api/gen/datasource", dataForm);
  }
};

export const useDataSourceTableListApi = (id: string) => {
  return request.get("/api/gen/datasource/table/list/" + id);
};

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNames = (dataForm: any) => {
  return request.post("/api/gen/datasource/dbnames", dataForm);
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
