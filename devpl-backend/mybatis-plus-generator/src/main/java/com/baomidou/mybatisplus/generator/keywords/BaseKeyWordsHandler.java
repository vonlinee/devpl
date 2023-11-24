package com.baomidou.mybatisplus.generator.keywords;

import com.baomidou.mybatisplus.generator.config.IKeyWordsHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 基类关键字处理
 *
 * @author nieqiurong 2020/5/8.
 * @since 3.3.2
 */
public abstract class BaseKeyWordsHandler implements IKeyWordsHandler {

    public Set<String> keyWords;

    public BaseKeyWordsHandler(@NotNull List<String> keyWords) {
        this.keyWords = new HashSet<>(keyWords);
    }

    public BaseKeyWordsHandler(@NotNull Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public @NotNull Collection<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public boolean isKeyWords(@NotNull String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }
}
