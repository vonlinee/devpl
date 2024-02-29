package io.devpl.backend.tools.translate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaiduTranslationRequest {

    private String q;
    private String from;
    private String to;
}
