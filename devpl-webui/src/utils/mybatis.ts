



/**
 * 生成foreach标签
 * @param paramName 
 * @param columnName 
 * @returns 
 */
export const foreachSnippet = (paramName: string, columnName: string) => {
    return `<if test="${paramName} != null and ${paramName}.size() > 0">
    AND ${columnName} IN
        <foreach item="item" index="index" collection="${paramName}" open="(" close=")" separator=",">
            #{item}
        </foreach>
    </if>`
}

/**
 * 针对单个字段生成test标签
 * @param paramName 
 * @param columnName 
 * @returns 
 */
export const stringSnippet = (paramName: string, columnName: string) => {
    return `<if test="${paramName} != null and ${paramName}.size() > 0">\n
    AND ${columnName} = #{${paramName}}
</if>`
}









