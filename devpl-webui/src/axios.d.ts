// 新建 axios.d.ts
import axios from 'axios'

declare module 'axios' {
    interface IAxios<D = null> {
        code: number
        msg: string
        data: D
        list: any[] // 分页结果返回
        stackTrace: string
    }

    export interface AxiosResponse<T = any> extends IAxios<D> {
    }
}

