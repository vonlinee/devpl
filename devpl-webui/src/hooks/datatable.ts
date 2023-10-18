/**
 * 数据表格配置项
 */
export interface DataTableOptions {
	// 否在创建页面时，调用数据列表接口
	createdIsNeed?: boolean
	// 数据列表 Url
	dataListUrl?: string,
	/**
	 * 查询数据
	 * @param param 
	 * @returns 表格所需的数据
	 */
	dataSupplier?: (param: any) => any[]
	// 是否需要分页
	pageable?: boolean
	// 删除 Url
	deleteUrl?: string
	// 主键key，字段名称，用于多选删除场景
	primaryKey?: string
	// 导出 Url
	exportUrl?: string
	// 查询条件
	queryForm?: any
	// 数据列表
	dataList?: any[]
	// 排序字段
	order?: string
	// 是否升序
	asc?: boolean
	// 当前页码
	page?: number
	// 每页数
	limit?: number
	// 总条数
	total?: number
	// 每页页数大小选项，可为空，不能为undefined, el-pagination组件的page-sizes属性不能为undefined
	pageSizes?: number[]
	// 数据列表，loading状态，loading显示/隐藏根据此值确定
	dataListLoading?: boolean
	// 数据列表，多选项
	dataListSelections?: any[]
}

/**
 * 默认选项
 */
const defaultOptions: DataTableOptions = {
	createdIsNeed: true,
	dataListUrl: '',
	pageable: true,
	deleteUrl: '',
	primaryKey: 'id',
	exportUrl: '',
	queryForm: {},
	dataList: [],
	order: '',
	asc: false,
	page: 1,
	limit: 10,
	total: 0,
	pageSizes: [1, 10, 20, 50, 100, 200],
	dataListLoading: false,
	dataListSelections: []
}

/**
 * 合并配置选项与默认选项
 * @param options
 * @param props
 */
export const mergeDefaultOptions = (props: any): DataTableOptions => {
	let options : any = defaultOptions
	for (const key in options) {
		if (!Object.getOwnPropertyDescriptor(props, key)) {
			props[key] = options[key]
		}
	}
	return props
}

