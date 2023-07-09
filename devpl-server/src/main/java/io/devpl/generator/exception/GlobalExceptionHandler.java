package io.devpl.generator.exception;

import io.devpl.generator.common.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数异常
     * @param e IllegalArgumentException
     * @return Result
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Result<?> exceptionHandler(IllegalArgumentException e) {
        return Result.exception(e);
    }
}
