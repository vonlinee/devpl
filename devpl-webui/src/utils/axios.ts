import axios, {AxiosInstance} from "axios";
import {ElMessage} from "element-plus";
import {h} from 'vue'
import qs from "qs";

interface config {
    [key: string]: any
}

// 设置默认的请求头数据格式，以json格式传输与接收
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.get["Accept"] = "application/json";
axios.defaults.withCredentials = false;

/**
 * 创建axios实例
 */
export const http: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL as any,
    timeout: 10000,
})

/**
 * 错误警告
 * @param content
 * @param type
 */
const errorAlert = (content: string, type: any = 'error') => {
    ElMessage({
        message: h('p', null, [
            h('span', null, content),
            h('i', {style: 'color: teal'}, 'VNode'),
            h('button', {
                text: 'Show',
                width: '200px'
            })
        ]),
    })
}

/**
 * 请求拦截器
 */
http.interceptors.request.use(
    (config: config) => {
        // console.log(config)
        // 发请求前做的一些处理
        // 此处判断请求的接口请求头中是否携带Authorization  还需要知道是管理端接口还是门户端接口
        // if (window.location.hash !== "#/" && !sessionStorage.getItem("store")) {
        //   window.location.hash = "#/"
        //   window.location.reload()
        // }
        // if ((config.method !== "post" && config.url === "/user/pwd")) {
        //   config.headers["Authorization"] = sessionStorage.getItem("Token");
        // }
        if (config.headers["Content-Type"] === "application/json") {
            config.data = JSON.stringify(config.data);
        }
        config.headers["Authorization"] = sessionStorage.getItem("PsnMgrToken");

        // 追加时间戳，防止GET请求缓存
        if (config.method?.toUpperCase() == 'GET') {
            config.params = {...config.params, t: new Date().getTime()}
        }
        // 表单
        if (Object.values(config.headers).includes('application/x-www-form-urlencoded')) {
            config.data = qs.stringify(config.data)
        }
        return config;
    },
    (error) => {
        Promise.reject(error).then(result => console.log("Rejected Request => ", result));
    })

/**
 * 响应拦截器
 */
http.interceptors.response.use(
    (response) => {
        // 判断url是否为带端口号的网址
        const reg = /^((ht|f)tps?:\/\/)?[\w-]+(\.[\w-]+)+:\d{1,5}\/?/;
        if (response.data.code === 401) {
            errorAlert("用户身份已过期，请重新登录");
            window.location.hash = "#/"
            window.location.reload()
        }
        if (response.data.code == 500) {
            errorAlert(response.data.data);
        }
        // 判断当前该用户无权限的情况，不弹出提示消息
        if (response.data.code == 416 && reg.test(response.data.data)) {
            return response.data;
        }
        if (response.data.code == 416) {
            errorAlert(response.data.data || response.data.msg, 'warning');
        }
        return response.data;
    },
    (error) => {
        // 捕获状态码，进行公共错误处理
        if (error && error.response) {
            switch (error.response.status) {
                case 400:
                    errorAlert("错误请求");
                    break;
                case 401:
                    errorAlert("未授权");
                    break;
                case 402:
                    errorAlert("安全验证失败");
                    break;
                case 403:
                    errorAlert("拒绝访问");
                    break;
                case 404:
                    errorAlert("资源不存在或已被删除");
                    break;
                case 405:
                    errorAlert("请求方法未允许");
                    break;
                case 406:
                    errorAlert("请求方法错误");
                    break;
                case 408:
                    errorAlert("请求超时");
                    break;
                case 416:
                    errorAlert("传参不符合要求", 'warning');
                    break;
                case 500:
                    errorAlert("服务器异常");
                    break;
                case 501:
                    errorAlert("网络未实现");
                    break;
                case 502:
                    errorAlert("网络错误");
                    break;
                case 503:
                    errorAlert("服务不可用");
                    break;
                case 504:
                    errorAlert("网络超时");
                    break;
                case 505:
                    errorAlert("http版本不支持该请求");
                    break;
                case 601:
                    errorAlert("系统创建文件夹异常");
                    break;
                case 602:
                    errorAlert("系统上传文件异常");
                    break;
                case 603:
                    errorAlert("系统IO流异常");
                    break;
                default:
                    errorAlert(`连接错误${error.response.status}`);
            }
        } else {
            // 超时处理
            if (JSON.stringify(error).includes("timeout")) {
                errorAlert("服务器响应超时，请刷新当前页");
            } else {
                errorAlert("连接服务器失败");
            }
        }
        // 如果不需要错误处理，以上的处理过程都可省略
        return Promise.resolve(error.response);
    }
);
