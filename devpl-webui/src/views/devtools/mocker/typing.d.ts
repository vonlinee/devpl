type DbMockColumn = {
  /**
   * 数据库字段名
   */
  columnName: string
  /**
   * 列数据类型
   */
  columnDataType: number
  /**
   * 生成器ID
   */
  generatorId: string
  /**
   * 生成器名称
   */
  generatorName: string
}

type GeneratorItem = {
  /**
   * 生成器名称
   */
  name: string
  /**
   * 描述信息
   */
  description: string
}

/**
 * 生成器选项
 */
type GeneratorOption = {
  /**
   * 选项名称
   */
  name: string
  /**
   * 配置项值
   */
  value: any
  /**
   * 值类型
   */
  type: number
}
