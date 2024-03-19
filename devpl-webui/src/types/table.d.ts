/**
 * 表生成信息
 */
type TableGeneration = {
  id: number | string
  tableName: string
  /**
   * 表类型
   */
  tableType: string
  className: string
  /**
   * 连接名称
   */
  connectionName: string
  tableComment: string

  baseclassId: string
  generatorType: number
  formLayout: number
  backendPath: string
  frontendPath: string
  packageName: string
  email: string
  author: string
  version: string
  moduleName: string
  functionName: string
  className: string
  tableComment: string
  tableName: string
  /**
   * 字段列表
   */
  fieldList: TableGenerationField[]
  /**
   * 表需要生成的文件
   */
  generationFiles?: TableFileGeneration[]
  /**
   * 模板参数 JSON字符串
   */
  templateArguments: Record<string, any>
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

class TableImportInfo {
  /**
   * 数据源 ID
   */
  dataSourceId: number

  /**
   * 数据库名称
   */
  databaseName: string

  /**
   * 表名
   */
  tableName: string
}
