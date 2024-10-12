// 1xx：表示请求已被接收，需要继续处理。
// 2xx：表示请求已成功被服务器接收、理解、并接受。
// 3xx：重定向，需要客户端采取进一步的操作才能完成请求。
// 4xx：客户端错误，表示请求包含语法错误或者无法完成请求。
// 5xx：服务器错误，服务器在处理请求的过程中发生了错误。

export enum HttpStatus {
  OK = 200,
}

/**
 * 响应状态枚举
 */
export enum ResponseStatus {
  TIMEOUT = 20000,
  OVERDUE = 600, // 登录失效
  FAIL = 999, // 请求失败
  SUCCESS = 2000, // 请求成功，不使用200,与HTTP状态码区分开
}

/**
 * 用于 Get 请求
 */
export interface GetRequestParam {
  // 可选参数，表示当前页码
  page?: number;
  // 可选参数，表示每页的记录数
  limit?: number;
  // 索引签名，表示其他字段
  [key: string]: any;
}
