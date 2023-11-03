import { MapKey } from "./api"

// 关于菜单State的action
export type MenuAction = {
  type: string
  keys: string[]
  menuItem: OpenedMenu
  list: MenuItem[],
  path: string
}

//
export interface OpenedMenu {
  key: string
  path: string
  title: string
}

export interface MenuState {
  openedMenu: OpenedMenu[]
  openMenuKey: string[]
  selectMenuKey: string[]
  menuList: MenuItem[],
  currentPath: string
}

// 未处理的菜单列表信息
export interface MenuItem {
  menu_id: number
  icon: string
  /**
   * 是否 keepAlive
   */
  keepAlive: string
  key: string | number
  order?: number
  parentKey: string
  /**
   * 菜单路径不需要加上父菜单的路径
   */
  path: string
  /**
   * 标题
   */
  title: string
  children?: MenuList
  parentPath?: string
  isShowOnMenu?: boolean | string
  [key: string]: any
}

export type MenuMap = {
  [key: string]: {
    children: MenuItem[]
  } | MenuItem
} | {
  [key: string]: MenuItem
}

export type MenuList = MenuItem[]

export type MenuResponse = MenuList

export type MenuListResponse = {
  data: MenuList,
  mapKey: MapKey
}
