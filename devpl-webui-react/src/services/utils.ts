/**
 * 转换分页参数
 * ant design pro支持的参数名称为current和pageSize
 * 这里需要写后端实际支持的分页参数名称
 */
export const convertPageParam = (param: any) => {
  if (param == null || param == undefined) {
    return param
  }
  return {
    page: param.current,
    limit: param.pageSize,
    ...param,
  };
};
