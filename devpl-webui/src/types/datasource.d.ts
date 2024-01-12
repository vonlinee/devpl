type TestConnVO = {
  /**
   * 是否失败
   */
  failed: boolean
  /**
   * 数据库类型
   */
  dbmsType: string
  /**
   * 是否使用ssl连接
   */
  useSsl: boolean
  /**
   * 连接失败时的错误信息
   */
  errorMsg: string
}
