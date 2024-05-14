const maxChunkSize = 52428800 // 最大容量块


function getFileHash(param: any) {

}

/**
 * 获取目录下的所有目录
 * @param file FileSystemHandle
 * @param pathSegments 
 * @param result 
 */
export async function listDirectories(file: any, pathSegments: string[], result: string[]) {
  try {
    if (file.kind === 'directory') {
      // 读取目录
      const entries = await file.values();
      let i = 0
      for await (const entry of entries) {
        i ++;
        pathSegments.push(entry.name)
        listDirectories(entry, pathSegments, result);
        pathSegments.pop()
      }
      if (i == 0) {
        // 空文件夹
        result.push(pathSegments.join('/'))
      }
    } else if (file.kind === 'file') {
      result.push(pathSegments.join('/'))
    } 
    console.log(pathSegments.join("/"));
  } catch (error) {
    console.error('无法获取目录:', error);
  }
}



















