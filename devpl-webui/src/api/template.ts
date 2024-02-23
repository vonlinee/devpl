import http from "@/utils/http";
import { Keys } from "@/api/index";

/**
 * 获取模板列表
 * @param pageIndex
 * @param pageSize
 * @param params
 */
export function apiListTemplatesByPage(
  pageIndex?: number,
  pageSize?: number,
  params?: any
) {
  return http.get("/api/codegen/template/page", {
    pageIndex: pageIndex,
    pageSize: pageSize,
    ...params
  });
}

/**
 * 查询模板类型
 */
export function apiListTemplateTypes() {
  return http.get<TemplateProvider[]>("/api/codegen/template/types");
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

/**
 * 模板参数列表
 */
export function apiListTemplateParams(templateId?: number) {
  return http.get("/api/codegen/template/param/list", {
    templateId: templateId || null
  });
}

/**
 * 模板参数列表
 */
export function apiSaveOrUpdateTemplateParams(params: TemplateParam[]) {
  return http.post("/api/codegen/template/param", {
    params: params
  });
}

/**
 * 获取自定义模板的示例文本
 */
export function apiGetCustomTemplateDirectiveExample() {
  return http.get<string>("/api/codegen/template/directive/custom/example");
}

/**
 * 获取自定义指令列表
 */
export function apiListCustomTemplateDirective(page?: number, limit?: number, queryParams?: any) {
  return http.get("/api/codegen/template/directive/custom/list", {
    page: page,
    limit: limit
  });
}

/**
 * 添加/修改自定义指令
 */
export function apiAddCustomTemplateDirective(param: CustomDirective) {
  return http.post<boolean>("/api/codegen/template/directive/custom/add", param);
}

/**
 * 删除自定义指令
 */
export function apiDeleteCustomTemplateDirective(param: CustomDirective) {
  return http.delete<boolean>("/api/codegen/template/directive/custom/remove", param);
}

/**
 * 模板参数值类型选项
 * @param typeGroupId
 */
export const apiListTemplateParamValueDataTypeOptions = () => {
  return http.get("/api/codegen/template/param/datatypes")
}