import { useEffect, useMemo, useState } from "react";
import { Routes, Route } from "react-router-dom";
import routerList, { RouterInfo } from "./list";
import Intercept from "./intercept";
import { getMenus } from "@/common";
import { formatMenu, reduceMenuList } from "@/utils";
import { MenuItem, MenuList } from "@/types";
import { useDispatchMenu } from "@/store/hooks";

/**
 * 路由组件
 * @returns
 */
const Router = () => {
  const { stateSetMenuList } = useDispatchMenu();
  const [mergeRouterList, setMergeList] = useState<RouterInfo[]>([]); // 本地 和 接口返回的路由列表 合并的结果
  const [ajaxUserMenuList, setAjaxUserMenuList] = useState<MenuList>([]); // 网络请求回来的 路由列表

  useEffect(() => {
    if (stateSetMenuList && typeof stateSetMenuList === "function") {
      // 加载所有菜单路由
      getMenus().then((list: MenuList) => {
        // 所有顶级父菜单
        const formatList: MenuList = formatMenu(list);
        // 所有菜单
        const userMenus: MenuList = reduceMenuList(formatList);

        const isMenuMathcesRouter = (m: MenuItem, r: RouterInfo): boolean => {
          return (m.parentPath || "") + m.path === r.path;
        };

        // 把请求的数据 和 本地pages页面暴露出的路由列表合并
        const routers: RouterInfo[] = routerList.map((router: RouterInfo) => {
          let find = userMenus.find((i: MenuItem) =>
            isMenuMathcesRouter(i, router)
          );
          if (find) {
            router = { ...find, ...router }; // 本地 优先 接口结果
          } else {
            router.key = router.path;
          }
          return router;
        });

        if (list && list.length) {
          stateSetMenuList(formatList);
          setAjaxUserMenuList(userMenus);
          setMergeList(routers);
        }
      });
    }
  }, [stateSetMenuList]);

  const routerBody = useMemo(() => {
    // 监听 本地路由列表 同时存在长度大于1时 渲染路由组件
    if (mergeRouterList.length) {
      return mergeRouterList.map((item) => {
        return (
          <Route
            key={item.key}
            path={item.path}
            element={
              <Intercept
                {...item}
                menuList={ajaxUserMenuList}
                pageKey={item.key}
              />
            }
          />
        );
      });
    }
  }, [ajaxUserMenuList, mergeRouterList]);

  return <Routes>{routerBody}</Routes>;
};

export default Router;
