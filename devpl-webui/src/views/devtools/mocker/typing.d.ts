type MockField = {
  /**
   * 名称
   */
  fieldName: string
  /**
   * 数据类型
   */
  dataType: number
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
