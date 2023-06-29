import http from '@/utils/http'

/**
 * 上传单个文件
 * @param file
 */
export const apiUploadSingleFile = (file: File) => {
    return http.postForm('/api/file/upload/single', {
        param: {
            file: file
        }
    })
}
