import { MenuList, MenuResponse } from "@/types/menu";

/**
 * 菜单数据
 * 手动配置菜单数据
 */
let menu: MenuList = [
  {
    menu_id: 1,
    [MENU_TITLE]: "代码生成",
    [MENU_PATH]: "/codegen",
    [MENU_KEY]: "codegen",
    [MENU_PARENTKEY]: "",
    [MENU_ICON]: "icon_form",
    [MENU_KEEPALIVE]: "false",
    order: 1,
  },
  {
    menu_id: 9,
    [MENU_TITLE]: "数据模拟",
    [MENU_PATH]: "/mocker",
    [MENU_KEY]: "list",
    [MENU_PARENTKEY]: "",
    [MENU_ICON]: "icon_list",
    [MENU_KEEPALIVE]: "false",
    order: 2,
  },
  {
    menu_id: 9,
    [MENU_TITLE]: "元数据",
    [MENU_PATH]: "/meta",
    [MENU_KEY]: "meta",
    [MENU_PARENTKEY]: "",
    [MENU_ICON]: "icon_list",
    [MENU_KEEPALIVE]: "false",
    order: 2,
  },
  {
    menu_id: 9,
    [MENU_TITLE]: "类型系统",
    [MENU_PATH]: "/meta/types",
    [MENU_KEY]: "types",
    [MENU_PARENTKEY]: "meta",
    [MENU_ICON]: "icon_list",
    [MENU_KEEPALIVE]: "false",
    order: 2,
  },
  {
    menu_id: 10,
    [MENU_TITLE]: "数据库",
    [MENU_PATH]: "/database",
    [MENU_KEY]: "listCard",
    [MENU_PARENTKEY]: "list",
    [MENU_ICON]: "",
    [MENU_KEEPALIVE]: "false",
    order: 5485,
  },
  {
    menu_id: 3,
    [MENU_TITLE]: "403",
    [MENU_PATH]: "/403",
    [MENU_KEY]: "error403",
    [MENU_PARENTKEY]: "result",
    [MENU_ICON]: "icon_locking",
    [MENU_KEEPALIVE]: "false",
    order: 0,
  },
  {
    menu_id: 4,
    [MENU_TITLE]: "404",
    [MENU_PATH]: "/404",
    [MENU_KEY]: "error404",
    [MENU_PARENTKEY]: "result",
    [MENU_ICON]: "icon_close",
    [MENU_KEEPALIVE]: "false",
    order: 1,
  },
  {
    menu_id: 5,
    [MENU_TITLE]: "500",
    [MENU_PATH]: "/500",
    [MENU_KEY]: "error500",
    [MENU_PARENTKEY]: "result",
    [MENU_ICON]: "icon_privacy_closed",
    [MENU_KEEPALIVE]: "false",
    order: 4568,
  },
  {
    menu_id: 8,
    [MENU_TITLE]: "图标库",
    [MENU_PATH]: "/icons",
    [MENU_KEY]: "icons",
    [MENU_PARENTKEY]: "",
    [MENU_ICON]: "icon_pen",
    [MENU_KEEPALIVE]: "true",
    order: 10,
  },
];

export const getSideMenus = (): Promise<MenuResponse> => {
  return new Promise<MenuResponse>(function (resolve, reject) {
    resolve(menu);
  });
};
