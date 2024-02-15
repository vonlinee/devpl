/**
 * 声明全局成员
 * 可以直接在这里进行实现
 */

/**
 * 判断是否为空对象
 * @param val 值
 */
declare function isEmpty(val: any): boolean {
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

/**
 * 全局声明Window对象的方法
 */
declare interface Window {
  /**
   * 选择目录
   * 使用showDirectoryPicker方法时，浏览器会提示用户授权应用程序访问他们的文件系统，拒绝则无法进行访问
   * 详情见MDN文档: https://developer.mozilla.org/zh-CN/docs/Web/API/Window/showDirectoryPicker
   * @returns 
   */
  showDirectoryPicker: () => Promise<FileSystemDirectoryHandle>

  /**
   * 选择文件
   * https://developer.mozilla.org/zh-CN/docs/Web/API/Window/showOpenFilePicker
   * @param options 
   * @returns 
   */
  showOpenFilePicker: (options: {
    multiple?: boolean,
    excludeAcceptAllOption?: boolean,
    types: {
      description: string,
      accept: Object
    }[]
  }) => Promise<FileSystemFileHandle[]>

  /**
   * 全局工具方法
   */
  hasText: (val: any) => boolean
}
