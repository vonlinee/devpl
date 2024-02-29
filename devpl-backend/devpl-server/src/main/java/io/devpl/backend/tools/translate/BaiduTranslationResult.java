package io.devpl.backend.tools.translate;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaiduTranslationResult {

    private String from;

    private String to;

    @JsonAlias(value = "trans_result")
    private List<TranslationResult> transResult;
}
