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
  id: number
  tableId: number
  templateId: number
  templateName: number
  fileName: string
  savePath: string
}
