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
  /**
   * 是否删除
   */
  deleted: boolean
}

/**
 * 文件生成结果
 */
type FileGenerationResult = {
  /**
   * 所有生成的根目录
   */
  rootDirs: string[]
}

/**
 * 文件节点
 */
type FileNode = {
  path: string
  key: string
  label: string
  leaf: boolean
  selectable: boolean
  extension: string // 文件后缀名
  children: FileNode[]
}

/**
 * 目标生成文件
 */
type TargetGenFile = {
  /**
   * 唯一ID
   */
  id?: number
  /**
   * 目标生成文件类型名称
   */
  typeName: string
  /**
   * 文件名称
   */
  fileName: string
  /**
   * 模板ID
   */
  templateId?: number
  /**
   * 模板名称
   */
  templateName: string
  /**
   * 备注信息
   */
  remark: string
  /**
   * 是否处于编辑状态
   */
  editing: boolean
  /**
   * 是否内置的生成文件类型，内置的不可删除，不可修改
   */
  builtin: boolean,
  /**
   * 是否默认在代码生成中生成此类型的文件
   */
  defaultTarget: boolean,
  /**
   * 是否选中
   */
  selected?: boolean
}