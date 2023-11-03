import auto from "./auto";
import { Navigate } from "react-router-dom";
import Error from "@/pages/err"
import Index from "@/pages"
import loadable from "@loadable/component"
import { Spin } from "antd";
import React from "react";

type LoadingComponent = () => Promise<React.ReactNode>

/**
 * 单个路由信息
 */
export interface RouterInfo {
  components: LoadingComponent | React.ReactNode
  [MENU_PATH]: string
  [MENU_KEY]?: any
  [MENU_TITLE]?: string | any
  [MENU_KEEPALIVE]?: string | any
  [name: string]: any
}

/**
 * 默认路由信息
 */
const defaultArr: RouterInfo[] = [
  {
    [MENU_PATH]: "/",
    [MENU_KEY]: "index",
    components: <Navigate to="/index" replace />,
  },
  {
    [MENU_PATH]: "/index",
    [MENU_KEY]: "index",
    components: <Index />,
  },
  {
    [MENU_PATH]: "/result/404",
    components: <Error />,
  },
  {
    [MENU_PATH]: "/result/403",
    components: <Error status="403" errTitle="403" subTitle="Sorry, you don't have access to this page." />,
  },
  {
    [MENU_PATH]: "/result/500",
    components: <Error status="500" errTitle="500" subTitle="Sorry, the server is reporting an error." />,
  },
  {
    [MENU_PATH]: "*",
    [MENU_TITLE]: "页面不存在",
    [MENU_KEY]: "404",
    components: <Error />,
  },
];
const autoList: RouterInfo[] = []
const fellbackStyle = {
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  minHeight: 500,
  fontSize: 24,
};

console.log("自动路由", auto);

auto.forEach(item => {
  const { components, ...anyProps } = item
  const Com = loadable(item.components, { fallback: <Spin style={fellbackStyle} tip="页面加载中...." /> })
  const info = { ...anyProps, components: <Com /> }
  autoList.push(info)
})
const list: RouterInfo[] = [...autoList, ...defaultArr]

export default list;
