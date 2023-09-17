import service from "@/utils/request";

import http from "@/utils/http";

export const useDataSourceTestApi = (id: Number) => {
  return service.get("/gen/datasource/test/" + id);
};

export const useDataSourceApi = (id: Number) => {
  return service.get("/gen/datasource/" + id);
};

export const useDataSourceListApi = () => {
  return service.get("/gen/datasource/list");
};

export const useDataSourceSubmitApi = (dataForm: any) => {
  if (dataForm.id) {
    return service.put("/gen/datasource", dataForm);
  } else {
    return service.post("/gen/datasource", dataForm);
  }
};

export const useDataSourceTableListApi = (id: string) => {
  return service.get("/gen/datasource/table/list/" + id);
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
