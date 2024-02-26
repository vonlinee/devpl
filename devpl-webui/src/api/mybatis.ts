import http from "@/utils/http"
import { FieldParseParam, FieldParseResult } from "./fields"

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

/**
 * 加载目录中的所有mapper文件
 * @return SQL 字符串
 */
export const apiBuildIndex = (dir: string) => {
  return http.get<string[]>("/api/tools/mybatis/index/build", {
    projectRootDir: dir
  })
}

/**
 * 加载目录中的所有mapper文件
 * @return SQL 字符串
 */
export const apiGetMapperStatementContent = (dir: string, msId: string) => {
  return http.get<string[]>("/api/tools/mybatis/index/query/ms", {
    projectRootDir: dir,
    mapperStatementId: msId
  })
}

/**
 * 加载目录中的所有mapper文件
 * @return SQL 字符串
 */
export const apiBuildMappedStatementIndex = (dir: string, reset: boolean) => {
  return http.get("/api/tools/mybatis/index/build/ms", {
    dir: dir,
    reset: reset
  })
}

/**
 * 加载目录中的所有mapper文件
 * @return SQL 字符串
 */
export const apiListIndexedProjectRootPaths = () => {
  return http.get<string[]>("/api/tools/mybatis/index/project")
}

/**
 * 
 * @return SQL 字符串
 */
export const apiListMappedStatements = (page: number, limit: number, params: any) => {
  return http.get("/api/tools/mybatis/index/query/mslist", {
    pageIndex: page,
    pageSize: limit,
    ...params
  })
}