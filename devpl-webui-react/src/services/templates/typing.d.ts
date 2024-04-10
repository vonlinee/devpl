
/**
 * 模板列表
 */
type TemplateListItem = {
  templateId?: number;
  templateName?: string;
  type: string;
  templatePath: string;
  content: string;
  provider?: string;
  remark?: string;
  internal: boolean
}