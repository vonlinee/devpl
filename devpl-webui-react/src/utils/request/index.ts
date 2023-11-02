/*
 * 服务器返回数据的的类型，根据接口文档确定
 **/
export interface Result<T = any> {
  code: number;
  message: string;
  data: T;
}

import axios from "axios";
import type { AxiosError, AxiosRequestConfig } from "axios";
import { message } from "antd";

import type {
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";

const service: AxiosInstance = axios.create({
  baseURL: "/api",
  timeout: 30000,
});

/* 请求拦截器 */
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    return config;
  },
  (error: AxiosError) => {
    message.error(error.message);
    return Promise.reject(error);
  }
);

/* 响应拦截器 */
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data;

    // 根据自定义错误码判断请求是否成功
    if (code === 0) {
      // 将组件用的数据返回
      return data;
    } else {
      // 处理业务错误。
      message.error(message);
      return Promise.reject(new Error(message));
    }
  },
  (error: AxiosError) => {
    // 处理 HTTP 网络错误
    let msg = "";
    // HTTP 状态码
    const status = error.response?.status;
    switch (status) {
      case 401:
        msg = "token 失效，请重新登录";
        // 这里可以触发退出的 action
        break;
      case 403:
        msg = "拒绝访问";
        break;
      case 404:
        msg = "请求地址错误";
        break;
      case 500:
        msg = "服务器故障";
        break;
      default:
        msg = "网络连接故障";
    }

    message.error(msg);
    return Promise.reject(error);
  }
);

// https://developer.aliyun.com/article/1058106
/* 导出封装的请求方法 */
export default {
  /**
   * GET请求
   * @param url 请求地址
   * @param config
   * @returns
   */
  get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, config);
  },

  post<T = any>(
    url: string,
    data?: object,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return service.post(url, data, config);
  },

  put<T = any>(
    url: string,
    data?: object,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return service.put(url, data, config);
  },

  delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, config);
  },
};
