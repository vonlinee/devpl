import { request } from '@umijs/max';
import { convertPageParam } from '../utils';

/** 获取当前的用户 GET /api/currentUser */
export async function apiListDataSource(param: PageParams) {
  return request<{
    data: DataSourceListItem[];
  }>('/api/datasource/page', {
    method: 'GET',
    params: convertPageParam(param)
  });
}

/**
 * 保存或更新数据源
 */
export async function apiSaveOrUpdateDataSource(options?: Record<string, any>) {
  return request<DataSourceListItem>('/api/datasource', {
    method: 'POST',
    data: options,
  });
}

/**
 * 测试数据库连接
 * @param id
 * @returns
 */
export const apiTestDataSourceConnection = (id: number) => {
  return request('/api/datasource/test/' + id, {
    method: 'GET',
  });
};

/**
 * 测试数据库连接
 * @param connInfo 连接信息
 * @returns
 */
export const apiTestConnection = (connInfo: DataSourceListItem) => {
  return request('/api/datasource/connection/test', {
    method: 'POST',
    params: connInfo,
  });
};

/**
 * 获取单个数据库连接
 * @param id
 * @returns
 */
export const apiGetDataSource = (id: number) => {
  return request('/api/datasource/' + id);
};

/**
 * 查询所有数据源列表，不分页
 * @returns
 */
export const apiListAllDataSource = () => {
  return request('/api/datasource/list', {
    method: 'GET',
  });
};

/**
 * 删除数据源列表
 * @param ids
 * @returns
 */
export const apiDelDataSource = (ids: (number | string)[]) => {
  return request('/api/datasource', {
    method: 'DELETE',
    data: ids,
  });
};

/**
 * 查询数据源下的所有表信息
 * @param id
 * @returns
 */
export const apiListTableNamesOfDataSource = (id: string | number, dbName: string) => {
  return request<{
    data: string[];
  }>(`/api/datasource/table/names`, {
    method: 'GET',
    params: {
      dataSourceId: id,
      databaseName: dbName
    }
  });
};

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNames = (dataForm: any) => {
  return request<{
    data: string[];
  }>('/api/datasource/dbnames', {
    method: 'GET',
    params: dataForm,
  });
};

/**
 * 获取所有数据库名称
 * @param dataForm
 */
export const apiGetDatabaseNamesById = (dataSourceId: number) => {
  return request<{
    data: string[];
  }>(`/api/datasource/dbnames/${dataSourceId}`);
};

/**
 * 上传驱动jar包
 * @param file
 */
export const apiUploadDriverJar = (file: File) => {
  return request('/api/jdbc/driver/upload', {
    method: 'POST',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: {
      file: file,
    },
  });
};

/**
 * 所有支持的驱动类型
 * @returns 所有支持的数据库类型
 */
export const apiListSupportedDbTypes = () => {
  return request<{
    data: DriverTypeVO[];
  }>('/api/datasource/drivers', {
    method: 'GET',
  });
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListSelectableDataSources = () => {
  return request<{
    data: DataSourceVO[];
  }>('/api/datasource/list/selectable', {
    method: 'GET',
  });
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiListTableNames = (id: number, dbName: string) => {
  return request(`/api/datasource/${id}/${dbName}/table/names`, {
    method: 'GET',
  });
};

/**
 * 所有支持的数据库类型
 * @returns 所有支持的数据库类型
 */
export const apiGetTableData = (param: ParamGetDbTableData) => {
  return request<DBTableDataVO>('/api/datasource/table/data', {
    method: 'GET',
    params: param,
  });
};
