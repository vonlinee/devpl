package io.devpl.generator.common.query;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class Response<T> {

    @JsonAlias(value = "code")
    private Integer code;

    @JsonAlias(value = "message")
    private String message;

    private List<T> data;
}
