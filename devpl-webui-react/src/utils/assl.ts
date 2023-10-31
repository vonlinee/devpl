import request from "@/utils/request/index";

/**
 * 接口参数类型
 */
interface TestParams {
  b: number
}

/** 接口返回值类型 */
interface TestResponse {
  a: string
}

// ------------------------------ 常规使用 ------------------------------

// 常规使用1：默认返回提取过的数据
request.get<TestResponse>('/api/test').then((res) => {

})
