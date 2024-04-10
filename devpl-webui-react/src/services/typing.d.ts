/**
 * 后台接口的数据返回类型
 */
namespace API {
  type Result<T> = {
    code: number;
    msg: string;
    data: T;
  };

  // 新增结果
  type UpdateResult = Result<boolean>;
  // 删除结果
  type DeleteResult = Result<boolean>;
  // 保存结果
  type SaveResult = Result<boolean>;
  // 列表查询结果
  type ListResult<T = any[]> = Result<T[]> & {
    total: number;
    pages?: number[];
  };
}

/**
 * 下拉列表选项VO
 */
type SelectOptionType = {
  label: string;
  value: string;
};