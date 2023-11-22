
/**
 * 文件节点
 */
type FileNode = {
  path: string,
  key: string,
  label: string,
  isLeaf: boolean,
  selectable: boolean,
  extension: string, // 文件后缀名
  children: FileNode[]
}