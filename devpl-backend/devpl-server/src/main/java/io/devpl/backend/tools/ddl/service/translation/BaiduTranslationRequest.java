package io.devpl.backend.tools.ddl.service.translation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaiduTranslationRequest {

    private String q;
    private String from;
    private String to;
}
