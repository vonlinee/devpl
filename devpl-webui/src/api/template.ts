import service from '@/utils/request'

import http from "@/utils/http";

// 获取模板列表
export function apiListTemplatesByPage(pageIndex: number | undefined, pageSize: number | undefined) {
    return service.get('/api/template/page', {
        params: {
            pageIndex: pageIndex,
            pageSize: pageSize
        }
    });
}

/**
 * 新增模板
 * @param params
 */
export function apiAddTemplate(params: object) {
    return http.postJson('/api/template/save', params);
}


/**
 * 修改模板
 * @param params
 */
export function apiUpdateTemplate(params: object) {
    return http.putJson('/api/template/update', params);
}
