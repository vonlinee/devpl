import http from "@/utils/http";

/**
 * 获取模板列表
 * @param pageIndex
 * @param pageSize
 */
export function apiListTemplatesByPage(pageIndex: number | undefined, pageSize: number | undefined) {
    return http.get('/api/codegen/template/page', {
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
    return http.post('/api/codegen/template/save', params);
}

/**
 * 修改模板
 * @param params
 */
export function apiUpdateTemplate(params: object) {
    return http.put('/api/codegen/template/update', params);
}

/**
 * 可选择的模板列表
 */
export function apiListSelectableTemplates() {
    return http.get('/api/codegen/template/list/selectable');
}

/**
 * 获取模板信息
 * @param templateId 模板ID
 */
export function apiGetTemplateById(templateId: number) {
    return http.get(`/api/codegen/template/info/${templateId}`)
}
