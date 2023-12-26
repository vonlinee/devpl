/**
 * 字段信息
 */
type FieldInfo = {
  /**
   * ID
   */
  id: string | number
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
  fieldName: string
  /**
   * 数据类型
   */
  dataType?: string
  /**
   * 描述信息
   */
  description?: string
  /**
   * 默认值
   */
  defaultValue?: string
  /**
   * 是否选中
   */
  selected?: boolean,
  /**
   * 子节点
   */
  children?: FieldInfo[]
}
