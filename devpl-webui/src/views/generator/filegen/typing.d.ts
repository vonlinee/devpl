type TemplateFileGeneration = {
  /**
   * 主键ID
   */
  id?: number
  /**
   * 父节点ID
   */
  parentId?: number
  /**
   * 名称
   */
  itemName: string
  /**
   * 模板ID
   */
  templateId?: number
  /**
   * 模板名称
   */
  templateName?: string
  /**
   * 数据填充策略 db_table-数据库表 manual-手动填充、
   */
  fillStrategy?: string
  /**
   * 数据填充策略名称
   */
  fillStrategyName?: string
}