import { MenuItem, OpenedMenu } from "@/types";
import { useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setOpenKey, setSelectKey, addOpenedMenu, filterOpenKey, setCurrentPath, setUserMenu } from "../action";
import { getStateCurrentPath, getStateOpenMenuKey, getStateMenuList, getStateOpenMenu, getStateSelectMenuKey } from "../getter";

export const useStateSelectMenuKey = () => useSelector(getStateSelectMenuKey)
export const useStateOpenMenuKey = () => useSelector(getStateOpenMenuKey)
export const useStateOpenedMenu = () => useSelector(getStateOpenMenu)
export const useStateCurrentPath = () => useSelector(getStateCurrentPath)
export const useStateMenuList = () => useSelector(getStateMenuList)

/**
 * 菜单相关状态操作
 * @returns 
 */
export function useDispatchMenu() {
  const dispatch = useDispatch()

  /**
   * 设置当前打开的菜单
   */
  const stateSetOpenMenuKey = useCallback((keys: string[]) => dispatch(setOpenKey(keys)), [dispatch])
  const stateSetSelectMenuKey = useCallback((keys: string[]) => dispatch(setSelectKey(keys)), [dispatch])
  const stateFilterOpenMenuKey = useCallback((key: string[]) => dispatch(filterOpenKey(key)), [dispatch])
  const stateAddOpenedMenu = useCallback((menuItem: OpenedMenu) => dispatch(addOpenedMenu(menuItem)), [dispatch])
  /**
   * 设置所有菜单
   */
  const stateSetMenuList = useCallback((list: MenuItem[]) => dispatch(setUserMenu(list)), [dispatch])
  /**
   * 设置当前的路径
   */
  const stateSetCurrentPath = useCallback((path: string) => dispatch(setCurrentPath(path)), [dispatch])
  return {
    stateSetOpenMenuKey,
    stateSetSelectMenuKey,
    stateFilterOpenMenuKey,
    stateAddOpenedMenu,
    stateSetMenuList,
    stateSetCurrentPath,
  }
}