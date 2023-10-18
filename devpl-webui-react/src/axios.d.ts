// 新建 axios.d.ts
import axios from 'axios'

declare type D = Record<string, any>

declare module 'axios' {
    interface IAxios<D = null> {
        code: number
        msg: string
        data: D
        stackTrace: string
    }

    export interface AxiosResponse<T = any> extends IAxios<D> {
    }
}
