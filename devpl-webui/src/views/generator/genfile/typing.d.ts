/**
 * 表格行数据
 */
type GenFile = {
  pid: number
  fileName: string
  templateId?: number
  templateName: string
  remark: string
  editing: boolean
  /**
   * 是否内置的生成文件类型，内置的不可删除
   */
  builtin: boolean
  /**
   * 是否选中
   */
  selected?: boolean
}