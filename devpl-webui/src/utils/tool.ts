import type {App, Plugin} from 'vue'

import {AES, enc, lib, mode, pad} from 'crypto-js'

/**
 * 全局组件安装
 * @param component
 * @param alias
 */
export const withInstall = <T>(component: T, alias?: string) => {
    const comp = component as any
    comp.install = (app: App): void => {
        app.component(comp.__name || comp.displayName, component)
        if (alias) {
            app.config.globalProperties[alias] = component
        }
    }
    return component as T & Plugin
}

// 密钥
const ENCRYPT_KEY: string = 'makulowcodeyyds1'

/**
 * 解密操作
 * @param ciphertext 密文
 */
export const decrypt = (ciphertext: string): string => {
    // 将密文转换为CipherParams对象
    const cipherParams: lib.CipherParams = lib.CipherParams.create({
        ciphertext: enc.Base64.parse(ciphertext)
    })
    // 使用密钥解密CipherParams对象
    const decrypted: lib.WordArray = AES.decrypt(cipherParams, enc.Utf8.parse(ENCRYPT_KEY), {
        mode: mode.ECB,
        padding: pad.Pkcs7
    })
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
    const encrypted: lib.CipherParams = AES.encrypt(message, enc.Utf8.parse(ENCRYPT_KEY), {
        mode: mode.ECB,
        padding: pad.Pkcs7
    })
    return encrypted.toString()
}

/**
 * 文本是否为空
 * @param content
 */
export const hasText = (content: any): boolean => {
    return typeof content == 'string' && content.length > 0;
}
