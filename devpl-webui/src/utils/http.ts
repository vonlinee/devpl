import axios, {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  CreateAxiosDefaults,
  InternalAxiosRequestConfig,
} from "axios"
import { ElMessage } from "element-plus"
import qs from "qs"

/**
 * 响应状态枚举
 */
enum ResponseStatus {
  TIMEOUT = 20000,
  OVERDUE = 600, // 登录失效
  FAIL = 999, // 请求失败
  SUCCESS = 2000, // 请求成功，不使用200,与HTTP状态码区分开
}

/**
 * 后端接口响应数据结构
 * @param T 响应携带的数据类型，即data的类型
 */
export interface Response<T = Record<string, any>> {
  /**
   * 接口响应状态码，和HTTP状态码区分开
   */
  code: number | string
  /**
   * 接口返回消息
   */
  msg: string
  /**
   * 用于提示的消息，由后端定义
   */
  toast: string
  /**
   * 总条数，仅当返回数据为列表时有值
   */
  total?: number
  /**
   * 接口返回的数据，T或者T[]，需要调用方进行指定
   */
  data?: T
  /**
   * 异常调用栈，仅在开发阶段使用，用于调试
   */
  stackTrace?: string
  /**
   * 时间戳
   */
  timestamp: number | string
}

// 请求数据，可能放在请求体里也可能放在请求参数里
type RequestData = Record<string, any> | string
// 请求参数
type RequestParam = Record<string, any> | string | any[]

/**
 * Http请求头
 */
export interface HttpHeader extends Record<string, string> {
  "Content-Type": string
}

/**
 * 不能写成AxiosPromise<T>，否则调用方的then接收到的类型是AxiosResponse而不是Response
 */
export type ResponsePromise<T> = Promise<Response<T>>

class Http {
  /**
   * Axios实例
   */
  instance: AxiosInstance

  constructor(config: CreateAxiosDefaults) {
    this.instance = axios.create(config)

    // 请求拦截器
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // GET请求追加时间戳，防止GET请求缓存
        if (config.method?.toUpperCase() === "GET" && config.params) {
          config.params.timestamp = new Date().getTime()
        }

        // 表单
        if (
          Object.values(config.headers).includes(
            "application/x-www-form-urlencoded"
          )
        ) {
          config.data = qs.stringify(config.data)
        }
        return config
      },
      (error: AxiosError) => {
        return Promise.reject(error)
      }
    )

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response : AxiosResponse) => {
        if (response.status !== 200) {
          return Promise.reject(new Error(response.statusText || "Error"))
        }
        const res = response.data
        // 响应成功 200
        if (res.code === ResponseStatus.SUCCESS) {
          return res
        }
        // 错误提示 通过自定义弹窗方式进行显示
        // showException(res.stackTrace)
        ElMessage.error(res.msg)
        return Promise.reject(new Error(res.msg || "Error"))
      },
      (error) => {
        ElMessage.error(error.message)
        return Promise.reject(error)
      }
    )
  }

  /**
   * 普通的get请求
   * @param url
   * @param params
   */
  get<T = any>(url: string, params?: RequestData): ResponsePromise<T> {
    return this.instance.get(url, { params: params })
  }

  /**
   * POST请求，JSON格式
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  post<T = any>(
    url: string,
    params?: RequestParam,
    headers: HttpHeader = { "Content-Type": "application/json" }
  ): ResponsePromise<T> {
    return this.instance.post(url, params, { headers })
  }

  /**
   * PUT请求，JSON格式
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  put<T = any>(
    url: string,
    params?: RequestParam,
    headers: HttpHeader = { "Content-Type": "application/json" }
  ): ResponsePromise<T> {
    return this.instance.put(url, params, { headers })
  }

  /**
   * DELETE请求，JSON格式
   * @param url
   * @param params
   * @param headers
   * @returns
   */
  delete<T = any>(
    url: string,
    params?: RequestParam,
    headers: HttpHeader = { "Content-Type": "application/json" }
  ): ResponsePromise<T> {
    return this.instance.delete(url, { params, headers })
  }

  /**
   * 表单提交 multipart/form-data
   * @param url
   * @param params
   * @param headers
   */
  form<T = any>(
    url: string,
    params?: RequestParam,
    headers: HttpHeader = { "Content-Type": "multipart/form-data" }
  ): ResponsePromise<T> {
    return this.instance.post(url, params, { headers })
  }
}

/**
 * 全局请求配置
 */
const config: CreateAxiosDefaults = {
  /**
   * 接口基础地址
   */
  baseURL: import.meta.env.VITE_API_URL,
  /**
   * 超时时间，毫秒
   */
  timeout: 6000,
}

const http = new Http(config)
export default http
