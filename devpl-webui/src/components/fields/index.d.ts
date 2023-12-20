/**
 * 字段节点
 */
type FieldTreeNode = {
  /**
   * 唯一ID
   */
  id: number
  /**
   * 字段名称
   */
  label?: string
  /**
   * 字段注释
   */
  comment?: string
  /**
   * 是否正处于编辑状态
   */
  editing?: boolean
  /**
   * 数据类型
   */
  dataType?: string
  /**
   * 是否有子节点
   */
  hasChildren?: boolean
  /**
   * 子节点
   */
  children?: FieldTreeNode[]
}