type GenTable = {
	id: number,
	tableName: string,
	className: string,
	tableComment: string,
	fieldList: GenTableField[]
}

type GenTableField = {
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