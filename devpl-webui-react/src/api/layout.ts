import { MenuList, MenuResponse } from "@/types/menu";

/**
 * 菜单数据
 * 手动配置菜单数据
 */
const menu: MenuList = [
  {
    id: 1,
    title: "代码生成",
    path: "/codegen",
    key: "codegen",
    parentKey: "",
    icon: "icon_form",
    keepAlive: "false",
    order: 1,
  },
  {
    id: 2,
    title: "数据模拟",
    path: "/mocker",
    key: "mocker",
    parentKey: "",
    icon: "icon_list",
    keepAlive: "false",
    order: 2,
  },
  {
    id: 3,
    title: "元数据",
    path: "/meta",
    key: "meta",
    parentKey: "",
    icon: "icon_like",
    keepAlive: "false",
    order: 2,
  },
  {
    id: 4,
    title: "类型系统",
    path: "/types",
    key: "types",
    parentKey: "meta",
    icon: "icon_increase",
    keepAlive: "false",
    order: 2,
  },
  {
    id: 5,
    title: "数据库",
    path: "/database",
    key: "database",
    parentKey: "mocker",
    icon: "icon_custom",
    keepAlive: "false",
    order: 5485,
  },
  {
    id: 6,
    title: "图标库",
    path: "/icons",
    key: "icons",
    parentKey: "",
    icon: "icon_pen",
    keepAlive: "true",
    order: 10,
  },
  {
    id: 6,
    title: "数据源管理",
    path: "/datasource",
    key: "datasource",
    parentKey: "",
    icon: "icon_wifi",
    keepAlive: "true",
    order: 10,
  },
];

export const getSideMenus = (): Promise<MenuResponse> => {
  return new Promise<MenuResponse>(function (resolve, reject) {
    resolve(menu);
  });
};
