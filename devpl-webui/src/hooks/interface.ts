/**
 * 增删改查配置项
 */
export interface IHooksOptions {
    // 否在创建页面时，调用数据列表接口
    createdIsNeed?: boolean
    // 数据列表 Url
    dataListUrl?: string
    // 是否需要分页
    isPage?: boolean
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
    // 分页查询
    queryPage?: (page: number, limit: number, queryForm: any) => Promise<any>
}
