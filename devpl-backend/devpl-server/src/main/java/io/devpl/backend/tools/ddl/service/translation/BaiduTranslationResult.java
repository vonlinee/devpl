package io.devpl.backend.tools.ddl.service.translation;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class BaiduTranslationResult {

    private String from;

    private String to;

    @JsonAlias(value = "trans_result")
    private List<TranslationResult> transResult;
}
