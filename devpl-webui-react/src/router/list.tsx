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
  path: string
  key?: any
  title?: string | any
  keepAlive?: string | any
  [name: string]: any
}

/**
 * 默认路由信息
 */
const defaultArr: RouterInfo[] = [
  {
    path: "/",
    key: "index",
    components: <Navigate to="/index" replace />,
  },
  {
    path: "/index",
    key: "index",
    components: <Index />,
  },
  {
    path: "/result/404",
    components: <Error />,
  },
  {
    path: "/result/403",
    components: <Error status="403" errTitle="403" subTitle="Sorry, you don't have access to this page." />,
  },
  {
    path: "/result/500",
    components: <Error status="500" errTitle="500" subTitle="Sorry, the server is reporting an error." />,
  },
  {
    path: "*",
    title: "页面不存在",
    key: "404",
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

auto.forEach(item => {
  const { components, ...anyProps } = item
  const Com = loadable(item.components, { fallback: <Spin style={fellbackStyle} tip="页面加载中...." /> })
  const info = { ...anyProps, components: <Com /> }
  autoList.push(info)
})

// 将auto.tsx中的路由信息和这里默认添加的路由信息合并
const list: RouterInfo[] = [...autoList, ...defaultArr]

export default list;
