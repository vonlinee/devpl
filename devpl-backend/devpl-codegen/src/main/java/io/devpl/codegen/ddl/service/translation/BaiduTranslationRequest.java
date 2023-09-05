package io.devpl.codegen.ddl.service.translation;

import lombok.Data;

@Data
public class BaiduTranslationRequest {

    private String q;
    private String from;
    private String to;
}
