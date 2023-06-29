import service from '@/utils/request'

// 获取模板列表
export function apiListTemplatesByPage(pageIndex: number | undefined, pageSize: number | undefined) {
    return service.get('/api/template/page', {
        params: {
            pageIndex: pageIndex,
            pageSize: pageSize
        }
    });
}