/**
 * 字段信息
 */
type FieldInfo = {
  /**
   * ID
   */
  id?: string | number
  /**
   * 父节点ID
   */
  parentId?: string | number
  /**
   * 字段Key
   */
  fieldKey: string
  /**
   * 字段名，中文名
   */
  fieldName?: string
  /**
   * 值，对应数据类型的值
   */
  value?: any
  /**
   * 数据类型
   */
  dataType?: string | number
  /**
   * 描述信息
   */
  description?: string
  /**
   * 默认值
   */
  defaultValue?: string
  /**
   * 是否选中，默认否
   */
  selected?: boolean
  /**
   * 子节点
   */
  children?: FieldInfo[]
  /**
   * 是否有子节点
   */
  hasChildren?: boolean
  /**
   * 是否处于编辑状态
   */
  editing?: boolean
  /**
   * 是否是叶子结点，默认是叶子结点
   */
  leaf?: boolean
}

/**
 * 字段组信息
 */
type FieldGroup = {
  /**
   * 字段组ID
   */
  id: number
  /**
   * 字段组名称
   */
  groupName: string
}
