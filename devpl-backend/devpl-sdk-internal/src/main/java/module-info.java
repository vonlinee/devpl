/**
 * 仅依赖于JDK及一些常用的第三方库(比如json库等等)的基础工具包
 * 其中多数类是从其他库中复制过来的
 *
 * @since 17
 */
open module devpl.sdk.internal {
    requires java.sql;
    requires java.net.http;
    requires java.datatransfer;
    requires java.desktop;
    requires org.jetbrains.annotations;

    exports io.devpl.sdk;
    exports io.devpl.sdk.util;
    exports io.devpl.sdk.lang;
    exports io.devpl.sdk.io;
    exports io.devpl.sdk.validation;
    exports io.devpl.sdk.collection;
}
