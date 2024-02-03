/**
 * 模板实现
 */
type TemplateProvider = {
  /**
   * ID
   */
  provider: string
  /**
   * 名称
   */
  providerName: string
}

/**
 * 模板信息
 */
type TemplateInfo = {
  /**
   * 模板ID
   */
  templateId?: number
  /**
   * 模板名称
   */
  templateName: string
  /**
   * 模板路径
   */
  templatePath: string
  /**
   * 模板内容
   */
  content: string
  /**
   * 模板类型，1-字符串模板 2-文件模板
   */
  type: number
  /**
   * 模板类型名称
   */
  typeName?: string
  /**
   * 模板提供者
   * @see TemplateProvider
   */
  provider: string
  /**
   * 备注信息
   */
  remark?: string
  /**
   * 是否内置模板，不可删除修改
   */
  internal: boolean
}

/**
 * 模板选择项VO
 */
type TemplateSelectVO = {
  /**
   * 模板ID
   */
  templateId: number
  /**
   * 模板名称
   */
  templateName: string
  /**
   * 备注信息
   */
  remark?: string
}

/**
 * 模板参数
 */
type TemplateParam = {
  /**
   * 参数key
   */
  paramKey?: number
  /**
   * 参数名称
   */
  paramName: string
  /**
   * 参数值
   */
  paramValue: string
  /**
   * 默认值
   */
  defaultValue: string
  /**
   * 数据类型ID
   */
  dataTypeId: number | string
}

/**
 * 自定义指令
 */
type CustomDirective = {
  directiveId?: number | string,
  directiveName: string,
  sourceCode: string,
  remark?: string
}