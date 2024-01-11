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

/**
 * 数据类型下拉选项
 */
type DataTypeSelectOption = {
  /**
   * 选项显示文本
   */
  label: string
  /**
   * 选项的key
   */
  key: number | string
  /**
   * 选项key对应的值
   */
  value: any
}
