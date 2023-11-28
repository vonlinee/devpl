package io.devpl.backend.domain.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompilerParam {

    @NotEmpty(message = "文本为空")
    private String code;
}
