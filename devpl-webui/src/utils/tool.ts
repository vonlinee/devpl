import type { App, Component, Plugin } from "vue"

import { AES, enc, lib, mode, pad } from "crypto-js"

/**
 * 全局组件安装
 * @param component
 * @param alias
 */
export const withInstall = <T>(component: T, alias?: string) => {
  const comp = component as any
  comp.install = (app: App): void => {
    app.component(comp.__name || comp.displayName, component as Component)
    if (alias) {
      app.config.globalProperties[alias] = component
    }
  }
  return component as T & Plugin
}

// 密钥
const ENCRYPT_KEY: string = "devpl11235813213"

/**
 * 解密操作
 * @param ciphertext 密文
 */
export const decrypt = (ciphertext: string): string => {
  // 将密文转换为CipherParams对象
  const cipherParams: lib.CipherParams = lib.CipherParams.create({
    ciphertext: enc.Base64.parse(ciphertext),
  })
  // 使用密钥解密CipherParams对象
  const decrypted: lib.WordArray = AES.decrypt(
    cipherParams,
    enc.Utf8.parse(ENCRYPT_KEY),
    {
      mode: mode.ECB,
      padding: pad.Pkcs7,
    }
  )
  // 获取明文
  return decrypted.toString(enc.Utf8)
}

/**
 * 加密操作
 * @param plaintext 明文
 */
export const encrypt = (plaintext: string): string => {
  // 将明文转换为要加密的格式
  const message: lib.WordArray = enc.Utf8.parse(plaintext)
  // 使用密钥加密明文
  const encrypted: lib.CipherParams = AES.encrypt(
    message,
    enc.Utf8.parse(ENCRYPT_KEY),
    {
      mode: mode.ECB,
      padding: pad.Pkcs7,
    }
  )
  return encrypted.toString()
}

/**
 * 文本是否为空
 * @param content any, 字符串
 */
export const hasText = (content: any): boolean => {
  return typeof content == "string" && !isBlank(content)
}

/**
 * 所有字符串都不为空
 * @param contents
 */
export const allHasText = (...contents: (string | undefined)[]): boolean => {
  let res = true
  for (let i = 0; i < contents.length; i++) {
    if (!hasText(contents[i])) {
      res = false
      break
    }
  }
  return res
}

/**
 * 文本是否全由空格组成，至少包含1个字符
 * @param content
 */
export const isBlank = (content: string | undefined | any): boolean => {
  if (typeof content === "string") {
    if (content.length == 0) {
      return true
    }
    for (let i: number = 0; i < content.length; i++) {
      if (content[i] !== " ") {
        return false
      }
    }
  } else if (typeof content === "object") {
    return content === null || content == undefined
  }
  return true
}

/**
 * 所有字符a-zA-Z
 * @param str
 */
export function isAllLetter(str: string): boolean {
  return /[a-zA-Z]/.test(str)
}

export const isWindows = () => {
  return navigator.userAgent.match(/Windows/i) !== null
}

export const isMacintosh = () => {
  return navigator.userAgent.match(/Macintosh/i) !== null
}

/**
 * 深拷贝对象
 * @param obj
 * @returns
 */
export const deepClone = (obj: Record<string, any>) => {
  const copy: any = Object.assign({}, obj)
  Object.keys(copy).forEach(
    (key) =>
    (copy[key] =
      typeof obj[key] === "object" ? deepClone(obj[key]) : obj[key])
  )
  return Array.isArray(obj)
    ? (copy.length = obj.length) && Array.from(copy)
    : copy
}

export function getIconName(data: FileNode): string {
  if (!data.leaf) {
    return "folder"
  }
  if (!data.extension) {
    return "file"
  }
  let iconName: string
  switch (data.extension) {
    case "java":
      iconName = "java"
      break
    case "ts":
      iconName = "ts"
      break
    case "vue":
      iconName = "vue"
      break
    case "sql":
      iconName = "sql"
      break
    case "txt":
      iconName = "txt"
      break
    case "html":
      iconName = "html"
      break
    case "xml":
      iconName = "xml"
      break
    default:
      iconName = "file"
  }
  return iconName
}

/**
 * 数组a-数组b
 * @param a
 * @param b
 */
export const sub = (a: any[], b: any[]) => {
  return a.filter(function (item) {
    return b.indexOf(item) < 0
  })
}

/**
 * 从至少一个空格分割的字符串中提取出所有子串
 * 例如 let str = "子串1    子串2              子串3";
 * 结果: ["子串1", "子串2", "子串3"]
 * @param input
 */
export const getSubStrings = (input: string) => {
  let substrings: string[] = input.split(" ")
  let result = []
  for (let i = 0; i < substrings.length; i++) {
    if (!isBlank(substrings[i])) {
      result.push(substrings[i])
    }
  }
  return result
}

// 挂载全局方法到window对象上
window.hasText = (val: any): boolean => {
  return !isBlank(val)
}

/**
 * 数组去重
 * @param arr 
 * @returns 
 */
export const removeDuplicate = (arr: any[]) => {
  return Array.from(new Set(arr))
}

/**
 * 数组添加元素
 * @param arr 1
 * @param arr 2
 * @returns 返回数组1
 */
export const addAll = <T>(arr1: T[], arr2?: T[]): T[] => {
  if (arr2 == undefined || arr2.length == 0) {
    return arr1;
  }
  for (let i = 0; i < arr2.length; i++) {
    const element = arr2[i];
    arr1.push(element)
  }
  return arr1
}

/**
 * 复制对象
 * @param copyFrom 
 * @param copyTo 
 * @returns 
 */
export function copyObject(copyFrom: Record<any, any>, copyTo: Record<any, any>): Record<any, any> {
  const keysTo = Object.keys(copyTo);
  for (const key of keysTo) {
    if (copyFrom[key] !== undefined) {
      copyTo[key] = copyFrom[key];
    }
  }
  return copyTo;
}

/**
 * Base64编码
 * @param content 文本
 * @returns 
 */
export function base64Encode(content: string) {
  var words = enc.Utf8.parse(content); // WordArray object
  var base64 = enc.Base64.stringify(words);
  return base64;
}