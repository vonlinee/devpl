package io.devpl.backend.service;

import io.devpl.backend.domain.param.JavaPojoCodeGenParam;

public interface CodeGenerationService {

    String generateJavaPojoClass(JavaPojoCodeGenParam param);

    String generatedDtoClass(JavaPojoCodeGenParam param);
}
