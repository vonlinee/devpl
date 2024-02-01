
/**
 * 声明全局成员
 * 可以直接在这里进行实现
 */

/**
 * 判断是否为空对象
 * @param val 值
 */
declare function isEmpty(val: any): boolean {
  console.log("global function")

  if (val == undefined || val == null) {
    return true
  }
  if (Array.isArray(val)) {
    return val.length == 0
  }
  if (typeof val == 'string') {
    return val.length == 0
  }
  return false
}
