package io.devpl.backend.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 作者信息
 */
@Getter
@Setter
public class Author {

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;
}
