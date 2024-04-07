package io.devpl.codegen.db.keywords;

import io.devpl.codegen.db.ColumnKeyWordsHandler;

import java.util.*;

/**
 * 数据库关键字处理
 * 由于只是生成代码上简单使用，目前简单的实现一下。<br/>
 * 随着数据库版本的不同，关键字会有新增或移除，建议去查询对应数据库版本文档来制订关键字集合。
 */
public abstract class BaseKeyWordsHandler implements ColumnKeyWordsHandler {

    public Set<String> keyWords;

    public BaseKeyWordsHandler(List<String> keyWords) {
        this.keyWords = new HashSet<>(keyWords);
    }

    public BaseKeyWordsHandler(Set<String> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public Collection<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public boolean isKeyWords(String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }
}
