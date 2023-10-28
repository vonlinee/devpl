import service from "./request"
import { AxiosPromise, AxiosRequestConfig } from "axios"

/**
 * axios请求配置: https://www.axios-http.cn/docs/req_config
 * @see AxiosRequestConfig
 */
interface RequestConfig extends AxiosRequestConfig {
  method: any
  url: string
  // headers?: object,
  // params?: object | string,
  // responseType?: string,
  // data?: object | string,
  // timeout?: number,
  [key: string]: any
}

/**
 * Http请求头
 */
interface HttpHeader extends Record<string, string> {
    'Content-Type': string
}

/**
 * 定义常用的请求方法
 */
const http = {
  getBody(
    url: string,
    params?: any,
    headers: HttpHeader = { "Content-Type": "application/json" }
  ) {
    const config: RequestConfig = {
      method: "get",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  /**
   * get请求
   * @param url
   * @param params
   */
  get<T = any>(url: string, params?: any): AxiosPromise<T> {
    const config: RequestConfig = {
      method: "get",
      url: url,
    }
    if (params) config.params = params
    return service(config)
  },
  blob(url: string, params?: any) {
    const config: RequestConfig = {
      method: "get",
      url: url,
      responseType: "blob",
    }
    if (params) config.params = params
    return service(config)
  },
  /**
   * 普通的post请求
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  post<T = any>(
    url: string,
    params?: any,
    headers: HttpHeader = { "Content-Type": "application/json" }
  ): AxiosPromise<T> {
    const config: RequestConfig = {
      method: "post",
      url: url,
      headers,
    }
    if (params) {
      config.data = params
    }
    return service(config)
  },
  postWWW(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/x-www-form-urlencoded" }
  ) {
    const config: RequestConfig = {
      method: "post",
      url: url,
      headers,
    }
    if (params) config.params = params
    return service(config)
  },
  /**
   * post请求，json格式
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  putJson(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/json" }
  ) {
    const config: RequestConfig = {
      method: "put",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  putWWW(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/x-www-form-urlencoded" }
  ) {
    const config: RequestConfig = {
      method: "put",
      url: url,
      headers,
    }
    if (params) config.params = params
    return service(config)
  },
  putQuery(url: string, params?: any) {
    const config: RequestConfig = {
      method: "put",
      url: url,
    }
    if (params) config.params = params
    return service(config)
  },
  delete(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/json" }
  ) {
    const config: RequestConfig = {
      method: "delete",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  deleteForm(
    url: string,
    params?: any,
    headers = { "Content-Type": "x-www-form-urlencoded" }
  ) {
    const config: RequestConfig = {
      method: "delete",
      url: url,
      headers,
    }
    if (params) {
      config.data = params
    }
    return service(config)
  },
  deleteQuery(url: string, params?: any) {
    const config: RequestConfig = {
      method: "delete",
      url: url,
    }
    if (params) config.params = params
    return service(config)
  },
  /**
   * 表单提交 multipart/form-data
   * @param url
   * @param params
   * @param headers
   */
  postForm(
    url: string,
    params?: any,
    headers = { "Content-Type": "multipart/form-data" }
  ) {
    const config: RequestConfig = {
      method: "post",
      url: url,
      headers,
    }
    if (params) {
      config.data = params
    }
    return service(config)
  },
  /**
   * POST请求JSON格式
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  postJson(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/json" }
  ) {
    const config: RequestConfig = {
      method: "post",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  formGet(
    url: string,
    params?: any,
    headers = { "Content-Type": "multipart/form-data" }
  ) {
    const config: RequestConfig = {
      method: "get",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  formPut(
    url: string,
    params?: any,
    headers = { "Content-Type": "multipart/form-data" }
  ) {
    const config: RequestConfig = {
      method: "put",
      url: url,
      headers,
    }
    if (params) config.data = params
    return service(config)
  },
  /**
   * 文件上传
   * @param url
   * @param params
   * @param headers
   * @param timeout
   * @returns
   */
  upload(
    url: string,
    params?: any,
    headers = { "Content-Type": "application/x-www-form-urlencoded" },
    timeout = 1200000
  ) {
    const config: RequestConfig = {
      method: "post",
      url: url,
      headers,
      timeout,
    }
    if (params) {
      config.data = params
    }
    return service(config)
  },
}

export default http
