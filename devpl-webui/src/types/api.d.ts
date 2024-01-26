
/**
 * 组件库封装和后端接口定义
 * 通用的VO类型定义
 * 需要前端组件搭配后端的接口
 */

/**
 * 选项类型的组件接口响应数据
 */
type SelectOptionVO = {
  /**
   * 选项的key，在所有选项中唯一
   */
  key: string | number
  /**
   * 显示的文本
   */
  label: string
  /**
   * 该选项的描述信息
   */
  description?: string
  /**
   * 选项的值
   */
  value: any
}















