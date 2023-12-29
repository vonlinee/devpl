/**
 * 类型分组
 */
type DataTypeGroup = {
  id?: number
  /**
   * 类型分组ID
   */
  typeGroupId: string
  /**
   * 类型分组名称
   */
  typeGroupName: string
  /**
   * 备注信息
   */
  remark: string
  /**
   * 是否处于编辑状态
   */
  editing: boolean
  /**
   * 是否内置
   */
  internal: boolean
}
