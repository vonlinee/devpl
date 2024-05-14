package org.apache.ddlutils.platform;

public interface PlatformTypeHandler {

    String toString(int jdbcType, Object value);
}
