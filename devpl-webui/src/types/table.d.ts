/**
 * 表生成信息
 */
type TableGeneration = {
  id: number
  tableName: string
  className: string
  tableComment: string
  fieldList: TableGenerationField[]
}

/**
 * 生成的表的字段
 */
type TableGenerationField = {
  tableId: number
  fieldName: string
  sort: number
  fieldType: string
  fieldComment: string
  attrName: string
  attrType: string
  packageName: string
  autoFill: string
  primaryKey: number
  baseField: number
  formItem: number
  formType: string
  formDict: string
  formValidator: string
  gridItem: boolean
}
