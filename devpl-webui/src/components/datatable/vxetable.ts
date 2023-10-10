import { reactive } from 'vue'
import { VxeGridProps } from 'vxe-table'

export interface VDTColumnOptions {
    type?: string,
    title?: string,
    field?: string,
    width?: number,
    editRender?: any,
    slots?: any,
    showOverflow?: boolean
}

export interface VDTOptions {
    // 列配置项
    columns: VDTColumnOptions[]
}


function useVxeGridTable() {


    let columns: ColumnOptions<VxeTableDataRow>[] = []

    const gridOptions = reactive<VxeGridProps>({
        border: true,
        keepSource: true,
        height: 500,
        printConfig: {},
        importConfig: {},
        exportConfig: {},
        columnConfig: {
            resizable: true
        },
        pagerConfig: {
            enabled: true,
            perfect: true,
            pageSize: 15
        },
        editConfig: {
            trigger: 'click',
            mode: 'row',
            showStatus: true
        },
        toolbarConfig: {
            buttons: [
                { code: 'insert_actived', name: '新增', status: 'perfect', icon: 'vxe-icon-add' },
                { code: 'mark_cancel', name: '标记/取消', status: 'perfect', icon: 'vxe-icon-delete' },
                { code: 'save', name: '保存', status: 'perfect', icon: 'vxe-icon-save' }
            ],
            perfect: true,
            refresh: {
                icon: 'vxe-icon-refresh',
                iconLoading: 'vxe-icon-refresh roll'
            },
            import: {
                icon: 'vxe-icon-cloud-upload'
            },
            export: {
                icon: 'vxe-icon-cloud-download'
            },
            print: {
                icon: 'vxe-icon-print'
            },
            zoom: {
                iconIn: 'vxe-icon-fullscreen',
                iconOut: 'vxe-icon-minimize'
            },
            custom: {
                icon: 'vxe-icon-menu'
            }
        },
        proxyConfig: {
            props: {
                result: 'result',
                total: 'page.total'
            },
            ajax: {

            }
        },
        columns: columns
    })

    return gridOptions
}

