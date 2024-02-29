import http from "@/utils/http"

export const apiModel2Ddl = (code: string) => {
  return http.post("/api/devtools/ddl", {
    content: code,
  })
}

export const apiGetTableCreatorColumns = (fieldGroupId: number) => {
  return http.post("/api/devtools/table/create/columns", {
    groupId: fieldGroupId,
  })
}

/**
 * 获取DDL
 * @param fieldGroupId 
 * @returns 
 */
export const apiGetTableCreatorDDL = (param: any) => {
  return http.post<string>("/api/devtools/table/create/ddl", param)
}