import http from "@/utils/http";
import { Keys } from "@/api/index";

/**
 * 获取模板列表
 * @param pageIndex
 * @param pageSize
 * @param params
 */
export function apiListTemplatesByPage(pageIndex?: number, pageSize?: number, params?: any) {
  return http.get("/api/codegen/template/page", {
    pageIndex: pageIndex,
    pageSize: pageSize,
    ...params
  });
}

/**
 * 新增模板
 * @param params
 */
export function apiAddTemplate(params: object) {
  return http.post("/api/codegen/template/save", params);
}

/**
 * 新增模板
 * @param ids
 */
export function apiBatchRemoveTemplateByIds(ids: Keys) {
  return http.delete("/api/codegen/template/delete/batch/ids", ids);
}


/**
 * 修改模板
 * @param params
 */
export function apiUpdateTemplate(params: object) {
  return http.put("/api/codegen/template/update", params);
}

/**
 * 可选择的模板列表
 */
export function apiListSelectableTemplates() {
  return http.get("/api/codegen/template/list/selectable");
}

/**
 * 获取模板信息
 * @param templateId 模板ID
 */
export function apiGetTemplateById(templateId: number) {
  return http.get<TemplateInfo>(`/api/codegen/template/info/${templateId}`);
}
