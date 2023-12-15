/**
 * 字段节点
 */
type FieldTreeNode {
    /**
     * 唯一ID
     */
    id: number
    /**
     * 字段名称
     */
    label: string
    /**
     * 是否正处于编辑状态
     */
    editing?: boolean
    /**
     * 数据类型
     */
    dataType?: string
    /**
     * 子节点
     */
    children?: FieldTreeNode[]
}