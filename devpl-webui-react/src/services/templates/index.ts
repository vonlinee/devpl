import { request } from '@umijs/max';
import { convertPageParam } from '../utils';

/**
 * 获取模板列表
 * @param pageIndex
 * @param pageSize
 */
export function apiListTemplatesByPage(params: PageParams) {
    return request<{
        data: TemplateListItem[]
    }>('/api/codegen/template/page', {
        method: 'GET',
        params: convertPageParam(params)
    });
}

/**
 * 新增模板
 * @param params
 */
export function apiAddTemplate(params: TemplateListItem) {
    return request('/api/codegen/template/save', {
        method: 'POST',
        data: params
    });
}

/**
 * 修改模板
 * @param params
 */
export function apiUpdateTemplate(params: TemplateListItem | Partial<TemplateListItem>) {
    return request('/api/codegen/template/update', {
        method: 'PUT',
        data: params
    });
}

/**
 * 可选择的模板列表
 */
export function apiListSelectableTemplates() {
    return request('/api/codegen/template/list/select');
}

/**
 * 获取模板信息
 * @param templateId 模板ID
 */
export function apiGetTemplateById(templateId: number) {
    return request(`/api/codegen/template/info/${templateId}`)
}
