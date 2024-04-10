import { request } from '@umijs/max';
import { convertPageParam } from '../utils';

/**
 * 查询数据类型列表
 * @param pageIndex 当前页
 * @param pageSize 每页大小
 * @param param
 */
export const apiListDataTypes = (param: any = null) => {
  return request<API.ListResult<DataTypeListItem>>('/api/datatype/page', {
    method: 'GET',
    params: convertPageParam(param),
  });
};

/**
 * @returns
 */
export const apiListAllDataTypeGroups = () => {
  return request<{
    data: DataTypeGroupItem[];
  }>('/api/datatype/groups', {
    method: 'GET',
  });
};

/**
 * @returns
 */
export const apiSaveDataTypeGroup = (group: any) => {
  return request('/api/datatype/group/add', {
    method: 'GET',
    params: {
      groupId: group.typeGroupId,
      groupName: group.typeGroupName,
    },
  });
};

/**
 * @returns
 */
export const apiUpdateDataTypeItem = (dataTypeItem: any) => {
  return request('/api/datatype/update', dataTypeItem);
};

/**
 * 批量新增或更新数据类型
 * @returns
 */
export const apiSaveDataTypeItems = (dataTypeItem: DataTypeListItem[]) => {
  return request('/api/datatype/saveOrUpdateBatch', {
    method: "POST",
    data: {
      dataTypeItems: dataTypeItem
    }
  });
};


/**
 * 批量新增或更新数据类型
 * @returns
 */
export const apiEditDataTypeItem = (dataTypeItem: DataTypeListItem) => {
  return request('/api/datatype/edit', {
    method: "POST",
    data: dataTypeItem
  });
};

export interface DataTypeMappingParam {
  typeId: number;
  anotherTypeId: number | undefined;
}

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiAddDataTypeMapping = (dataTypeItem: DataTypeMappingParam[]) => {
  return request('/api/datatype/mapping', {
    mappings: dataTypeItem,
  });
};

/**
 * 所有可映射的数据类型
 * @param typeId 如果为空，则查询所有未设置过类型映射的数据类型
 * @returns
 */
export const apiListAllMappableDataTypes = (typeId: number | undefined = undefined) => {
  return request('/api/datatype/mappable', {
    typeId: typeId,
  });
};

/**
 * 添加数据类型映射关系
 * @returns
 */
export const apiListAllDataTypeMappings = (
  typeId: number | undefined = undefined,
  typeGroupId: number | undefined = undefined,
) => {
  return request('/api/datatype/mapping/all', {
    typeId: typeId,
    typeGroupId: typeGroupId,
  });
};
