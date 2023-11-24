/**
 * 模板信息
 */
type TemplateInfo = {
  templateId?: number,
  templateName: string,
  templatePath: string,
  content: string,
  type: number,  // 模板类型，1-字符串模板 2-文件模板
  typeName?: string,
  provider: string
  remark?: string
  /**
   * 是否内置模板，不可删除修改
   */
  internal: boolean
}

type TemplateSelectVO = {
  templateId?: number,
  templateName: string,
  remark?: string
}