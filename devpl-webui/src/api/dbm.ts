import http from "@/utils/http"

/**
 * 数据类型选项
 * @param typeGroupId 非空
 */
export const apiListColumnDataTypeOptions = (dbType: string) => {
  return http.get("/api/dbm/design/table/column/types", {
    dbType: dbType,
  })
}
