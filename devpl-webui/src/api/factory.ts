import service from '@/utils/request'

// 获取文件目录树形结构
export function apiGetFileTree(root_path: string) {
    return service.get('/gen/generator/file-tree', {
        params: {
            rootPath: root_path
        }
    });
}

// 获取文件内容
export function apiGetFileContent(path: string) {
    return service.get('/gen/generator/file', {
        params: {
            path: path
        }
    });
}
