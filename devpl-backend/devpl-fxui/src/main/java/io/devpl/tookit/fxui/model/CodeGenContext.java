package io.devpl.tookit.fxui.model;

import lombok.Data;

import java.util.Map;

/**
 * 代码生成参数
 */
@Data
public class CodeGenContext {

    private ProjectConfiguration projectConfiguration;

    private Map<String, TableGeneration> targetedTables;
}
