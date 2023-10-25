import http from '@/utils/http'

/**
 * 获取Mapper中的参数
 * @param content
 */
export const getMapperStatementParams = (content: string) => {
    return http.postJson('/api/tools/mybatis/ms/params', {
        mapperStatement: content
    })
}

/**
 * 获取Mapper中的参数
 */
export const apiGetDataTypes = () => {
    return http.get('/api/tools/mybatis/ms/param/datatypes')
}

/**
 * 获取Mapper中的SQL
 * @return SQL 字符串
 */
export const apiGetSql = (ms: string, params: any[], real: boolean) => {
    return http.postJson('/api/tools/mybatis/ms/sql', {
        mapperStatement: ms,
        msParams: params,
        real: real ? 1 : 0.
    })
}