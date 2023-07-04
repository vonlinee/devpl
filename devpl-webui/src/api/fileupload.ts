import http from '@/utils/http'

/**
 * 上传单个文件
 * @param folder
 * @param file
 */
export const apiUploadSingleFile = (folder: string, file: File) => {
    return http.postForm('/api/file/upload/single', {
        folder: folder,
        filename: file.name,
        file: file
    })
}

export const apiUploadMultiFiles = (param: FormData) => {
    return http.postForm('/api/file/upload/single', param)
}
