import { MapKey } from "./api"

// 关于菜单State的action
export type MenuAction = {
  type: string
  keys: string[]
  menuItem: OpenedMenu
  list: MenuItem[],
  path: string
}

/**
 * 当前打开的菜单
 */
export interface OpenedMenu {
  key: string
  path: string
  title: string
}

/**
 * 菜单状态
 */
export interface MenuState {
  openedMenu: OpenedMenu[]
  openMenuKey: string[]
  selectMenuKey: string[]
  menuList: MenuItem[],
  currentPath: string
}

// 未处理的菜单列表信息
export interface MenuItem {
  /**
   * 唯一菜单id
   */
  id: number
  /**
   * 图标
   */
  icon: string
  /**
   * 是否 keepAlive
   */
  keepAlive: string
  /**
   * 菜单的key
   */
  key: string | number
  /**
   * 排序号
   */
  order?: number
  /**
   * 根据父节点的key将菜单转成树形结构
   */
  parentKey: string
  /**
   * 如果是子菜单，菜单路径不需要加上父菜单的路径，需要parentKey指定正确
   */
  path: string
  /**
   * 标题
   */
  title: string
  /**
   * 子菜单节点
   */
  children?: MenuList
  /**
   * 父节点的path
   */
  parentPath?: string
  /**
   * 是否展示菜单
   */
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
