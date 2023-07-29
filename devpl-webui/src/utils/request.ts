import axios, {AxiosInstance} from 'axios'
import qs from 'qs'
import {ElMessage} from 'element-plus'

// axios实例
const service: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL as any,
    timeout: 60000,
})

// 请求拦截器
service.interceptors.request.use(
    (config: any) => {
        // 追加时间戳，防止GET请求缓存
        if (config.method?.toUpperCase() === 'GET') {
            config.params = {...config.params, t: new Date().getTime()}
        }

        // 表单
        if (Object.values(config.headers).includes('application/x-www-form-urlencoded')) {
            config.data = qs.stringify(config.data)
        }

        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        if (response.status !== 200) {
            return Promise.reject(new Error(response.statusText || 'Error'))
        }
        const res = response.data
        // 响应成功 200
        if (res.code === 200) {
            return res
        }
        // 开发模式下展示异常
        // if (res.stackTrace != null || res.stackTrace != undefined) {
        //     handleOpenDialog(response.request.responseURL, res.stackTrace)
        // }
        // 错误提示 通过自定义弹窗方式进行显示
        ElMessage.error(res.msg)
        return Promise.reject(new Error(res.msg || 'Error'))
    },
    error => {
        ElMessage.error(error.message)
        return Promise.reject(error)
    }
)

// 导出 axios 实例
export default service
