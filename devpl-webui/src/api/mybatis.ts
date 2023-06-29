import service from '@/utils/request'

/**
 * 获取Mapper中的参数
 * @param content
 */
export const getMapperStatementParams = (content: string) => {
    let param: Object = {
        ms: content
    }
    return service.post('/api/tools/mybatis/ms/params', param)
}

/**
 * 获取Mapper中的参数
 */
export const apiGetDataTypes = () => {
    return service.get('/api/tools/mybatis/ms/param/datatypes')
}

/**
 * 获取Mapper中的SQL
 * @return SQL 字符串
 */
export const apiGetSql = (ms: string, params: any[], real: boolean) => {
    return service.post('/api/tools/mybatis/ms/sql', {
        mapperStatement: ms,
        msParams: params,
        real: real ? 1 : 0.
    })
}