package io.devpl.backend.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class ASTParseResult {

    private List<ASTClassInfo> classInfoList;
}
