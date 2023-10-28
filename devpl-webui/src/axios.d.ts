// 新建 axios.d.ts
import axios from "axios"

declare module "axios" {

  /**
   * 服务器返回数据的的类型，根据接口文档确定
   */
  interface Result<D = any | null> {
    /**
     * 状态码
     */
    code: number
    /**
     * 接口消息
     */
    msg: string
    /**
     * 数据类型
     */
    data: D | D[]
    /**
     * 总记录条数，用于接口返回为列表数据
     */
    total: number
    /**
     * 总记录条数，用于接口返回为列表数据
     */
    list: D[] // 分页结果返回
    /**
     * 异常调用栈，仅在开发环境有值
     */
    stackTrace: string
  }

  export interface AxiosResponse<T = any> extends Result<D> {}
}
