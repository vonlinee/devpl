/**
 * 扩展vue的类型定义
 * 主要是用于解决typescript报错问题
 */

/**
 * Vue子节点
 * @see RawChildren vue未将其暴露出来
 */
type ChildrenVNodes =
  | string
  | number
  | boolean
  | VNode
  | VNodeArrayChildren
  | (() => any)
