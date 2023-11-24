import http from "@/utils/http";

/**
 * 生成代码（zip压缩包）
 * @param tableIds
 */
export const useDownloadApi = (tableIds: any[]): void => {
    location.href = import.meta.env.VITE_API_URL + '/api/codegen/download?tableIds=' + tableIds.join(',')
}

/**
 * 生成代码（自定义目录）
 * @param tableIds
 */
export const useGeneratorApi = (tableIds: any[]) => {
    return http.post('/api/codegen/code', tableIds)
}

/**
 * 查询所有生成的文件类型
 */
export const apiListGenFiles = () => {
    return http.get('/api/codegen/genfiles')
}

/**
 * 查询所有生成的文件类型
 * @return boolean
 */
export const apiSaveOrUpdateGenFile = (genFile: TargetGenFile) => {
    return http.post('/api/codegen/genfile', genFile)
}

/**
 * 保存或更新生成文件类型
 * @param genFiles
 */
export const apiSaveOrUpdateGenFiles = (genFiles: TargetGenFile[]) => {
    return http.post('/api/codegen/genfiles/replace', genFiles)
}

/**
 * 删除生成文件类型
 * @param genFiles
 */
export const apiDeleteGenFiles = (genFiles: TargetGenFile[]) => {
    return http.delete('/api/codegen/genfiles/replace', genFiles.map((file: TargetGenFile) => file.pid))
}

/**
 * 获取生成器配置
 * @return JSON字符串
 */
export const apiGetGeneratorConfig = () => {
    return http.get('/api/codegen/config')
}

/**
 * 生成代码（自定义目录）
 * @return JSON字符串
 */
export const apiSaveGeneratorConfig = (content: string) => {
    return http.post('/api/codegen/config', {
        content: content
    }, { "Content-Type": "multipart/form-data" })
}
