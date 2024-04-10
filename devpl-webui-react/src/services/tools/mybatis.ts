import { request } from '@umijs/max';

/** 获取当前的用户 GET /api/currentUser */
export async function apiListMsDataTypes() {
  return request<{
    data: DataTypeVO[];
  }>('/api/tools/mybatis/ms/param/datatypes', {
    method: 'GET',
  });
}

export async function apiGetSampleInput() {
  return request<{
    data: string;
  }>('/api/tools/mybatis/ms/sample', {
    method: 'GET',
  });
}

/**
 * 获取Mapper中的参数
 * @param content
 */
export async function getMapperStatementParams(content: string, options: any) {
  return request<{
    data: ParamNode[]
  }>('/api/tools/mybatis/ms/params', {
    method: 'POST',
    data: {
      mapperStatement: content,
      ...options,
    },
  });
}

/**
 * 获取Mapper中的SQL
 * @return SQL 字符串
 */
export async function apiGetSql(ms: string, params: any[], real: boolean) {
  return request<{
    data: string;
  }>('/api/tools/mybatis/ms/sql', {
    method: 'POST',
    data: {
      mapperStatement: ms,
      msParams: params,
      real: real ? 1 : 0,
    },
  });
}
