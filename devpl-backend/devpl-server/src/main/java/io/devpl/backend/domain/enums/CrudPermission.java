package io.devpl.backend.domain.enums;

import lombok.Getter;

/**
 * CRUD操作权限
 */
@Getter
public enum CrudPermission {

    INSERT(1),
    DELETE(2),
    UPDATE(3),
    SELECT(4);

    private final int permission;

    CrudPermission(int permission) {
        this.permission = permission;
    }
}
