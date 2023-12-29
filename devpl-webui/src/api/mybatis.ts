import http from "@/utils/http"

/**
 * 获取Mapper中的参数
 * @param content
 */
export const apiGetSampleXmlText = () => {
  return http.get("/api/tools/mybatis/ms/sample")
}

/**
 * 获取Mapper中的参数
 * @param content
 * @param options
 */
export const getMapperStatementParams = (content: string, options: any) => {
  return http.post("/api/tools/mybatis/ms/params", {
    mapperStatement: content,
    ...options,
  })
}

/**
 * 获取Mapper中的参数
 */
export const apiGetMapperStatementValueTypes = () => {
  return http.get("/api/tools/mybatis/ms/param/datatypes")
}

/**
 * 获取Mapper中的SQL
 * @return SQL 字符串
 */
export const apiGetSql = (ms: string, params: any[], real: boolean) => {
  return http.post("/api/tools/mybatis/ms/sql", {
    mapperStatement: ms,
    msParams: params,
    real: real ? 1 : 0,
  })
}
