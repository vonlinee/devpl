package io.devpl.backend.domain.enums;

public enum PermissionType {

    /**
     * 允许操作
     */
    ALLOWED,

    /**
     * 不允许操作
     */
    NOT_ALLOWED,

    /**
     * 操作受限，具体问题具体处理
     */
    RESTRICTED
}
