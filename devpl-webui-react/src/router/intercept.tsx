import { useCallback, useEffect, useMemo, useState } from "react";
import { getMenuParentKey } from "@/utils";
import Error from "@/pages/err";
import { useLocation } from "react-router-dom";
import { LayoutMode, MenuItem } from "@/types";
import { useDispatchLayout, useDispatchMenu } from "@/store/hooks";

const scrollPage = () => {
  window.scrollTo({
    top: 0,
    left: 0,
    behavior: "smooth",
  });
};

interface RouteInterceptorProps {
  path?: string;
  title?: string;
  pageKey: string;
  menuList: MenuItem[];
  layout?: LayoutMode;
  [key: string]: any;
}

/**
 * 路由拦截器
 * @param param0
 * @returns
 */
function Intercept({
  menuList,
  components,
  title,
  path: pagePath,
  pageKey,
  layout,
}: RouteInterceptorProps) {
  const [pageInit, setPageInit] = useState(false);
  const location = useLocation();
  const {
    stateSetOpenMenuKey,
    stateSetSelectMenuKey,
    stateAddOpenedMenu,
    stateSetCurrentPath,
  } = useDispatchMenu();
  const { stateChangeLayout } = useDispatchLayout();
  const currentPath = useMemo(() => {
    const { pathname, search } = location;
    return pathname + search;
  }, [location]);

  // 监听 location 改变
  const onPathChange = useCallback(() => {
    stateSetCurrentPath(currentPath);
    stateAddOpenedMenu({
      key: pageKey,
      path: currentPath,
      title: title || "未设置标题信息",
    });
  }, [currentPath, pageKey, title, stateSetCurrentPath, stateAddOpenedMenu]);

  const setCurrentPageInfo = useCallback(() => {
    if (!title) {
      return;
    }
    document.title = title;
    stateSetSelectMenuKey([String(pageKey)]);
    let openkey = getMenuParentKey(menuList, pageKey);
    stateSetOpenMenuKey(openkey);
    stateAddOpenedMenu({ key: pageKey, path: currentPath, title });
  }, [
    currentPath,
    menuList,
    title,
    pageKey,
    stateSetOpenMenuKey,
    stateSetSelectMenuKey,
    stateAddOpenedMenu,
  ]);

  const init = useCallback(() => {
    setCurrentPageInfo();
    scrollPage();
  }, [setCurrentPageInfo]);

  useEffect(() => {
    if (init && !pageInit) {
      init();
      setPageInit(true);
    }
  }, [init, pageInit]);

  useEffect(() => {
    if (pageInit) {
      onPathChange();
    }
  }, [onPathChange, pageInit]);

  if (layout) {
    // 切换布局
    useEffect(() => {
      console.log("切换布局 ", layout);
      layout && stateChangeLayout("push", layout);
    }, [layout]);
  }

  const hasPath = !menuList.find(
    (m: MenuItem) => (m.parentPath || "") + m.path === pagePath
  );

  if (
    hasPath &&
    pagePath !== "/" &&
    pagePath !== "*" &&
    pagePath !== "/index"
  ) {
    return (
      <Error
        status="403"
        errTitle="权限不够"
        subTitle="Sorry, you are not authorized to access this page."
      />
    );
  }
  return components;
}
export default Intercept;
