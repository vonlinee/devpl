/**
 * 文件节点
 */
type FileNode = {
  path: string
  key: string
  label: string
  isLeaf: boolean
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
