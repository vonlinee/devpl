package ${package}.${moduleName}.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.*;
<#list importList as i>
import ${i!};
</#list>
<#if baseClass??>
import ${baseClass.packageName}.${baseClass.code};
</#if>

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${date}
*/
<#if baseClass??>@EqualsAndHashCode(callSuper=false)</#if>
@Getter
@Setter
@TableName(value = "${tableName}")
public class ${ClassName}Entity<#if baseClass??> extends ${baseClass.code}</#if> {
<#list fieldList as field>
<#if !field.baseField>
    <#if field.fieldComment!?length gt 0>
    /**
     * ${field.fieldComment}
     */
    </#if>
    <#if field.autoFill == "INSERT">
    @TableField(value = "${field.fieldName}", fill = FieldFill.INSERT)
    <#elseif field.autoFill == "INSERT_UPDATE">
    @TableField(value = "${field.fieldName}", fill = FieldFill.INSERT_UPDATE)
    <#elseif field.autoFill == "UPDATE">
    @TableField(value = "${field.fieldName}", fill = FieldFill.UPDATE)
    <#elseif field.primaryKey>
        <#--如果是主键，仅使用@TableId注解-->
    <#else>
    @TableField(value = "${field.fieldName}")
    </#if>
    <#if field.primaryKey>
    @TableId(value = "${field.fieldName}", type = IdType.AUTO)
    </#if>
    private ${field.attrType} ${field.attrName};
</#if>

</#list>
}
