import http from "@/utils/http";

/**
 * 查询数据类型列表
 * @param pageIndex 当前页
 * @param pageSize 每页大小
 */
export const apiListDataTypes = (pageIndex: Number, pageSize: Number) => {
  return http.get("/api/datatype/page", {
    pageIndex: pageIndex,
    pageSize: pageSize
  });
};
