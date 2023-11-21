/**
 * 仅依赖于JDK的基础工具包，包括一些工具类
 *
 * @since 17
 */
open module devpl.sdk.internal {
    requires com.google.common;
    requires java.sql;
    requires lombok;
    requires jsr305;
    requires java.net.http;
    requires java.datatransfer;
    requires java.desktop;

    exports io.devpl.sdk;
    exports io.devpl.sdk.util;
    exports io.devpl.sdk.lang;
    exports io.devpl.sdk.io;
    exports io.devpl.sdk.validation;
}
