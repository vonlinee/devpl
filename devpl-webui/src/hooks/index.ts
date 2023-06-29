import {IHooksOptions} from '@/hooks/interface'
import service from '@/utils/request'
import {onMounted} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'

export const useCrud = (options: IHooksOptions) => {

    /**
     * 默认选项
     */
    const defaultOptions: IHooksOptions = {
        createdIsNeed: true,
        dataListUrl: '',
        isPage: true,
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
    const mergeDefaultOptions = (options: any, props: any): IHooksOptions => {
        for (const key in options) {
            if (!Object.getOwnPropertyDescriptor(props, key)) {
                props[key] = options[key]
            }
        }
        return props
    }

    // 覆盖默认值
    const state = mergeDefaultOptions(defaultOptions, options)

    onMounted(() => {
        if (state.createdIsNeed) {
            query()
        }
    })

    // 查询
    const query = () => {
        if (!state.dataListUrl) {
            return
        }
        state.dataListLoading = true
        service.get(state.dataListUrl, {
            params: {
                order: state.order,
                asc: state.asc,
                page: state.isPage ? state.page : null,
                limit: state.isPage ? state.limit : null,
                ...state.queryForm
            }
        }).then((res: any) => {
            // 分页
            state.dataList = state.isPage ? res.data.list : res.data
            state.total = state.isPage ? res.data.total : 0
        })
        state.dataListLoading = false
    }

    /**
     * 默认查询回调
     */
    const getDataList = () => {
        state.page = 1
        query()
    }

    /**
     * 每页大小变化回调，默认查第一页
     * @param val
     */
    const sizeChangeHandle = (val: number) => {
        state.page = 1
        state.limit = val
        query()
    }

    /**
     * 当前页变化回调
     * @param val
     */
    const currentChangeHandle = (val: number) => {
        state.page = val
        query()
    }

    /**
     * 多选回调
     * @param selections
     */
    const selectionChangeHandle = (selections: any[]) => {
        state.dataListSelections = selections.map((item: any) => state.primaryKey && item[state.primaryKey])
    }

    /**
     * 排序回调
     * @param data
     */
    const sortChangeHandle = (data: any) => {
        const {prop, order} = data

        if (prop && order) {
            state.order = prop
            state.asc = order === 'ascending'
        } else {
            state.order = ''
        }

        query()
    }

    /**
     * 删除回调
     * @param key
     */
    const deleteHandle = (key: number | string) => {
        if (!state.deleteUrl) {
            return
        }

        ElMessageBox.confirm('确定进行删除操作?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
            .then(() => {
                service.delete(state.deleteUrl + '/' + key).then(() => {
                    ElMessage.success('删除成功')

                    query()
                })
            })
            .catch(() => {
            })
    }

    /**
     * 批量删除回调
     * @param key
     */
    const deleteBatchHandle = (key?: number | string) => {
        let data: any[] = []
        if (key) {
            data = [key]
        } else {
            data = state.dataListSelections ? state.dataListSelections : []

            if (data.length === 0) {
                ElMessage.warning('请选择删除记录')
                return
            }
        }

        ElMessageBox.confirm('确定进行删除操作?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
            .then(() => {
                if (state.deleteUrl) {
                    service.delete(state.deleteUrl, {data}).then(() => {
                        ElMessage.success('删除成功')

                        query()
                    })
                }
            })
            .catch(() => {
            })
    }

    return {
        getDataList,
        sizeChangeHandle,
        currentChangeHandle,
        selectionChangeHandle,
        sortChangeHandle,
        deleteHandle,
        deleteBatchHandle
    }
}
