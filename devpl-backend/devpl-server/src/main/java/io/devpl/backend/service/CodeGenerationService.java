package io.devpl.backend.service;

import io.devpl.backend.domain.param.FieldCopyGenParam;
import io.devpl.backend.domain.param.JavaPojoCodeGenParam;

/**
 * 各类代码生成的实现
 */
public interface CodeGenerationService {

    /**
     * 生成Java Pojo类代码
     *
     * @param param JavaPojoCodeGenParam
     * @return 代码片段
     */
    String generateJavaPojoClass(JavaPojoCodeGenParam param);

    String generatedDtoClass(JavaPojoCodeGenParam param);

    String generatePoiPojo(JavaPojoCodeGenParam param);

    /**
     * 生成字段拷贝方法代码
     *
     * @param param FieldCopyGenParam
     * @return 代码片段
     */
    String generateFieldCopyCode(FieldCopyGenParam param);
}
