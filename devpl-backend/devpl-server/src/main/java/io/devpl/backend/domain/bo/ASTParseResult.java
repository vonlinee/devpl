package io.devpl.backend.domain.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ASTParseResult {

    private List<ASTClassInfo> classInfoList;
}
