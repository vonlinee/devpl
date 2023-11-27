package io.devpl.generator.tools.ddl.service.translation;

import io.devpl.generator.tools.ddl.model.TranslationVO;

import java.util.List;

/**
 * 翻译服务
 */
public interface TranslationService {

    /**
     * 翻译成中文
     * @param content 需要翻译的文本
     * @return 翻译结果
     */
    List<TranslationVO> toChinese(String content);
}
