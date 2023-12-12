/**
 * 目标生成文件
 */
type TargetGenFileItem = {
  taskId?: string
  templateId: number
  templateName?: string
  dataFillStrategy?: string
  filename: string
  fileTypeName?: string
}

/**
 * 表文件生成
 */
type TableFileGeneration = {
  /**
   * 主键ID
   */
  id: number
  /**
   * 表ID
   */
  tableId: number
  /**
   * 模板ID
   */
  templateId: number
  /**
   * 模板名称
   */
  templateName: number
  /**
   * 文件名
   */
  fileName: string
  /**
   * 保存路径
   */
  savePath: string
  /**
   * 行编辑状态
   */
  editing: boolean
}
