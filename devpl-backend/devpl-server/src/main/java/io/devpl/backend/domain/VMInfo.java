package io.devpl.backend.domain;

import lombok.Data;

@Data
public class VMInfo {

    /**
     * 端口号
     */
    private int port;

    /**
     * 主类名
     */
    private String mainClassName;

    /**
     * 参数
     */
    private String args;
}
