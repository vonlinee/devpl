// 新建 axios.d.ts
import axios from "axios"

declare module "axios" {
  interface IAxios<D = any | null> {
    code: number
    msg: string
    data: D
    total: number
    list: D[] // 分页结果返回
    stackTrace: string
  }

  export interface AxiosResponse<T = any> extends IAxios<D> {}
}
