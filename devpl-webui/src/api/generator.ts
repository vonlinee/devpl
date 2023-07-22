import service from '@/utils/request'

import http from "@/utils/http";

/**
 * 生成代码（zip压缩包）
 * @param tableIds
 */
export const useDownloadApi = (tableIds: any[]): void => {
    location.href = import.meta.env.VITE_API_URL + '/gen/generator/download?tableIds=' + tableIds.join(',')
}

/**
 * 生成代码（自定义目录）
 * @param tableIds
 */
export const useGeneratorApi = (tableIds: any[]) => {
    return service.post('/gen/generator/code', tableIds)
}

/**
 * 查询所有生成的文件类型
 */
export const apiListGenFiles = () => {
    return http.get('/gen/generator/genfiles')
}

/**
 * 保存或更新生成文件类型
 * @param genFiles
 */
export const apiSaveOrUpdateGenFiles = (genFiles: GenFile[]) => {
    return http.postJson('/gen/generator/genfiles/replace', genFiles)
}
