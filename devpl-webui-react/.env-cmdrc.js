/**
 * 环境变量配置
 */

/**
 * 开发环境配置
 */
const dev = {
  REACT_APP_ROUTERBASE: "/devpl",
  REACT_APP_API_BASEURL: "http://localhost:8088/devpl",
  REACT_APP_MODE: "development",
}

// 是否是hash路由
const REACT_APP_ROUTER_ISHASH = "1"

/**
 * 生产环境
 */
const pro = {
  REACT_APP_ROUTERBASE: "/devpl",
  REACT_APP_API_BASEURL: "http://localhost:8088/devpl",
  REACT_APP_MODE: "production"
}

// 接口是否通过Mock返回
const REACT_APP_MOCK = "1"
const REACT_APP_COLOR = "1"

module.exports = Promise.resolve({
  dev: dev,
  dev_mock: {
    ...dev,
    REACT_APP_MOCK
  },
  dev_color: {
    ...dev,
    REACT_APP_COLOR
  },
  build: pro,
  build_color: {
    ...pro,
    REACT_APP_COLOR
  }
})